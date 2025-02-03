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

/**Name: Yug vashisth
 * MacID: vashisty, 400501750
 * Maze runner main class.
 * Parses command-line arguments, reads a maze file, and computes or validates paths.
 * FINAL
 */
public class Main {
    
    /** Logger instance for debugging purposes*/
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        
        // Define command-line options
        Options options = new Options();
        options.addOption("i", "input", true, "Path to the input maze file");
        options.addOption("p", "path", true, "Path string to validate");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try {
            // Parse command-line arguments
            cmd = parser.parse(options, args);
            if (!cmd.hasOption("i")) {
                System.out.println("/!\\ An error has occurred /!\\");
                System.out.println("Error: No maze file specified.");
                System.out.println("PATH NOT COMPUTED");
                return;
            }

            // Retrieve input file path from command-line arguments
            String inputPath = cmd.getOptionValue("i");

            // Read the maze file into a list of strings
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

            // Check if the maze file is empty
            if (mazeGrid.isEmpty()) {
                System.out.println("/!\\ An error has occurred /!\\");
                System.out.println("Error: Maze file is empty.");
                System.out.println("PATH NOT COMPUTED");
                return;
            }

            // Create a Maze object and a MazeExplorer instance
            Maze maze = new Maze(mazeGrid);
            MazeExplorer explorer = new MazeExplorer(maze);

            // Check if the user provided a path to validate
            if (cmd.hasOption("p")) {
                String providedPath = cmd.getOptionValue("p").trim();
                
                boolean isValid = explorer.isValidPath(providedPath);
                
                if (isValid) {
                    System.out.println("Correct path.");
                } else {
                    System.out.println("Incorrect path.");
                }
                return;
            }

            // Compute and display the canonical and factorized path
            Path path = explorer.computePath();
            System.out.println("Canonical Path: " + path.getCanonicalForm());
            System.out.println("Factorized Path: " + path.getFactorized());

        } catch (ParseException e) {
            // Handle command-line argument parsing errors
            System.out.println("/!\\ An error has occurred /!\\");
            System.out.println("Error: Failed to parse command-line arguments.");
            System.out.println("PATH NOT COMPUTED");
        } catch (IllegalArgumentException e) {
            // Handle invalid arguments
            System.out.println("/!\\ An error has occurred /!\\");
            System.out.println("Error: " + e.getMessage());
            System.out.println("PATH NOT COMPUTED");
        } catch (Exception e) {
            // Catch-all for unexpected errors
            System.out.println("/!\\ An error has occurred /!\\");
            System.out.println("An unexpected error occurred: " + e.getMessage());
            System.out.println("PATH NOT COMPUTED");
        }
    }
}
