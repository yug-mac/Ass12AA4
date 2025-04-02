package ca.mcmaster.se2aa4.mazerunner;

import java.util.List;

public interface MazeFactory {
    Maze createMaze(List<String> grid);
}
