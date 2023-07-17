package View;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;


/**
 * Class MazeDisplayer.
 * The Class extends the Canvas Class.
 * This class is responsible for drawing the maze.
 */

public class MazeDisplayer extends Canvas {

    private Maze maze;
    private int characterPositionRow = 1;
    private int characterPositionColumn = 1;
    private int finalPositionRow = 1;
    private int finalPositionColumn = 1;
    private ArrayList<AState> solutionPath;
    private boolean ifDisplaySolution = false;
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();
    private StringProperty ImageFileNameFinish = new SimpleStringProperty();



    /**
     * @return the image file name of the wall.
     */
    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }


    /**
     * setting new param for the image file name of the wall.
     * @param imageFileNameWall: image to set.
     */
    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }


    /**
     * @return the image file name of the character.
     */
    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }


    /**
     * setting new param for the image file name of the character.
     * @param imageFileNameCharacter: image to set.
     */
    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);
    }


    /**
     * @return the image file name of the finish state.
     */
    public String getImageFileNameFinish() {
        return ImageFileNameFinish.get();
    }


    /**
     * setting new param for the image file name of the finish state.
     * @param imageFileNameFinish: image to set.
     */
    public void setImageFileNameFinish(String imageFileNameFinish) {
        this.ImageFileNameFinish.set(imageFileNameFinish);
    }


    /**
     * setting new position to maze character.
     * @param row: row index
     * @param column: col index.
     */
    public void setCharacterPosition(int row, int column) {
        int oldRow = characterPositionRow;
        int oldColumn = characterPositionColumn;
        characterPositionRow = row;
        characterPositionColumn = column;
        redrawCharacter(oldRow, oldColumn);
    }


    /**
     * @return the row position of the character.
     */
    public int getCharacterPositionRow() {
        return characterPositionRow;
    }


    /**
     * setter for the player row position.
     * @param row: player row position.
     */
    public void setCharacterPositionRow(int row){
        characterPositionRow = row;
    }


    /**
     * setter for the player column position.
     * @param col: player column position.
     */
    public void setCharacterPositionCol(int col){
        characterPositionColumn = col;
    }


    /**
     * setter for the goal row position.
     * @param row: goal row position.
     */
    public void setGoalPositionRow(int row){
        finalPositionRow = row;
    }


    /**
     * setter for the goal column position.
     * @param col: goal column position.
     */
    public void setGoalPositionCol(int col){
        finalPositionColumn = col;
    }


    /**
     * @return the col position of the character.
     */
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }


    /**
     * setting new maze.
     * @param maze: maze to set.
     */
    public void setMaze(Maze maze) {
        this.maze = maze;
        redraw();
    }


    /**
     *
     * @param flag
     */
    public void setIfDisplaySolution(boolean flag){
        ifDisplaySolution = flag;
    }


    /**
     * first drawing maze when setting new maze.
     */
    public void redraw() {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.getMaze().length;
            double cellWidth = canvasWidth / maze.getMaze()[0].length;
            try {
                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());
                if (!ifDisplaySolution){
                    gc.setGlobalAlpha(1);
                }
                ImageFileNameWall.set("./resources/images/wall.jpeg");
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                ImageFileNameFinish.set("./resources/images/finish.png");
                Image finishImage = new Image(new FileInputStream(ImageFileNameFinish.get()));
                //Draw Maze
                for (int i = 0; i < maze.getMaze().length; i++) {
                    for (int j = 0; j < maze.getMaze()[i].length; j++) {
                        if (maze.getMaze()[i][j] == 1) {
                            gc.drawImage(wallImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                        }
                    }
                }
                gc.drawImage(finishImage, maze.getGoalPosition().getColumnIndex() * cellWidth, maze.getGoalPosition().getRowIndex() * cellHeight, cellWidth, cellHeight);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * method which responsible on clearing the Character from previous position.
     * and drawing her at the updated position.
     * @param: oldRow the previous raw coordinate.
     * @param: oldCol the previous col coordinate.
     */
    public void redrawCharacter(int oldRow, int oldCol) {
        try {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.getMaze().length; //row
            double cellWidth = canvasWidth / maze.getMaze()[0].length; //column
            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(oldCol * cellWidth, oldRow * cellHeight, cellWidth, cellHeight);
            ImageFileNameCharacter.set("./resources/images/player.png");
            Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
            gc.drawImage(characterImage, characterPositionColumn * cellWidth, characterPositionRow * cellHeight, cellWidth, cellHeight);
            MazeState mazeState = new MazeState(null,null, new Position(oldRow, oldCol));
            if (ifDisplaySolution && solutionPath.contains(mazeState)) {
                gc.fillRect(oldCol * cellWidth, oldRow * cellHeight, cellWidth, cellHeight);
            }
            if (!ifDisplaySolution)
                gc.setGlobalAlpha(1);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * method which responsible on displaying the solution path.
     * @param solutionPath: ArrayList of AStates combining the solution path.
     */
    public void displaySolution(ArrayList<AState> solutionPath) {
        ifDisplaySolution = true;
        this.solutionPath = solutionPath;
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        double cellHeight = canvasHeight / maze.getMaze().length; //row
        double cellWidth = canvasWidth / maze.getMaze()[0].length; //column
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.BROWN);
        gc.setGlobalAlpha(0.4);
        for (int i = 0; i < solutionPath.size(); i++) {
            gc.fillRect(((MazeState) solutionPath.get(i)).getPosition().getColumnIndex() * cellWidth, ((MazeState) solutionPath.get(i)).getPosition().getRowIndex() * cellHeight, cellWidth, cellHeight);
        }
    }


    /**
     * method which display gif to the winner.
     */
    public void finishDisplay() {
        try {
            Random rand = new Random();
            int num = rand.nextInt(4);
            String path = "resources/images/finishFlag" + num + ".gif";

            Image finish = new Image(new FileInputStream(path));
            ImageView finishGif = new ImageView();
            finishGif.setImage(finish);
            finishGif.setFitHeight(getHeight());
            finishGif.setFitWidth(getWidth());


            Pane pane = new Pane();
            Scene scene = new Scene(pane, getWidth(),getHeight());
            Stage newStage = new Stage();
            newStage.setTitle("You did it!");
            newStage.getIcons().add(new Image("./resources/images/icon.jpg"));
            newStage.setScene(scene);
            newStage.initModality(Modality.APPLICATION_MODAL);

            Button button = new Button();
            button.setText("Play Again!");
            button.setLayoutX(150);
            button.setLayoutY(370);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    newStage.close();
                    event.consume();
                }
            });

            finishGif.setImage(finish);
            pane.getChildren().addAll(finishGif, button);
            newStage.initOwner(View.Main.mainStage);

            newStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * method which responsible on getting the maze object.
     * @return the maze object.
     */
    public Maze getMaze() {
        return maze;
    }


    /**
     * method which responsible on zooming in and out the gui screen size.
     */
    public void zoom() {
        setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.isControlDown()) {
                    double zoom = 1.05;
                    double y = event.getDeltaY();
                    double x = event.getDeltaX();
                    if (y < 0) {
                        zoom = 0.95;
                    } else {
                        zoom = 1.05;
                    }
                    setScaleX(getScaleX() * zoom);
                    setScaleY(getScaleY() * zoom);
                    event.consume();
                }
            }
        });
    }
}


