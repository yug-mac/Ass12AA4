package ca.mcmaster.se2aa4.mazerunner;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Name: Yug Vashisth, MacID: vashisty, Student number: 400501750
 * The MazeExplorer class is responsible for navigating through a given maze.
 * It computes a path from the entry point to the exit point while handling movement
 * in different directions (left, right, forward, and turnaround).
 */
public class MazeExplorer {
    
    // Logger instance for debugging
    private static final Logger logger = LogManager.getLogger(MazeExplorer.class);
    
    private final Maze maze;
    
    /** Current position (x, y) within the maze. */
    private int x, y;
    
    /** Current direction the explorer is facing (N, E, S, W). */
    private char currentDirection;
    
    /** The exit point of the maze. */
    private final Point exitPoint;

    /**
     * Constructs a MazeExplorer instance with a given maze.
     * Initializes starting position and direction.
     */
    public MazeExplorer(Maze maze) {
        this.maze = maze;
        Point[] entryAndExit = maze.getEntryAndExitPoints();
        Point entryPoint = entryAndExit[0];
        this.exitPoint = entryAndExit[1];

        this.x = entryPoint.x;
        this.y = entryPoint.y;
        this.currentDirection = 'E'; //current direwction starts off at eats
    }
    private final List<MazeObserver> observers = new ArrayList<>();

public void addObserver(MazeObserver observer) {
    observers.add(observer);
}

private void notifyObservers(String action) {
    for (MazeObserver observer : observers) {
        observer.update(x, y, action, currentDirection);
    }
}

    /**
     * Computes a valid path from the entry point to the exit point.
     */
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

    /**
     * Checks if the explorer can move right.
     */
    private boolean canMoveRight() {
        int[] newPos = getRightPosition();
        return maze.isValidMove(newPos[0], newPos[1]);
    }

    /**
     * Checks if the explorer can move forward.
     */
    private boolean canMoveForward() {
        int newX = x, newY = y;
        
        if (currentDirection == 'N') newY -= 1;
        else if (currentDirection == 'E') newX += 1;
        else if (currentDirection == 'S') newY += 1;
        else if (currentDirection == 'W') newX -= 1;
        
        return maze.isValidMove(newX, newY);
    }

    /**
     * Checks if the explorer can move left.
     */
    private boolean canMoveLeft() {
        int[] newPos = getLeftPosition();
        return maze.isValidMove(newPos[0], newPos[1]);
    }

    /**
     * Determines the new position if moving right.
     */
    private int[] getRightPosition() {
        int newX = x, newY = y;

        if (currentDirection == 'N') newX += 1;
        else if (currentDirection == 'E') newY += 1;
        else if (currentDirection == 'S') newX -= 1;
        else if (currentDirection == 'W') newY -= 1;
        
        return new int[]{newX, newY};
    }

    /**
     * Determines the new position if moving left.
     */
    private int[] getLeftPosition() {
        int newX = x, newY = y;
        
        if (currentDirection == 'N') newX -= 1;
        else if (currentDirection == 'E') newY -= 1;
        else if (currentDirection == 'S') newX += 1;
        else if (currentDirection == 'W') newY += 1;
        
        return new int[]{newX, newY};
    }

    /**
     * Turns the explorer left
     */
    private void turnLeft() {
        currentDirection = getNewDirection(currentDirection, 'L');
        notifyObservers("TURN_LEFT");
    }

    /**
     * Turns the explorer right
     */
    private void turnRight() {
        currentDirection = getNewDirection(currentDirection, 'R');
    }

    /**
     * Turns the explorer around (180 degrees).
     */
    private void turnAround() {
        if (currentDirection == 'N') currentDirection = 'S';
        else if (currentDirection == 'S') currentDirection = 'N';
        else if (currentDirection == 'E') currentDirection = 'W';
        else if (currentDirection == 'W') currentDirection = 'E';
    }

    /**
     * Moves the explorer forward in the current direction.
     */
    public void moveForward() {
        if (currentDirection == 'N') y -= 1;
        else if (currentDirection == 'E') x += 1;
        else if (currentDirection == 'S') y += 1;
        else if (currentDirection == 'W') x -= 1;
    
        notifyObservers("MOVE_FORWARD");
    }

    /**
     * Checks if the path is valid or invalid based off of the path string
     */
    public boolean isValidPath(String path) {
        String expandedPath = expandFactorizedPath(path);
        int tempX = x, tempY = y;
        char tempDir = currentDirection;
    
        for (char move : expandedPath.toCharArray()) {
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
    
    private String expandFactorizedPath(String factorizedPath) {
        StringBuilder expanded = new StringBuilder();
        int i = 0;
    
        while (i < factorizedPath.length()) {
            char c = factorizedPath.charAt(i);
    
            // If it's a digit, parse full number (in case of 12F etc.)
            if (Character.isDigit(c)) {
                int j = i;
                while (j < factorizedPath.length() && Character.isDigit(factorizedPath.charAt(j))) {
                    j++;
                }
                int count = Integer.parseInt(factorizedPath.substring(i, j));
                if (j >= factorizedPath.length()) return ""; // malformed, e.g. "4"
                char move = factorizedPath.charAt(j);
                expanded.append(String.valueOf(move).repeat(count));
                i = j + 1;
            } else {
                expanded.append(c);
                i++;
            }
        }
    
        return expanded.toString();
    }
    
    /**
     * gets the nextposotiion of the movement, has three parameters, the current x and y position and the direction
     */
    private int[] getNextPosition(int currX, int currY, char dir) {
        int newX = currX, newY = currY;
        if (dir == 'N') newY -= 1;
        else if (dir == 'E') newX += 1;
        else if (dir == 'S') newY += 1;
        else if (dir == 'W') newX -= 1;
        return new int[]{newX, newY};
    }
    
    /**
     * Gets the the new direction in which it is goign to have to go... takes two parameters.
     */
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