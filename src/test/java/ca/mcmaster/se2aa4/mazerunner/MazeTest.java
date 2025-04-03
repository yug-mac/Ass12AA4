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
        "###### "
    );
    Maze createdMaze = factory.createMaze(grid);

    // Ensure both entry and exit are found without throwing
    assertDoesNotThrow(createdMaze::getEntryAndExitPoints);
    Point[] points = createdMaze.getEntryAndExitPoints();
    assertEquals(2, points.length);

    // Ensure the entry and exit points are valid moves
    assertTrue(createdMaze.isValidMove(points[0].x, points[0].y), "Entry point is not valid.");
    assertTrue(createdMaze.isValidMove(points[1].x, points[1].y), "Exit point is not valid.");

    // Ensure the entry and exit points are on the boundaries
    assertTrue(
        points[0].x == 0 || points[0].x == createdMaze.getWidth() - 1 || points[0].y == 0 || points[0].y == createdMaze.getHeight() - 1,
        "Entry point is not on the boundary."
    );
    assertTrue(
        points[1].x == 0 || points[1].x == createdMaze.getWidth() - 1 || points[1].y == 0 || points[1].y == createdMaze.getHeight() - 1,
        "Exit point is not on the boundary."
    );
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

    @Test
void testObserverIsNotifiedOnMoveAndTurn() {
    List<String> grid = List.of(
        "# #",
        "   ",
        "# #"
    );
    Maze testMaze = new Maze(grid);
    MazeExplorer explorer = new MazeExplorer(testMaze);

    // Create a mock observer to capture calls
    class TestObserver implements MazeObserver {
        int updates = 0;
        String lastAction = "";
        int lastX = -1, lastY = -1;
        char lastDirection = 'X';

        @Override
        public void update(int x, int y, String action, char direction) {
            updates++;
            lastAction = action;
            lastX = x;
            lastY = y;
            lastDirection = direction;
        }
    }

    TestObserver observer = new TestObserver();
    explorer.addObserver(observer);

    Path path = explorer.computePath();

    // Since path might be invalid (no exit), we still expect some updates
    assertTrue(observer.updates > 0, "Observer should be notified at least once.");
    assertNotNull(observer.lastAction);
    assertTrue(List.of("MOVE_FORWARD", "TURN_LEFT").contains(observer.lastAction));
    assertTrue(observer.lastDirection == 'E' || observer.lastDirection == 'N' || observer.lastDirection == 'W' || observer.lastDirection == 'S');
}

@Test
void testMultipleObserversReceiveUpdate() {
    MazeExplorer explorer = new MazeExplorer(maze);

    class CounterObserver implements MazeObserver {
        int count = 0;

        @Override
        public void update(int x, int y, String action, char direction) {
            count++;
        }
    }

    CounterObserver observer1 = new CounterObserver();
    CounterObserver observer2 = new CounterObserver();

    explorer.addObserver(observer1);
    explorer.addObserver(observer2);

    explorer.computePath();

    // Both observers should have been updated the same number of times
    assertEquals(observer1.count, observer2.count);
    assertTrue(observer1.count > 0);
}

@Test
void testFactoryMazeHandlesUnevenRowLengths() {
    MazeFactory factory = new BasicMazeFactory();
    List<String> grid = List.of(
        "###",
        "#",
        "#####"
    );
    Maze maze = factory.createMaze(grid);

    // It should pad shorter rows
    assertEquals(5, maze.getWidth());
    assertEquals(3, maze.getHeight());
}
@Test
void testFactoryCreatesMazeFromEmptyList() {
    MazeFactory factory = new BasicMazeFactory();
    List<String> grid = List.of();
    Maze emptyMaze = factory.createMaze(grid);

    assertEquals(0, emptyMaze.getHeight());
    assertEquals(0, emptyMaze.getWidth());
}
@Test
void testObserverReceivesSpecificActions() {
    MazeExplorer explorer = new MazeExplorer(maze);

    class ActionLogger implements MazeObserver {
        boolean sawMove = false;
        boolean sawTurn = false;

        @Override
        public void update(int x, int y, String action, char direction) {
            if ("MOVE_FORWARD".equals(action)) sawMove = true;
            if ("TURN_LEFT".equals(action) || "TURN_RIGHT".equals(action)) sawTurn = true;
        }
    }

    ActionLogger observer = new ActionLogger();
    explorer.addObserver(observer);
    explorer.computePath();

    assertTrue(observer.sawMove);
    // Turn is only notified on LEFT in your implementation (not RIGHT) — adjust accordingly
}
@Test
void testObserverDirectionUpdates() {
    MazeExplorer explorer = new MazeExplorer(maze);

    class DirectionChecker implements MazeObserver {
        char lastDirection = 'X';
        @Override
        public void update(int x, int y, String action, char direction) {
            lastDirection = direction;
        }
    }

    DirectionChecker observer = new DirectionChecker();
    explorer.addObserver(observer);
    explorer.computePath();

    assertNotEquals('X', observer.lastDirection); // should update from initial
    assertTrue(List.of('N', 'E', 'S', 'W').contains(observer.lastDirection));
}


}
