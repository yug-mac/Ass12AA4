package ca.mcmaster.se2aa4.mazerunner;

import java.awt.Point;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MazeExplorer {
    private static final Logger logger = LogManager.getLogger(MazeExplorer.class);
    private final Maze maze;
    private int x, y;
    private char currentDirection;
    private final Point exitPoint;

    public MazeExplorer(Maze maze) {
        this.maze = maze;
        Point[] entryAndExit = maze.getEntryAndExitPoints();
        Point entryPoint = entryAndExit[0];
        this.exitPoint = entryAndExit[1];

        this.x = entryPoint.x;
        this.y = entryPoint.y;

        if (x == 0) { 
            this.currentDirection = 'E'; 
        } else if (x == maze.getWidth() - 1) { 
            this.currentDirection = 'W'; 
        } else if (y == 0) {
            this.currentDirection = 'S'; 
        } else {
            this.currentDirection = 'N'; 
        }
    }

    public Path computePath() {
        Path path = new Path();

        try {
            while (x != exitPoint.x || y != exitPoint.y) {
                if (canMoveRight()) {  
                    turnRight();
                    path.addStep('R');
                    moveForward();
                    path.addStep('F');
                } else if (canMoveForward()) {  
                    moveForward();
                    path.addStep('F');
                } else if (canMoveLeft()) {  
                    turnLeft();
                    path.addStep('L');
                    moveForward();
                    path.addStep('F');
                } else {
                    turnAround();
                    path.addStep('R');
                    path.addStep('R');
                    moveForward();
                    path.addStep('F');
                }
            }
        } catch (Exception e) {
            logger.error("Error during path computation: {}", e.getMessage(), e);
        }

        return path;
    }

    private boolean canMoveRight() {
        int[] newPos = getRightPosition();
        return maze.isValidMove(newPos[0], newPos[1]);
    }

    private boolean canMoveForward() {
        int newX = x, newY = y;

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

    private boolean canMoveLeft() {
        int[] newPos = getLeftPosition();
        return maze.isValidMove(newPos[0], newPos[1]);
    }

    private int[] getRightPosition() {
        int newX = x, newY = y;

        if (currentDirection == 'N') {
            newX += 1;
        } else if (currentDirection == 'E') {
            newY += 1;
        } else if (currentDirection == 'S') {
            newX -= 1;
        } else if (currentDirection == 'W') {
            newY -= 1;
        }

        return new int[]{newX, newY};
    }

    private int[] getLeftPosition() {
        int newX = x, newY = y;

        if (currentDirection == 'N') {
            newX -= 1;
        } else if (currentDirection == 'E') {
            newY -= 1;
        } else if (currentDirection == 'S') {
            newX += 1;
        } else if (currentDirection == 'W') {
            newY += 1;
        }

        return new int[]{newX, newY};
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

    private void turnAround() {
        if (currentDirection == 'N') {
            currentDirection = 'S';
        } else if (currentDirection == 'S') {
            currentDirection = 'N';
        } else if (currentDirection == 'E') {
            currentDirection = 'W';
        } else if (currentDirection == 'W') {
            currentDirection = 'E';
        }
    }

    public void moveForward() {
        if (currentDirection == 'N') {
            y -= 1;
        } else if (currentDirection == 'E') {
            x += 1;
        } else if (currentDirection == 'S') {
            y += 1;
        } else if (currentDirection == 'W') {
            x -= 1;
        }
    }

    public boolean isValidPath(String path) {
        int tempX = x, tempY = y;
        char tempDir = currentDirection;
    
        for (char move : path.toCharArray()) {
            if (move == 'F') {
                int[] nextPos = getNextPosition(tempX, tempY, tempDir);
                if (!maze.isValidMove(nextPos[0], nextPos[1])) return false;
                tempX = nextPos[0];
                tempY = nextPos[1];
            } else if (move == 'R') {
                tempDir = getNewDirection(tempDir, 'R');
            } else if (move == 'L') {
                tempDir = getNewDirection(tempDir, 'L');
            } else {
                return false;
            }
        }
    
        return tempX == exitPoint.x && tempY == exitPoint.y;
    }
    
    private int[] getNextPosition(int currX, int currY, char dir) {
        int newX = currX, newY = currY;
        if (dir == 'N') newY -= 1;
        else if (dir == 'E') newX += 1;
        else if (dir == 'S') newY += 1;
        else if (dir == 'W') newX -= 1;
        return new int[]{newX, newY};
    }
    
    private char getNewDirection(char dir, char turn) {
        if (turn == 'R') {
            if (dir == 'N') return 'E';
            if (dir == 'E') return 'S';
            if (dir == 'S') return 'W';
            return 'N'; 
        } else {
            if (dir == 'N') return 'W';
            if (dir == 'W') return 'S';
            if (dir == 'S') return 'E';
            return 'N'; 
        }
    }
   
}
