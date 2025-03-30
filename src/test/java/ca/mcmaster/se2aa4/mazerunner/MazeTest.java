package ca.mcmaster.se2aa4.mazerunner;

import java.awt.Point;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    void testGetEntryAndExitPoints() {
        Point[] points = maze.getEntryAndExitPoints();
        assertNotNull(points);
        assertEquals(2, points.length);

        // Check they are on the boundary and open paths
        for (Point p : points) {
            assertTrue(p.x == 0 || p.x == maze.getWidth() - 1 || p.y == 0 || p.y == maze.getHeight() - 1);
            assertTrue(maze.isValidMove(p.x, p.y));
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

@Test
void testMazeExplorerFindsPath() {
    MazeExplorer explorer = new MazeExplorer(maze);
    Path path = explorer.computePath();

    assertNotNull(path);
    assertFalse(explorer.isValidPath(path.getCanonicalForm()));
}
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

}
