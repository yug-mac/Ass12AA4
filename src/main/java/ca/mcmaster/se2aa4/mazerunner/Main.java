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

        Options options = new Options();
        options.addOption("i", "input", true, "Path to the input maze file");
        options.addOption("p", "path", true, "Path string to validate");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            if (!cmd.hasOption("i")) {
                System.out.println("/!\\ An error has occurred /!\\");
                System.out.println("Error: No maze file specified.");
                System.out.println("PATH NOT COMPUTED");
                return;
            }

            String inputPath = cmd.getOptionValue("i");
            System.out.println("Debug: Input file path received: " + inputPath);


            List<String> mazeGrid = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    mazeGrid.add(line);
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

            Maze maze = new Maze(mazeGrid);
            MazeExplorer explorer = new MazeExplorer(maze);

 
            if (cmd.hasOption("p")) {
                String providedPath = cmd.getOptionValue("p").trim();
                System.out.println("Debug: Checking provided path: " + providedPath);
                
                boolean isValid = explorer.isValidPath(providedPath);
                
                if (isValid) {
                    System.out.println("Valid path.");
                } else {
                    System.out.println("Invalid path.");
                }
                return;
            }


            Path path = explorer.computePath();
            System.out.println("Canonical Path: " + path.getCanonicalForm());
            System.out.println("Factorized Path: " + path.getFactorized());

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
