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
        Point entry = null;
        Point exit = null;

        
        for (int y = 0; y < height; y++) {
            if (mazeGrid[y][0] == ' ') {
                entry = new Point(0, y);
                logger.info("Entry point found at: (" + entry.x + ", " + entry.y + ")");
                break;
            }
        }

       
        for (int y = 0; y < height; y++) {
            if (mazeGrid[y][width - 1] == ' ') {
                exit = new Point(width - 1, y);
                logger.info("Exit point found at: (" + exit.x + ", " + exit.y + ")");
                break;
            }
        }

        if (entry == null || exit == null) {
            throw new IllegalArgumentException("Maze must have both an entry and an exit.");
        }

        logger.info("Entry Point: (" + entry.x + ", " + entry.y + ")");
        logger.info("Exit Point: (" + exit.x + ", " + exit.y + ")");

        return new Point[]{entry, exit};
    }

    public boolean isValidMove(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height && mazeGrid[y][x] == ' ';
    }
}