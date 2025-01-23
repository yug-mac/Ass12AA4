package ca.mcmaster.se2aa4.mazerunner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MazeExplorer {
    private static final Logger logger = LogManager.getLogger(MazeExplorer.class);
    private final Maze maze;

    public MazeExplorer(Maze maze) {
        this.maze = maze;
        logger.info("MazeExplorer initialized.");
    }

    public Path computePath() {
        logger.info("Compute path functionality not implemented yet.");
        return new Path();
    }
}
