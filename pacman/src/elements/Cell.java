package elements;

import javafx.geometry.Rectangle2D;

/**
 * The purpose of this class is helping in check collisions with the walls
 * and the ghosts house.
 * 
 * @author Maxsuel F. de Almeida
 */
public class Cell {
    private final int row;
    private final int col;
    
    /**
     * Constructor of the class.
     * 
     * @param row The cell's row
     * @param col The cell's column
     */
    public Cell(int row, int col)
    {
        this.row = row;
        this.col = col;
    }
    
    /**
     * Given the maze, check if the cell is a wall.
     * 
     * @param maze The game data maze
     * @return boolean
     */
    public boolean isWall(char[][] maze)
    {
        return maze[row][col] == '#';
    }
    
    public boolean isGhostHouse(char[][] maze)
    {
        return maze[row][col] == '*';
    }

    public boolean isLeftPortal(char[][] maze) {
        return (row == (int) (maze.length / 2 - 1) && col == 0);
    }

    public boolean isRightPortal(char maze[][]) {
        return (row == (int) (maze.length / 2 - 1) && col == maze[0].length - 1);
    }
}
