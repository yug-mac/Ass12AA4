package ca.mcmaster.se2aa4.mazerunner;

import java.util.List;
    
    public class BasicMazeFactory implements MazeFactory {
        @Override
        public Maze createMaze(List<String> grid) {
            return new Maze(grid);
        }
    }

