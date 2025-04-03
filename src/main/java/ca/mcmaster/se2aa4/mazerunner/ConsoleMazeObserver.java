package ca.mcmaster.se2aa4.mazerunner;

public class ConsoleMazeObserver implements MazeObserver {
    @Override
    public void update(int x, int y, String action, char direction) {
        System.out.printf("[Observer] %s at (%d, %d), facing %c%n", action, x, y, direction);
    }
}
