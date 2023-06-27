package ViewModel;
import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;
import java.util.Observable;
import java.util.Observer;


/**
 * MyViewModel class.
 * This Class extends the Observable Class and implements the Observer Interface.
 * This class is responsible for the connection between the View layer and the Model layer.
 */

public class MyViewModel extends Observable implements Observer {

    private IModel model;
    private int playerRowPos;
    private int playerColPos;
    public StringProperty strPlayerRowPos = new SimpleStringProperty(""); //For Binding
    public StringProperty strPlayerColPos = new SimpleStringProperty(""); //For Binding


    /**
     * Constructor.
     * @param model: the Model layer.
     */
    public MyViewModel(IModel model) {
        this.model = model;
    }


    /**
     * override method for updating the observers.
     * @param o: the observable object.
     * @param arg: an argument passed to the {@code notifyObservers} method.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Boolean && (Boolean) arg){
            setChanged();
            notifyObservers(arg);
        }

        else if (arg instanceof Solution){
            setChanged();
            notifyObservers(arg);
        }

        else{
            playerRowPos = model.getPlayerRowPosition();
            strPlayerRowPos.set(playerRowPos + 1 + "");
            playerColPos = model.getPlayerColPosition();
            strPlayerColPos.set(playerColPos + 1 + "");

            setChanged();
            notifyObservers(arg);
        }
    }


    public void generateMaze(int rows, int cols){
        model.generateMaze(rows, cols);

    }


    public void moveCharacter(KeyCode movement){
        model.moveCharacter(movement);
    }


    public int[][] getMazeArray(){
        return model.getMazeArray();
    }


    public int getPlayerRowPosition(){
        return model.getPlayerRowPosition();
    }


    public int getPlayerColPosition(){
        return model.getPlayerColPosition();
    }


    public void setModelPlayerRowPos(int row){
        model.setPlayerRowPos(row);
    }


   public void setModelPlayerColPos(int col){
        model.setPlayerColPos(col);
   }

    public Maze getMaze(){
        return model.getMaze();
    }


    public void setModelMaze(Maze maze){
        model.setMaze(maze);
    }


    public void showSolution() {
        model.showSolution();
        setChanged();
        notifyObservers();
    }


    public void setFinish(Boolean flag){
        model.setFinish(flag);
    }


    public void saveMaze(String fileName){
        model.saveMaze(fileName);
    }


    public void loadMaze(String fileName){
        model.loadMaze(fileName);
    }


    public void exit(){
        model.exitProgram();
    }

}
