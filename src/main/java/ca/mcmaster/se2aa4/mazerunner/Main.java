package ca.mcmaster.se2aa4.mazerunner;

import java.io.BufferedReader;
import java.io.FileReader;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("i", "input", true, "Path to the input maze file");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        logger.info("Initialize Maze Runner");

        try {
            cmd = parser.parse(options, args);
            if (!cmd.hasOption("i")) {
                logger.error("Input file not specified. Use -i <file> to specify a maze file.");
                return;
            }

            String inputPath = cmd.getOptionValue("i");
            Maze maze = new Maze(inputPath);
            MazeExplorer explorer = new MazeExplorer(maze);
            Path path = explorer.computePath();

            logger.info("Canonical Path: {}", path.getCanonicalForm());
            logger.info("Factorized Path: {}", path.getFactorizedForm());
        } catch (ParseException e) {
            logger.error("Failed to parse command-line arguments", e);
        } catch (Exception e) {
            logger.error("An error occurred while processing the maze file", e);
        }

        logger.info("End of Maze Runner");
    }
}

public class Maze {
    private final List<String> grid = new ArrayList<>();
    private final Logger logger = LogManager.getLogger(Maze.class);

    public Maze(String filePath) throws IOException {
        logger.info("Loading maze from file: {}", filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                grid.add(line);
            }
        }
        logger.info("Maze loaded successfully.");
    }

    public char getCell(int row, int col) {
        return grid.get(row).charAt(col);
    }

    public int getHeight() {
        return grid.size();
    }

    public int getWidth() {
        return grid.get(0).length();
    }

    public boolean isWall(int row, int col) {
        return getCell(row, col) == '#';
    }

    public boolean isPassage(int row, int col) {
        return getCell(row, col) == ' ';
    }
}

public class MazeExplorer {
    private final Maze maze;
    private final Logger logger = LogManager.getLogger(MazeExplorer.class);
    private int currentRow, currentCol;
    private String direction; // Current direction: "N", "E", "S", "W"

    public MazeExplorer(Maze maze) {
        this.maze = maze;
        this.currentRow = 1; // Assume entry is at (1, 0)
        this.currentCol = 0; // Starting point (entry)
        this.direction = "E"; // Facing East initially
    }

    public Path computePath() {
        logger.info("Starting maze exploration...");
        Path path = new Path();

        // Implementing a simple walking logic (right-hand rule)
        for (int steps = 0; steps < 20; steps++) { // Limit to 20 steps for the MVP
            if (canTurnRight()) {
                turnRight();
                path.addStep('R');
            } else if (canMoveForward()) {
                moveForward();
                path.addStep('F');
            } else {
                turnLeft();
                path.addStep('L');
            }
        }

        logger.info("Maze exploration complete.");
        return path;
    }

    private boolean canTurnRight() {
        int[] next = getNextPositionAfterTurn("R");
        return maze.isPassage(next[0], next[1]);
    }

    private boolean canMoveForward() {
        int[] next = getNextPositionAfterTurn("F");
        return maze.isPassage(next[0], next[1]);
    }

    private void moveForward() {
        int[] next = getNextPositionAfterTurn("F");
        currentRow = next[0];
        currentCol = next[1];
    }

    private void turnRight() {
        switch (direction) {
            case "N": direction = "E"; break;
            case "E": direction = "S"; break;
            case "S": direction = "W"; break;
            case "W": direction = "N"; break;
        }
    }

    private void turnLeft() {
        switch (direction) {
            case "N": direction = "W"; break;
            case "W": direction = "S"; break;
            case "S": direction = "E"; break;
            case "E": direction = "N"; break;
        }
    }

    private int[] getNextPositionAfterTurn(String turn) {
        String newDirection = direction;

        if (turn.equals("R")) {
            switch (direction) {
                case "N": newDirection = "E"; break;
                case "E": newDirection = "S"; break;
                case "S": newDirection = "W"; break;
                case "W": newDirection = "N"; break;
            }
        } else if (turn.equals("L")) {
            switch (direction) {
                case "N": newDirection = "W"; break;
                case "W": newDirection = "S"; break;
                case "S": newDirection = "E"; break;
                case "E": newDirection = "N"; break;
            }
        }

        switch (newDirection) {
            case "N": return new int[]{currentRow - 1, currentCol};
            case "E": return new int[]{currentRow, currentCol + 1};
            case "S": return new int[]{currentRow + 1, currentCol};
            case "W": return new int[]{currentRow, currentCol - 1};
        }

        return new int[]{currentRow, currentCol}; // Default (shouldn't happen)
    }
}

public class Path {
    private final StringBuilder steps = new StringBuilder();

    public void addStep(char step) {
        steps.append(step);
    }

    public String getCanonicalForm() {
        return steps.toString();
    }

    public String getFactorizedForm() {
        StringBuilder factorized = new StringBuilder();
        int count = 1;

        for (int i = 1; i < steps.length(); i++) {
            if (steps.charAt(i) == steps.charAt(i - 1)) {
                count++;
            } else {
                factorized.append(count > 1 ? count : "").append(steps.charAt(i - 1));
                count = 1;
            }
        }

        factorized.append(count > 1 ? count : "").append(steps.charAt(steps.length() - 1));
        return factorized.toString();
    }
}

