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
        this.width = grid.get(0).length();
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
    
        return new Point[]{entry, exit};
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
}
