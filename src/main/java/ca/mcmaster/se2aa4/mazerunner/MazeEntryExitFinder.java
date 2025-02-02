package ca.mcmaster.se2aa4.mazerunner;

import java.awt.Point;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MazeEntryExitFinder {
    private static final Logger logger = LogManager.getLogger(MazeEntryExitFinder.class);
    private final char[][] mazeGrid;
    private final int width;
    private final int height;

    public MazeEntryExitFinder(char[][] mazeGrid, int width, int height) {
        this.mazeGrid = mazeGrid;
        this.width = width;
        this.height = height;
    }

    public Point[] findEntryAndExit() {
        Point entry = null;
        Point exit = null;

        for (int y = 0; y < height; y++) {
            if (mazeGrid[y][0] == ' ') {
                entry = new Point(0, y);
                //ogger.info("Entry point found at: (" + entry.x + ", " + entry.y + ")");
                break;
            }
        }
        for (int y = 0; y < height; y++) {
            if (mazeGrid[y][width - 1] == ' ') {
                exit = new Point(width - 1, y);
                //logger.info("Exit point found at: (" + exit.x + ", " + exit.y + ")");
                break;
            }
        }

        if (entry == null || exit == null) {
            throw new IllegalArgumentException("Maze must have both an entry and an exit.");
        }

        //logger.info("Entry Point: (" + entry.x + ", " + entry.y + ")");
        //ogger.info("Exit Point: (" + exit.x + ", " + exit.y + ")");

        return new Point[]{entry, exit};
    }
}
