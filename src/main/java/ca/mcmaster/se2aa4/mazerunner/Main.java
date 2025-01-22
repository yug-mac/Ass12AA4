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
            logger.info("Reading Maze: {}", inputPath);
            BufferedReader reader = new BufferedReader(new FileReader(inputPath));
            String line;
            while ((line = reader.readLine()) != null) {
                for (int idx = 0; idx < line.length(); idx++) {
                    if (line.charAt(idx) == '#') {
                        logger.trace("WALL ");
                    } else if (line.charAt(idx) == ' ') {
                        logger.trace("PASS ");
                    }
                }
                logger.trace(System.lineSeparator());
            }
        } catch (ParseException e) {
            logger.error("Failed to parse command-line arguments", e);
        } catch (Exception e) {
            logger.error("An error occurred while processing the maze file", e);
        }
        logger.info("**** Computing path");

        logger.warn("PATH NOT COMPUTED");

        logger.info("** End of Maze Runner");
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
