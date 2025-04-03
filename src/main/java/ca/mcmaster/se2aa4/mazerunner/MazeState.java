package ca.mcmaster.se2aa4.mazerunner;

import java.awt.Point;

public class MazeState {
    private Point position;
    private char direction;

    public MazeState(Point start, char startDirection) {
        this.position = new Point(start);
        this.direction = startDirection;
    }

    public Point getPosition() {
        return new Point(position);
    }

    public char getDirection() {
        return direction;
    }

    public void moveForward() {
        if (direction == 'N') position.y--;
        else if (direction == 'E') position.x++;
        else if (direction == 'S') position.y++;
        else if (direction == 'W') position.x--;
    }

    public void turnLeft() {
        direction = switch (direction) {
            case 'N' -> 'W';
            case 'W' -> 'S';
            case 'S' -> 'E';
            default -> 'N'; // 'E'
        };
    }

    public void turnRight() {
        direction = switch (direction) {
            case 'N' -> 'E';
            case 'E' -> 'S';
            case 'S' -> 'W';
            default -> 'N'; // 'W'
        };
    }

    public void turnAround() {
        direction = switch (direction) {
            case 'N' -> 'S';
            case 'S' -> 'N';
            case 'E' -> 'W';
            default -> 'E'; // 'W'
        };
    }

    public int[] peekForward() {
        int x = position.x, y = position.y;
        return switch (direction) {
            case 'N' -> new int[]{x, y - 1};
            case 'E' -> new int[]{x + 1, y};
            case 'S' -> new int[]{x, y + 1};
            case 'W' -> new int[]{x - 1, y};
            default -> new int[]{x, y};
        };
    }

    public int[] peekLeft() {
        turnLeft();
        int[] pos = peekForward();
        turnRight(); // reset
        return pos;
    }

    public int[] peekRight() {
        turnRight();
        int[] pos = peekForward();
        turnLeft(); // reset
        return pos;
    }
}
