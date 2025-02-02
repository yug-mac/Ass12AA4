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
    
        System.out.println("Reading maze... Expected width: " + width);
        
        this.mazeGrid = new char[height][width];
    
        for (int i = 0; i < height; i++) {
            String row = grid.get(i);
            int rowLength = row.length();
            
            if (rowLength < width) {
                row = String.format("%-" + width + "s", row).replace(' ', ' '); 
            }
    
            mazeGrid[i] = row.toCharArray();
            

            System.out.println("Row " + i + " (length " + row.length() + "): " + row);
        }
    
        printMaze();  
    }
    

    public void printMaze() {
        System.out.println("\nMaze Layout:");
        logger.info("Maze Layout:");
        
        for (int y = 0; y < height; y++) {
            StringBuilder row = new StringBuilder();
            for (int x = 0; x < width; x++) {
                row.append(mazeGrid[y][x]);  
            }
            System.out.println(row.toString());  
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Point[] getEntryAndExitPoints() {
        // Delegating entry/exit point detection to a separate class
        MazeEntryExitFinder finder = new MazeEntryExitFinder(mazeGrid, width, height);
        return finder.findEntryAndExit();
    }

    public boolean isValidMove(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height && mazeGrid[y][x] == ' ';
    }
}
