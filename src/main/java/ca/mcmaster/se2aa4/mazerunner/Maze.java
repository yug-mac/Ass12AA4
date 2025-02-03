package ca.mcmaster.se2aa4.mazerunner;

import java.awt.Point;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Name: Yug Vashisth
 * MacID: vashisty, 400510750
 * Represents a maze structure with a grid-based representation.
 * Provides methods to access maze dimensions, find entry/exit points,
 * and validate moves within the maze.
 *
 */
public class Maze {
    
    /** Logger instance for logging debugging
    private static final Logger logger = LogManager.getLogger(Maze.class);
    
    /** 2D character array representing the maze grid. */
    private final char[][] mazeGrid;
    
    /** Width of the maze (number of columns). */
    private final int width;
    
    /** Height of the maze (number of rows). */
    private final int height;
    
    /**
     * Constructs a Maze object from a list of string rows.
     * Each row is converted into a character array to represent the maze grid.
     */
    public Maze(List<String> grid) {
        this.height = grid.size();
        this.width = grid.stream().mapToInt(String::length).max().orElse(0);
    
        // Initialize the maze grid
        this.mazeGrid = new char[height][width];
    
        for (int i = 0; i < height; i++) {
            String row = grid.get(i);
            int rowLength = row.length();
            
            // Ensure each row is properly formatted to match the width of the maze
            if (rowLength < width) {
                row = String.format("%-" + width + "s", row).replace(' ', ' '); 
            }
    
            // Convert the row into a character array and store it in the grid
            mazeGrid[i] = row.toCharArray();
        }
    }
    
    /**
     * Returns the width of the maze.
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Returns the height of the maze.
     */
    public int getHeight() {
        return height;
    }
    
    /**
     * Finds and returns the entry and exit points of the maze.
*/
    public Point[] getEntryAndExitPoints() {
        MazeEntryExitFinder finder = new MazeEntryExitFinder(mazeGrid, width, height);
        return finder.findEntryAndExit();
    }
    
    /**
     * Checks whether a given coordinate (x, y) is a valid move within the maze.
     * A move is valid if it is within the maze boundaries and on an open path (' ').
*/
    public boolean isValidMove(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height && mazeGrid[y][x] == ' ';
    }
}
