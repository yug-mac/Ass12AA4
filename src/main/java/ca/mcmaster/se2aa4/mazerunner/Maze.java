package ca.mcmaster.se2aa4.mazerunner;

import java.awt.Point;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Maze {
    private final char[][] mazeGrid;
    private final int width;
    private final int height;
    private static final Logger logger = LogManager.getLogger(Maze.class);

    public Maze(List<String> grid) {
        this.height = grid.size();
        this.width = grid.stream().mapToInt(String::length).max().orElse(0); 
        this.mazeGrid = new char[height][width];

        for (int i = 0; i < height; i++) {
            String row = grid.get(i);
            if (row.isEmpty()) {
                mazeGrid[i] = " ".repeat(width).toCharArray();
            } else {
                for (int j = 0; j < width; j++) {
                    mazeGrid[i][j] = j < row.length() ? row.charAt(j) : '#';
                }
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Point[] getEntryAndExitPoints() {
        Point entry = null;
        Point exit = null;

        for (int i = 0; i < height; i++) {
            if (mazeGrid[i][0] == ' ') { 
                entry = new Point(0, i);
            }
            if (mazeGrid[i][width - 1] == ' ') { 
                exit = new Point(width - 1, i);
            }
        }

        for (int i = 0; i < width; i++) {
            if (mazeGrid[0][i] == ' ') { 
                entry = new Point(i, 0);
            }
            if (mazeGrid[height - 1][i] == ' ') { 
                exit = new Point(i, height - 1);
            }
        }

        if (entry == null || exit == null) {
            throw new IllegalArgumentException("Maze must have both an entry and an exit.");
        }

        logger.debug("Entry Point: (" + entry.x + ", " + entry.y + ")");
        logger.debug("Exit Point: (" + exit.x + ", " + exit.y + ")");

        return new Point[]{entry, exit};
    }

    public boolean isValidMove(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height && mazeGrid[y][x] == ' ';
    }

    public void printMaze() {
        System.out.println("Processed Maze:");
        for (char[] row : mazeGrid) {
            System.out.println(new String(row));
        }
    }
}
