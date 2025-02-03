package ca.mcmaster.se2aa4.mazerunner;

import java.awt.Point;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Name: Yug Vashisth, MacID: vashisty, Student number: 400501750
 * Finds the entry and exit points in a given maze grid.
 * Assumes that the entry is on the left boundary and the exit is on the right boundary.
 */
public class MazeEntryExitFinder {
    
    /** Logger instance for debugging */
    private static final Logger logger = LogManager.getLogger(MazeEntryExitFinder.class);
    
    /** 2D character grid representing the maze. */
    private final char[][] mazeGrid;
    
    /** Width of the maze. */
    private final int width;
    
    /** Height of the maze. */
    private final int height;

    /**
     * Constructs a MazeEntryExitFinder object.
     */
    public MazeEntryExitFinder(char[][] mazeGrid, int width, int height) {
        this.mazeGrid = mazeGrid;
        this.width = width;
        this.height = height;
    }

    /**
     * Finds the entry and exit points in the maze.
     */
    public Point[] findEntryAndExit() {
        Point entry = null;
        Point exit = null;

        // Find entry point on the left boundary
        for (int y = 0; y < height; y++) {
            if (mazeGrid[y][0] == ' ') {
                entry = new Point(0, y);
                break;
            }
        }

        // Find exit point on the right boundary
        for (int y = 0; y < height; y++) {
            if (mazeGrid[y][width - 1] == ' ') {
                exit = new Point(width - 1, y);
                break;
            }
        }

        // Ensure both entry and exit points exist
        if (entry == null || exit == null) {
            throw new IllegalArgumentException("Maze must have both an entry and an exit.");
        }

        return new Point[]{entry, exit};
    }
}
