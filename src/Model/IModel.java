package Model;
import algorithms.mazeGenerators.Maze;
import javafx.scene.input.KeyCode;

import java.util.Observer;


/**
 * IModel Interface.
 * This interface is responsible for defining methods for Model layer objects.
 */

public interface IModel {

    /**
     * method for generating the maze.
     * @param rows: maze width size.
     * @param cols: maze height size.
     */
    void generateMaze(int rows, int cols);


    /**
     * start and run the servers.
     */
    void startServers();


    /**
     * stop the running servers.
     */
    void stopServers();


    /**
     * method which responsible on moving the Character.
     * @param movement: key press to move by user.
     */
    void moveCharacter(KeyCode movement);


    /**
     * getter for the maze board.
     * @return maze board.
     */
    int[][] getMazeArray();


    /**
     * getter for the current Character position row.
     * @return Character position row.
     */
    int getPlayerRowPosition();


    /**
     * setter for the current Character position row.
     * @param row: Character position row.
     */
    public void setPlayerRowPos(int row);


    /**
     * getter for the current Character position col.
     * @return Character position col.
     */
    int getPlayerColPosition();


    /**
     * setter for the current Character position column.
     * @param col: Character position column.
     */
    public void setPlayerColPos(int col);


    /**
     * getter for the maze object.
     * @return maze object.
     */
    Maze getMaze();


    /**
     * setter for the maze.
     * @param maze: maze to set.
     */
    public void setMaze(Maze maze);


    /**
     * setter for new boolean value to finish variable.
     * @param finish: boolean.
     */
    void setFinish(boolean finish);


    /**
     * showing the solution.
     */
    void showSolution();


    /**
     * saving the maze into file.
     * @param fileName: name of the file.
     */
    void saveMaze(String fileName);


    /**
     * loading the maze from file.
     * @param fileName: name of the file.
     */
    void loadMaze(String fileName);


    /**
     * override method for assigning the Observer.
     * @param o: Observer.
     */
    void assignObserver(Observer o);


    /**
     * exit the program.
     */
    void exitProgram();

}
