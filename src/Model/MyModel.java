package Model;
import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import Server.*;
import Client.*;
import java.util.Observable;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Observer;


/**
 * MyModel Class.
 * The Class extends the Observable Class and implements the IModel Interface.
 * This Class is responsible on the logic part and communicate with the ViewModel layer.
 */

public class MyModel extends Observable implements IModel{

    private Maze maze;
    private int playerRowPos = 1;
    private int playerColPos = 1;
    private Solution solution;
    private boolean finish;
    private int goalRowPos;
    private int goalColPos;
    private Server mazeGeneratorServer;
    private Server mazeSolverServer;
    private HashMap<Integer, String> solvedMazes;


    /**
     * Constructor, opens a server and set the attributes.
     */
    public MyModel(){
        this.mazeGeneratorServer = new Server(5400,1000,new ServerStrategyGenerateMaze());
        this.mazeSolverServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
        this.finish = false;
        this.startServers();
        this.solvedMazes = new HashMap<>();
    }


    /**
     * method for generating the maze.
     * @param rows: maze width size.
     * @param cols: maze height size.
     */
    @Override
    public void generateMaze(int rows, int cols) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer){
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDim = new int[]{rows, cols};
                        toServer.writeObject(mazeDim);
                        toServer.flush();

                        byte[] compressedMaze = (byte[])fromServer.readObject();
                        InputStream in = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[rows * cols + 12];
                        in.read(decompressedMaze);

                        maze = new Maze(decompressedMaze);
                        playerRowPos = maze.getStartPosition().getRowIndex();
                        playerColPos = maze.getStartPosition().getColumnIndex();
                        goalRowPos = maze.getGoalPosition().getRowIndex();
                        goalColPos = maze.getGoalPosition().getColumnIndex();

                        maze.print();
                        System.out.println();
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }
                }
            });

            client.start();
        }
        catch(UnknownHostException e){
            e.printStackTrace();
        }

        setChanged();
        notifyObservers();
    }


    /**
     * start and run the servers.
     */
    @Override
    public void startServers() {
        this.mazeGeneratorServer.start();
        this.mazeSolverServer.start();
    }


    /**
     * stop the running servers.
     */
    @Override
    public void stopServers() {
        this.mazeGeneratorServer.stop();
        this.mazeSolverServer.stop();
    }


    /**
     * method which responsible on moving the Character.
     * @param movement: key press to move by user.
     */
    @Override
    public void moveCharacter(KeyCode movement) {
        if (!finish){
            switch (movement){
                //case UP:
                case NUMPAD8:
                    if (playerRowPos - 1 >= 0 && playerRowPos - 1 < maze.getRowIndex() && maze.getCellValue(playerRowPos - 1, playerColPos) == 0){
                        playerRowPos--;
                    }
                    break;


                //case RIGHT:
                case NUMPAD6:
                    if (playerColPos + 1 < maze.getColumnIndex() && maze.getCellValue(playerRowPos, playerColPos + 1) == 0){
                        playerColPos++;
                    }
                    break;


                //case DOWN:
                case NUMPAD2:
                    if (playerRowPos + 1 < maze.getRowIndex() && maze.getCellValue(playerRowPos + 1, playerColPos) == 0){
                        playerRowPos++;
                    }
                    break;


                //case LEFT:
                case NUMPAD4:
                    if (playerColPos - 1 >= 0 && playerColPos - 1 < maze.getColumnIndex() && maze.getCellValue(playerRowPos, playerColPos - 1) == 0){
                        playerColPos--;
                    }
                    break;


                case NUMPAD9:
                    if (playerRowPos - 1 >= 0 && playerColPos + 1 < maze.getColumnIndex() && maze.getCellValue(playerRowPos - 1, playerColPos + 1) == 0 && (maze.getCellValue(playerRowPos, playerColPos + 1) == 0 || maze.getCellValue(playerRowPos - 1, playerColPos) == 0)){
                        playerRowPos--;
                        playerColPos++;
                    }
                    break;


                case NUMPAD3:
                    if (playerRowPos + 1 < maze.getRowIndex() && playerColPos + 1 < maze.getColumnIndex() && maze.getCellValue(playerRowPos + 1, playerColPos + 1) == 0 && (maze.getCellValue(playerRowPos, playerColPos + 1) == 0 || maze.getCellValue(playerRowPos + 1, playerColPos) == 0)){
                        playerRowPos++;
                        playerColPos++;
                    }
                    break;


                case NUMPAD1:
                    if (playerRowPos + 1 < maze.getRowIndex() && playerColPos - 1 >= 0 && maze.getCellValue(playerRowPos + 1, playerColPos - 1) == 0 && (maze.getCellValue(playerRowPos + 1, playerColPos) == 0 || maze.getCellValue(playerRowPos, playerColPos - 1) == 0)){
                        playerRowPos++;
                        playerColPos--;
                    }
                    break;


                case NUMPAD7:
                    if (playerRowPos - 1 >= 0 && playerColPos - 1 >= 0 && maze.getCellValue(playerRowPos - 1, playerColPos - 1) == 0 && (maze.getCellValue(playerRowPos - 1, playerColPos) == 0 || maze.getCellValue(playerRowPos, playerColPos - 1) == 0)){
                        playerRowPos--;
                        playerColPos--;
                    }
                    break;

            }
        }

        if (playerRowPos == maze.getGoalPosition().getRowIndex() && playerColPos == maze.getGoalPosition().getColumnIndex())
            finish = true;

        setChanged();
        notifyObservers(finish);
    }


    /**
     * getter for the maze board.
     * @return maze board.

     */
    @Override
    public int[][] getMazeArray() {
        if (this.maze != null)
            return this.maze.getMaze();

        else
            return null;
    }


    /**
     * getter for the current Character position row.
     * @return Character position row.

     */
    @Override
    public int getPlayerRowPosition() {
        if (this.playerRowPos >= 0 && this.playerRowPos <= this.maze.getRowIndex())
            return this.playerRowPos;

        else
            return -1;

    }


    /**
     * setter for the current Character position row.
     * @param row: Character position row.
     */
    public void setPlayerRowPos(int row){
        playerRowPos = row;
    }


    /**
     * getter for the current Character position column.
     * @return Character position column.
     */
    @Override
    public int getPlayerColPosition() {
        if (this.playerColPos >= 0 && this.playerColPos <= this.maze.getColumnIndex())
            return this.playerColPos;

        else
            return -1;
    }


    /**
     * setter for the current Character position column.
     * @param col: Character position column.
     */
    public void setPlayerColPos(int col){
        playerColPos = col;
    }


    /**
     * getter for the maze object.
     * @return maze object.
     */
    @Override
    public Maze getMaze() {
        if (this.maze != null)
            return this.maze;

        else
            return null;
    }


    /**
     * setter for the maze.
     * @param maze: maze to set.
     */
    public void setMaze(Maze maze){
        if (maze != null)
            this.maze = maze;
    }


    /**
     * setter for new boolean value to finish variable.
     * @param finish: boolean.
     */
    @Override
    public void setFinish(boolean finish) {
        this.finish = finish;
    }


    /**
     * showing the solution.
     */
    @Override
    public void showSolution() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        solution = (Solution) fromServer.readObject();
                        //ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();
                        //display.Solve_Maze(mazeSolutionSteps) ; // draw the path from the start place to the goal
                        setChanged();
                        notifyObservers(solution);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }


    /** ######################################TO REMOVE
     * saving the maze into file.
     * @param fileName: name of the file.
     */
    @Override
    public void saveMaze(String fileName) {
        if (maze != null){
            try {
                maze.setStartPosition(playerRowPos, playerColPos);
                String tempDirectoryPath = System.getProperty("java.io.tmpdir");
                int key = maze.hashCode();
                String path = tempDirectoryPath + "/" + fileName + ".txt";
                File file = new File(path);
                file.createNewFile();
                if (!solvedMazes.containsKey(key)){
                    OutputStream out = new MyCompressorOutputStream(new FileOutputStream(path));
                    out.write(maze.toByteArray());
                    out.flush();
                    out.close();
                }
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
    }


    /** ######################################TO REMOVE
     * loading the maze from file.
     * @param fileName: name of the file.
     */
    @Override
    public void loadMaze(String fileName) {
        String tempDirectoryPath = System.getProperty("java.io.tmpdir");
        //int key = maze.hashCode();
        //String path = tempDirectoryPath + "/" + fileName + ".txt";
        File folder = new File(tempDirectoryPath);
        File[] listOfFiles = folder.listFiles();
        String path = fileName + ".txt";
        boolean flag = false;
        try {
            for (File file : listOfFiles) {
                if (file.getName().equals(path))
                    flag = true;
            }

            if (flag) {
                InputStream in = new MyDecompressorInputStream(new FileInputStream(tempDirectoryPath + "/" + fileName + ".txt"));
                BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(tempDirectoryPath + "/" + fileName + ".txt")));
                input.readLine();
                int rows = Integer.parseInt(input.readLine());
                input.readLine();
                int cols = Integer.parseInt(input.readLine());
                System.out.println(rows + "" + cols);
                byte[] savedMazeBytes = new byte[rows * cols + 12];
                in.read(savedMazeBytes);

                maze = new Maze(savedMazeBytes);
                maze.print();
                playerRowPos = maze.getStartPosition().getRowIndex();
                playerColPos = maze.getStartPosition().getColumnIndex();
                goalRowPos = maze.getGoalPosition().getRowIndex();
                goalColPos = maze.getGoalPosition().getColumnIndex();
                in.close();
            }

            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("This file doesn't exist!"); //edit
                alert.show();
            }
        }

        catch (Exception e) { }

        setChanged();
        notifyObservers();
    }


    /**
     * override method for assigning the Observer.
     * @param o: Observer.
     */
    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }


    /**
     * exit the program.
     */
    @Override
    public void exitProgram() {
        this.stopServers();
    }

}
