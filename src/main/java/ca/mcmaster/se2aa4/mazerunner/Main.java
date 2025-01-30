package ca.mcmaster.se2aa4.mazerunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        System.out.println("Debug: Command-line arguments received:");
        for (String arg : args) {
            System.out.println("  " + arg);
        }

        Options options = new Options();
        options.addOption("i", "input", true, "Path to the input maze file");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            if (!cmd.hasOption("i")) {
                System.out.println("**** Reading the maze from file: <no file specified>");
                System.out.println("/!\\ An error has occurred /!\\");
                System.out.println("PATH NOT COMPUTED");
                System.out.println("** End of Maze Runner");
                return;
            }

            String inputPath = cmd.getOptionValue("i");

            System.out.println("Debug: Input file path received: " + inputPath);

            List<String> mazeGrid = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {
                String line;
                System.out.println("Debug: Reading maze file content...");
                while ((line = reader.readLine()) != null) {
                    mazeGrid.add(line);
                    System.out.println("  " + line); 
                }
            } catch (IOException e) {
                System.out.println("/!\\ An error has occurred /!\\");
                System.out.println("Error: File not found - " + inputPath);
                System.out.println("PATH NOT COMPUTED");
                return;
            }

            if (mazeGrid.isEmpty()) {
                System.out.println("/!\\ An error has occurred /!\\");
                System.out.println("Error: Maze file is empty.");
                System.out.println("PATH NOT COMPUTED");
                return;
            }

            logger.debug("Maze content:");
            for (String row : mazeGrid) {
                logger.debug(row);
            }

            Maze maze = new Maze(mazeGrid);
            maze.printMaze();  
            MazeExplorer explorer = new MazeExplorer(maze);
            Path path = explorer.computePath();
            System.out.println("Canonical Path: " + path.getCanonicalForm());

        } catch (ParseException e) {
            System.out.println("/!\\ An error has occurred /!\\");
            System.out.println("Error: Failed to parse command-line arguments.");
            System.out.println("PATH NOT COMPUTED");
        } catch (IllegalArgumentException e) {
            System.out.println("/!\\ An error has occurred /!\\");
            System.out.println("Error: " + e.getMessage());
            System.out.println("PATH NOT COMPUTED");
        } catch (Exception e) {
            System.out.println("/!\\ An error has occurred /!\\");
            System.out.println("An unexpected error occurred: " + e.getMessage());
            System.out.println("PATH NOT COMPUTED");
        }
    }
}
