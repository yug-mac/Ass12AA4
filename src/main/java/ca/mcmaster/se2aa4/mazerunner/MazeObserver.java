package ca.mcmaster.se2aa4.mazerunner;

public interface MazeObserver{

    void update(int x, int y, String action, char direction);

}