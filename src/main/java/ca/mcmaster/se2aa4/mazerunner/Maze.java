package ca.mcmaster.se2aa4.mazerunner;

import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Maze{
    private final List<String> grid = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger(Maze.class);

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

