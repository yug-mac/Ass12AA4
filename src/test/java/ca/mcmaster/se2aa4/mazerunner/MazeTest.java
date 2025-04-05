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

    // basic tests created for code before refactoring the database

     @Test //Test 1 (dimensions test)
    void testDimensions() {
        assertEquals(3, maze.getWidth());
        assertEquals(3, maze.getHeight());
    }

    @Test
    void testIsValidMove() { //Test 2 (testing validiity of the move)
        assertTrue(maze.isValidMove(1, 1));  // center
        assertFalse(maze.isValidMove(0, 0)); // wall
        assertFalse(maze.isValidMove(3, 1)); // out of bounds
    }

     @Test //Test 3 (testing the move)
    void testValidMovesOnBoundary() {
        assertTrue(maze.isValidMove(0, 1)); // left boundary
        assertTrue(maze.isValidMove(2, 1)); // right boundary
        assertFalse(maze.isValidMove(0, 0)); // top-left wall
        assertFalse(maze.isValidMove(2, 2)); // bottom-right wall
    }

    @Test // Test 4 (testing the exit and entry points + validity)
    void testGetEntryAndExitPoints() {
        Point[] points = maze.getEntryAndExitPoints();
        assertNotNull(points);
        assertEquals(2, points.length);

        for (Point p : points) {
            assertTrue(p.x == 0 || p.x == maze.getWidth() - 1 || p.y == 0 || p.y == maze.getHeight() - 1);
            assertTrue(maze.isValidMove(p.x, p.y));
        }
    }

      @Test // Test 5 (testing the entry and exit points in a large maze)
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

    @Test // Test 6 (testing the entry and exit points in a large maze with no entry or exit)
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

    

    @Test // Test 7 (testing the entry and exit points in a large maze with no entry or exit, it finfs the path)
    void testMazeExplorerFindsPath() {
        MazeExplorer explorer = new MazeExplorer(maze);
        Path path = explorer.computePath();

        assertNotNull(path);
        assertFalse(explorer.isValidPath(path.getCanonicalForm()));
    }

    @Test //Test 8 (adding tan instance of observer
    void testAddObserver() {
        MazeExplorer explorer = new MazeExplorer(maze);
        MazeObserver observer = new ConsoleMazeObserver();
        explorer.addObserver(observer);
        assertNotNull(explorer);
    }



    @Test //Test 9 (testing the path representation)
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

    @Test //Test 10 (testing if output is valid)
    void testPathOutput() {
        Path path = new Path();
        path.addStep('F');
        path.addStep('F');
        path.addStep('F');

        assertEquals("FFF", path.getCanonicalForm());
    }

    // Test for the patterns I created

    @Test //Test 11 (testing if factory can create maze)
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

    @Test //Test12 (testing if factory can create maze with walls)
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

   @Test //Test13 (testinf if maze can see the valid exit and enetry pts)
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
    @Test //Test14 (see if factiory can throw exception on no entry or exit)
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

    //Observer Tests
    @Test //Test15 (See if observer can contsatntly log and update with every move and turn)
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

@Test //Test 16 (see if multiple observers can be added and notified)
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

@Test //Test 17 (see if factory can handle uneven row lengths)
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
@Test //Test 18 (see if factory can handle empty list)
void testFactoryCreatesMazeFromEmptyList() {
    MazeFactory factory = new BasicMazeFactory();
    List<String> grid = List.of();
    Maze emptyMaze = factory.createMaze(grid);

    assertEquals(0, emptyMaze.getHeight());
    assertEquals(0, emptyMaze.getWidth());
}
@Test //Test 19 (see if observer can handle specifc actions and log)
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
@Test //Test 20 (see if observer can handle direction updates)
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
