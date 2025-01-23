package ca.mcmaster.se2aa4.mazerunner;

import java.io.FileNotFoundException;

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

            Maze maze = new Maze(inputPath);
            MazeExplorer explorer = new MazeExplorer(maze);
            Path path = explorer.computePath();
            System.out.println("Canonical Path: " + path.getCanonicalForm());
        } catch (FileNotFoundException e) {
            System.out.println("/!\\ An error has occurred /!\\");
            System.out.println("Error: File not found - ");
            System.out.println("PATH NOT COMPUTED");
        } catch (ParseException e) {
            System.out.println("/!\\ An error has occurred /!\\");
            System.out.println("Error: Failed to parse command-line arguments.");
            System.out.println("PATH NOT COMPUTED");
        } catch (Exception e) {
            System.out.println("/!\\ An error has occurred /!\\");
            System.out.println("An unexpected error occurred: ");
            System.out.println("PATH NOT COMPUTED");
        }
    }
}
