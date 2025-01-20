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
                        System.out.print("WALL ");
                    } else if (line.charAt(idx) == ' ') {
                        System.out.print("PASS ");
                    }
                }
                System.out.print(System.lineSeparator());
            }
        } catch (ParseException e) {
            logger.error("Failed to parse command-line arguments", e);
        } catch (Exception e) {
            logger.error("An error occurred while processing the maze file", e);
        }
        logger.info("**** Computing path");

        logger.info("PATH NOT COMPUTED");

        logger.info("** End of Maze Runner");
    }
}
