package ca.mcmaster.se2aa4.mazerunner;

import java.awt.Point;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MazeExplorer {
    private static final Logger logger = LogManager.getLogger(MazeExplorer.class);
    private final Maze maze;
    private int x; 
    private int y;
    private char currentDirection; 
    private final Point exitPoint; 

    public MazeExplorer(Maze maze) {
        this.maze = maze;
    
        Point[] entryAndExit = maze.getEntryandExitPoint(); 
        Point entryPoint = entryAndExit[0];
        this.exitPoint = entryAndExit[1];
    

        this.x = entryPoint.x;
        this.y = entryPoint.y;
    
        
        if (x == 0) { 
            this.currentDirection = 'E'; 
        } else if (x == maze.getWidth() - 1) { 
            this.currentDirection = 'W'; 
        }
    
        
    }
    
    

    public Path computePath() {
        Path path = new Path();
    
        try {
            while (x != exitPoint.x || y != exitPoint.y) {
                if (canMoveForward()) {
                    moveForward();
                    path.addStep('F');
                } else if (canMoveLeft()) {
                    turnLeft();
                    path.addStep('L');
                    moveForward();
                    path.addStep('F');
                } else if (canMoveRight()) {
                    turnRight();
                    path.addStep('R');
                    moveForward();
                    path.addStep('F');
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("Error during path computation: {}", e.getMessage(), e);
        }
    
        return path;
    }
    
    
    private boolean canMoveLeft() {
        int newX = x;
        int newY = y;
    
        if (currentDirection == 'N') {
            newX -= 1; 
        } else if (currentDirection == 'E') {
            newY -= 1; 
        } else if (currentDirection == 'S') {
            newX += 1; 
        } else if (currentDirection == 'W') {
            newY += 1; 
        }
    
        return maze.isValidMove(newX, newY);
    }
    
    private boolean canMoveRight() {
        int newX = x;
        int newY = y;
    
        if (currentDirection == 'N') {
            newX += 1; 
        } else if (currentDirection == 'E') {
            newY += 1; 
        } else if (currentDirection == 'S') {
            newX -= 1; 
        } else if (currentDirection == 'W') {
            newY -= 1; 
        }
    
        return maze.isValidMove(newX, newY);
    }

    private void turnLeft() {
        if (currentDirection == 'N') {
            currentDirection = 'W';
        } else if (currentDirection == 'W') {
            currentDirection = 'S';
        } else if (currentDirection == 'S') {
            currentDirection = 'E';
        } else if (currentDirection == 'E') {
            currentDirection = 'N';
        }
        
    }

    private void turnRight() {
        if (currentDirection == 'N') {
            currentDirection = 'E';
        } else if (currentDirection == 'E') {
            currentDirection = 'S';
        } else if (currentDirection == 'S') {
            currentDirection = 'W';
        } else if (currentDirection == 'W') {
            currentDirection = 'N';
        }
       
    }
    
    
    

    public boolean moveForward() {
        int newX = x;
        int newY = y;

        if (currentDirection == 'N') {
            newY -= 1;
        } else if (currentDirection == 'E') {
            newX += 1;
        } else if (currentDirection == 'S') {
            newY += 1;
        } else if (currentDirection == 'W') {
            newX -= 1;
        }

        if (maze.isValidMove(newX, newY)) {
            x = newX;
            y = newY;
            
            return true;
        } else {
           
            return false;
        }
    }

    private boolean canMoveForward() {
        int newX = x;
        int newY = y;

        if (currentDirection == 'N') {
            newY -= 1;
        } else if (currentDirection == 'E') {
            newX += 1;
        } else if (currentDirection == 'S') {
            newY += 1;
        } else if (currentDirection == 'W') {
            newX -= 1;
        }

        return maze.isValidMove(newX, newY);
    }
}
