package ca.mcmaster.se2aa4.mazerunner;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class MazeExplorer {

    private final Maze maze;
    private final MazeState state;
    private final Point exitPoint;
    private final List<MazeObserver> observers = new ArrayList<>();

    public MazeExplorer(Maze maze) {
        this.maze = maze;
        Point[] entryExit = maze.getEntryAndExitPoints();
        Point entry = entryExit[0];
        this.exitPoint = entryExit[1];
        this.state = new MazeState(entry, 'E');
    }

    public void addObserver(MazeObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(String action) {
        Point p = state.getPosition();
        char dir = state.getDirection();
        for (MazeObserver observer : observers) {
            observer.update(p.x, p.y, action, dir);
        }
    }

    public Path computePath() {
        Path path = new Path();

        while (!state.getPosition().equals(exitPoint)) {
            if (maze.isValidMove(state.peekLeft()[0], state.peekLeft()[1])) {
                state.turnLeft(); path.addStep('L'); notifyObservers("TURN_LEFT");
                state.moveForward(); path.addStep('F'); notifyObservers("MOVE_FORWARD");
            } else if (maze.isValidMove(state.peekForward()[0], state.peekForward()[1])) {
                state.moveForward(); path.addStep('F'); notifyObservers("MOVE_FORWARD");
            } else if (maze.isValidMove(state.peekRight()[0], state.peekRight()[1])) {
                state.turnRight(); path.addStep('R');
                state.moveForward(); path.addStep('F'); notifyObservers("MOVE_FORWARD");
            } else {
                state.turnAround(); path.addStep('R'); path.addStep('R');
                state.moveForward(); path.addStep('F'); notifyObservers("MOVE_FORWARD");
            }
        }

        return path;
    }

    public boolean isValidPath(String factorized) {
        String expanded = expandFactorizedPath(factorized);
        MazeState ghost = new MazeState(state.getPosition(), state.getDirection());

        for (char move : expanded.toCharArray()) {
            switch (move) {
                case 'F' -> {
                    int[] next = ghost.peekForward();
                    if (!maze.isValidMove(next[0], next[1])) return false;
                    ghost.moveForward();
                }
                case 'L' -> ghost.turnLeft();
                case 'R' -> ghost.turnRight();
                default -> { return false; }
            }
        }
        return ghost.getPosition().equals(exitPoint);
    }

    private String expandFactorizedPath(String input) {
        StringBuilder expanded = new StringBuilder();
        int i = 0;

        while (i < input.length()) {
            char c = input.charAt(i);
            if (Character.isDigit(c)) {
                int j = i;
                while (j < input.length() && Character.isDigit(input.charAt(j))) j++;
                int count = Integer.parseInt(input.substring(i, j));
                if (j >= input.length()) return "";
                char move = input.charAt(j);
                expanded.append(String.valueOf(move).repeat(count));
                i = j + 1;
            } else {
                expanded.append(c);
                i++;
            }
        }
        return expanded.toString();
    }
}
