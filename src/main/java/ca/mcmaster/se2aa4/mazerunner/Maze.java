package ca.mcmaster.se2aa4.mazerunner;

import java.awt.Point;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Maze {
    private static final Logger logger = LogManager.getLogger(Maze.class);
    private final char[][] mazeGrid;
    private final int width;
    private final int height;

    public Maze(List<String> grid) {
        this.height = grid.size();
        this.width = grid.stream().mapToInt(String::length).max().orElse(0);
    
        
        this.mazeGrid = new char[height][width];
    
        for (int i = 0; i < height; i++) {
            String row = grid.get(i);
            int rowLength = row.length();
            
            if (rowLength < width) {
                row = String.format("%-" + width + "s", row).replace(' ', ' '); 
            }
    
            mazeGrid[i] = row.toCharArray();
            
        }
    
    }
    

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Point[] getEntryAndExitPoints() {
        MazeEntryExitFinder finder = new MazeEntryExitFinder(mazeGrid, width, height);
        return finder.findEntryAndExit();
    }

    public boolean isValidMove(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height && mazeGrid[y][x] == ' ';
    }
}
