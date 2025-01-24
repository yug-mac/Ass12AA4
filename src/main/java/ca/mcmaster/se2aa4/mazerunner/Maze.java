package ca.mcmaster.se2aa4.mazerunner;

import java.awt.Point; 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Maze {
    private final char[][] mazeGrid;
    private final int width;
    private final int height;
    private static final Logger logger = LogManager.getLogger(Maze.class);

    public Maze(String filePath) throws IOException {
        List<String> grid = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) { 
                    grid.add(line);
                }
            }
        }

        if (grid.isEmpty()) {
            throw new IllegalArgumentException("Maze file is empty or contains only empty lines.");
        }

    
        int width = grid.get(0).length();
        for (String row : grid) {
            if (row.length() != width) {
                throw new IllegalArgumentException("Inconsistent row lengths in maze file.");
            }
        }

        this.width = width;
        this.height = grid.size();
        this.mazeGrid = new char[height][width];
        for (int i = 0; i < height; i++) {
            mazeGrid[i] = grid.get(i).toCharArray();
        }

    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

   
    public Point[] getEntryandExitPoint() {
        Point entry = null;
        Point exit = null;
    
        // Check for entry point on the left wall and corresponding exit on the right wall
        for (int i = 0; i < height; i++) {
            if (mazeGrid[i][0] == ' ') { // Left wall
                entry = new Point(0, i);
                exit = findExitOnRightWall(); // Find corresponding exit
                break;
            }
        }
    
        // If no entry found on the left wall, check the right wall and corresponding exit on the left wall
        if (entry == null) {
            for (int i = 0; i < height; i++) {
                if (mazeGrid[i][width - 1] == ' ') { // Right wall
                    entry = new Point(width - 1, i);
                    exit = findExitOnLeftWall(); // Find corresponding exit
                    break;
                }
            }
        }
    
        // Validate that both entry and exit points are found
        if (entry == null || exit == null) {
            logger.error("Maze does not have a valid entry and exit point.");
            throw new IllegalArgumentException("Maze must have an entry and an exit point.");
        }
    
        logger.info("Entry point found at: ({}, {}), Exit point found at: ({}, {})",
                    entry.x, entry.y, exit.x, exit.y);
        return new Point[]{entry, exit};
    }
    
    
    
    private Point findExitOnRightWall() {
        for (int i = 0; i < height; i++) {
            if (mazeGrid[i][width - 1] == ' ') {
                return new Point(width - 1, i);
            }
        }
        return null;
    }
    
  
    private Point findExitOnLeftWall() {
        for (int i = 0; i < height; i++) {
            if (mazeGrid[i][0] == ' ') {
                return new Point(0, i);
            }
        }
        return null;
    }
    
    

    public boolean isValidMove(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        }
        if (mazeGrid[y][x] != ' ') {
            return false;
        }
        return true;
    }

    public boolean isExit(int x, int y) {
        return x == width - 1 && mazeGrid[y][x] == ' '; 
    }
}
