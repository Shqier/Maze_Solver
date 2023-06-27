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


public class MazeDisplayer extends Canvas {

    private Maze maze;
    private int characterPositionRow = 1;
    private int characterPositionColumn = 1;
    private int finalPositionRow = 1;
    private int finalPositionColumn = 1;
    private ArrayList<AState> solutionPath;
    private boolean ifDisplaySolution = false;
    private StringProperty imageFileNameWall = new SimpleStringProperty("./resources/images/wall.jpeg");
    private StringProperty imageFileNameCharacter = new SimpleStringProperty("./resources/images/player.png");
    private StringProperty imageFileNameFinish = new SimpleStringProperty("./resources/images/finish.png");


    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNameCharacter() {
        return imageFileNameCharacter.get();
    }

    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.imageFileNameCharacter.set(imageFileNameCharacter);
    }

    public String getImageFileNameFinish() {
        return imageFileNameFinish.get();
    }

    public void setImageFileNameFinish(String imageFileNameFinish) {
        this.imageFileNameFinish.set(imageFileNameFinish);
    }

    public void setCharacterPosition(int row, int column) {
        int oldRow = characterPositionRow;
        int oldColumn = characterPositionColumn;
        characterPositionRow = row;
        characterPositionColumn = column;
        redrawCharacter(oldRow, oldColumn);
    }

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public void setCharacterPositionRow(int row) {
        characterPositionRow = row;
    }

    public void setCharacterPositionCol(int col) {
        characterPositionColumn = col;
    }

    public void setGoalPositionRow(int row) {
        finalPositionRow = row;
    }

    public void setGoalPositionCol(int col) {
        finalPositionColumn = col;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
        redraw();
    }

    public void setIfDisplaySolution(boolean flag) {
        ifDisplaySolution = flag;
    }

    public void redraw() {
        if (maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.getMaze().length;
            double cellWidth = canvasWidth / maze.getMaze()[0].length;
            try {
                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());
                if (!ifDisplaySolution) {
                    gc.setGlobalAlpha(1);
                }
                Image wallImage = new Image(new FileInputStream(imageFileNameWall.get()));
                Image finishImage = new Image(new FileInputStream(imageFileNameFinish.get()));
                for (int i = 0; i < maze.getMaze().length; i++) {
                    for (int j = 0; j < maze.getMaze()[i].length; j++) {
                        if (maze.getMaze()[i][j] == 1) {
                            gc.drawImage(wallImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                        }
                    }
                }
                gc.drawImage(finishImage, maze.getGoalPosition().getColumnIndex() * cellWidth,
                        maze.getGoalPosition().getRowIndex() * cellHeight, cellWidth, cellHeight);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void redrawCharacter(int oldRow, int oldCol) {
        try {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / maze.getMaze().length;
            double cellWidth = canvasWidth / maze.getMaze()[0].length;
            GraphicsContext gc = getGraphicsContext2D();
            gc.clearRect(oldCol * cellWidth, oldRow * cellHeight, cellWidth, cellHeight);
            Image characterImage = new Image(new FileInputStream(imageFileNameCharacter.get()));
            gc.drawImage(characterImage, characterPositionColumn * cellWidth, characterPositionRow * cellHeight,
                    cellWidth, cellHeight);
            MazeState mazeState = new MazeState(null, null, new Position(oldRow, oldCol));
            if (ifDisplaySolution && solutionPath.contains(mazeState)) {
                gc.fillRect(oldCol * cellWidth, oldRow * cellHeight, cellWidth, cellHeight);
            }
            if (!ifDisplaySolution) {
                gc.setGlobalAlpha(1);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void displaySolution(ArrayList<AState> solutionPath) {
        ifDisplaySolution = true;
        this.solutionPath = solutionPath;
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        double cellHeight = canvasHeight / maze.getMaze().length;
        double cellWidth = canvasWidth / maze.getMaze()[0].length;
        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.BROWN);
        gc.setGlobalAlpha(0.4);
        for (int i = 0; i < solutionPath.size(); i++) {
            gc.fillRect(((MazeState) solutionPath.get(i)).getPosition().getColumnIndex() * cellWidth,
                    ((MazeState) solutionPath.get(i)).getPosition().getRowIndex() * cellHeight, cellWidth, cellHeight);
        }
    }

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
            Scene scene = new Scene(pane, getWidth(), getHeight());
            Stage newStage = new Stage();
            newStage.setTitle("You did it!");
            newStage.getIcons().add(new Image("./resources/images/icon.jpg"));
            newStage.setScene(scene);
            newStage.initModality(Modality.APPLICATION_MODAL);

            Button button = new Button();
            button.setText("Play Again!");
            button.setLayoutX(150);
            button.setLayoutY(370);
            button.setOnAction(event -> {
                newStage.close();
                event.consume();
            });

            finishGif.setImage(finish);
            pane.getChildren().addAll(finishGif, button);
            newStage.initOwner(View.Main.mainStage);

            newStage.showAndWait();

        } catch (FileNotFoundException e) {
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

    public void zoom() {
        setOnScroll(event -> {
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
        });
    }
}
