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
    
        
        for (int i = 0; i < height; i++) {
            if (mazeGrid[i][0] == ' ') { 
                entry = new Point(0, i);
                exit = findExitOnRightWall();
                break;
            }
        }
    
        
        if (entry == null) {
            for (int i = 0; i < height; i++) {
                if (mazeGrid[i][width - 1] == ' ') { // Right wall
                    entry = new Point(width - 1, i);
                    exit = findExitOnLeftWall();
                    break;
                }
            }
        }
    
    
        if (entry == null || exit == null) {
            throw new IllegalArgumentException("Maze must have an entry and an exit point.");
        }
    
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
