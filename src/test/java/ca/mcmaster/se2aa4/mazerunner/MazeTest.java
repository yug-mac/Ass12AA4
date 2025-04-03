package ca.mcmaster.se2aa4.mazerunner;

import java.awt.Point;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MazeTest {

    private Maze maze;

    @BeforeEach
    void setUp() {
        List<String> grid = List.of(
            "# #",
            "   ",
            "# #"
        );
        maze = new Maze(grid);
    }

    // ------------------ Maze Core Functionality Tests ------------------

    @Test
    void testDimensions() {
        assertEquals(3, maze.getWidth());
        assertEquals(3, maze.getHeight());
    }

    @Test
    void testIsValidMove() {
        assertTrue(maze.isValidMove(1, 1));  // center
        assertFalse(maze.isValidMove(0, 0)); // wall
        assertFalse(maze.isValidMove(3, 1)); // out of bounds
    }

    @Test
    void testValidMovesOnBoundary() {
        assertTrue(maze.isValidMove(0, 1)); // left boundary
        assertTrue(maze.isValidMove(2, 1)); // right boundary
        assertFalse(maze.isValidMove(0, 0)); // top-left wall
        assertFalse(maze.isValidMove(2, 2)); // bottom-right wall
    }

    @Test
    void testGetEntryAndExitPoints() {
        Point[] points = maze.getEntryAndExitPoints();
        assertNotNull(points);
        assertEquals(2, points.length);

        for (Point p : points) {
            assertTrue(p.x == 0 || p.x == maze.getWidth() - 1 || p.y == 0 || p.y == maze.getHeight() - 1);
            assertTrue(maze.isValidMove(p.x, p.y));
        }
    }

    @Test
    void testGetEntryAndExitPointsLargeMaze() {
        List<String> grid = List.of(
            "# # # #",
            "       ",
            "# # # #"
        );
        Maze largeMaze = new Maze(grid);
        Point[] points = largeMaze.getEntryAndExitPoints();
        assertNotNull(points);
        assertEquals(2, points.length);

        for (Point p : points) {
            assertTrue(p.x == 0 || p.x == largeMaze.getWidth() - 1 || p.y == 0 || p.y == largeMaze.getHeight() - 1);
            assertTrue(largeMaze.isValidMove(p.x, p.y));
        }
    }

    @Test
    void testMissingEntryOrExitThrows() {
        List<String> badGrid = List.of(
            "###",
            "###",
            "###"
        );
        Maze badMaze = new Maze(badGrid);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            badMaze.getEntryAndExitPoints();
        });

        assertEquals("Maze must have both an entry and an exit.", exception.getMessage());
    }

    // ------------------ MazeExplorer Tests ------------------

    @Test
    void testMazeExplorerFindsPath() {
        MazeExplorer explorer = new MazeExplorer(maze);
        Path path = explorer.computePath();

        assertNotNull(path);
        assertFalse(explorer.isValidPath(path.getCanonicalForm()));
    }

    @Test
    void testAddObserver() {
        MazeExplorer explorer = new MazeExplorer(maze);
        MazeObserver observer = new ConsoleMazeObserver();
        explorer.addObserver(observer);
        assertNotNull(explorer);
    }

    // ------------------ Path Tests ------------------

    @Test
    void testPathRepresentations() {
        Path path = new Path();
        path.addStep('R');
        path.addStep('R');
        path.addStep('F');
        path.addStep('F');
        path.addStep('F');

        assertEquals("RRFFF", path.getCanonicalForm());
        assertEquals("2R3F", path.getFactorized());
    }

    @Test
    void testPathOutput() {
        Path path = new Path();
        path.addStep('F');
        path.addStep('F');
        path.addStep('F');

        assertEquals("FFF", path.getCanonicalForm());
    }

    // ------------------ Factory Pattern Tests ------------------

    @Test
    void testFactoryCreatesMaze() {
        MazeFactory factory = new BasicMazeFactory();
        List<String> grid = List.of(
            "#####",
            "#   #",
            "#####"
        );
        Maze createdMaze = factory.createMaze(grid);

        assertNotNull(createdMaze);
        assertEquals(Maze.class, createdMaze.getClass());
    }

    @Test
    void testFactoryMazeDimensions() {
        MazeFactory factory = new BasicMazeFactory();
        List<String> grid = List.of(
            "#######",
            "#     #",
            "#######"
        );
        Maze createdMaze = factory.createMaze(grid);
        assertEquals(7, createdMaze.getWidth());
        assertEquals(3, createdMaze.getHeight());
    }

    @Test
    void testFactoryMazeWithValidEntryAndExit() {
        MazeFactory factory = new BasicMazeFactory();
        List<String> grid = List.of(
            "#######",
            "      #",
            "#######"
        );
        Maze createdMaze = factory.createMaze(grid);
        assertDoesNotThrow(createdMaze::getEntryAndExitPoints);
    }

    @Test
    void testFactoryThrowsOnNoEntryOrExit() {
        MazeFactory factory = new BasicMazeFactory();
        List<String> grid = List.of(
            "#####",
            "#####",
            "#####"
        );
        Maze createdMaze = factory.createMaze(grid);

        Exception exception = assertThrows(IllegalArgumentException.class, createdMaze::getEntryAndExitPoints);
        assertEquals("Maze must have both an entry and an exit.", exception.getMessage());
    }
}
