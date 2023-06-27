package View;
import algorithms.mazeGenerators.Maze;


/**
 * IView Interface.
 * this interface is responsible for defining method for View layer object.
 */

public interface IView {

    /**
     * method which responsible on displaying the maze.
     * @param maze: the Maze object to be displayed.
     */
    void displayMaze(Maze maze);

}