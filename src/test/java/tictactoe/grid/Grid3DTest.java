package tictactoe.grid;

import org.junit.jupiter.api.Test;
import tictactoe.grid.Grid;
import tictactoe.grid.Grid3D;
import tictactoe.grid.exceptions.PositionInvalidException;
import tictactoe.grid.exceptions.PositionUsedException;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class Grid3DTest {
    @Test
    public void positionValid() throws Exception {
        Grid grid = new Grid3D(3);
        assertFalse(grid.place("A1", 'x'));
        assertFalse(grid.place("B2", 'x'));
        assertFalse(grid.place("c4", 'x'));
    }

    @Test
    public void positionAlreadyUsed() throws Exception {
        Grid grid = new Grid3D(3);
        assertFalse(grid.isCellUsed("A5"));
        grid.place("A5", 'x');
        PositionUsedException thrown = assertThrows(
                PositionUsedException.class,
                () -> grid.place("A5", 'x'),
                "The position : A5 is already used"
        );
        assertTrue(grid.isCellUsed("A5"));
    }

    @Test
    public void positionInvalid() throws Exception {
        Grid grid = new Grid3D(3);
        PositionInvalidException thrown = assertThrows(
                PositionInvalidException.class,
                () -> grid.place("0", 'x')
        );

        thrown = assertThrows(
                PositionInvalidException.class,
                () -> grid.place("D0", 'x')
        );

        thrown = assertThrows(
                PositionInvalidException.class,
                () -> grid.place("A10", 'x')
        );

        thrown = assertThrows(
                PositionInvalidException.class,
                () -> grid.place("AA9", 'x')
        );
    }

    @Test
    public void no_winner() throws PositionInvalidException, PositionUsedException {
        Grid grid = new Grid3D(3);
        assertFalse( grid.place("A1",'x'));
        assertFalse( grid.place("A2",'o'));
    }

    @Test
    public void check_win_depth() throws PositionInvalidException, PositionUsedException {
        Grid3D grid = new Grid3D(3);
        assertFalse( grid.place("A1",'o'));
        assertFalse( grid.place("B1",'o'));
        assertTrue( grid.place("C1",'o'));

        assertTrue(grid.getCellStatus(0,0,0));
        assertTrue(grid.getCellStatus(0,0,1));
        assertTrue(grid.getCellStatus(0,0,2));
    }
    @Test
    public void check_win_diagonal() throws PositionInvalidException, PositionUsedException {
        Grid3D grid = new Grid3D(3);
        assertFalse( grid.place("A3",'o'));
        assertFalse( grid.place("B5",'o'));
        assertTrue( grid.place("C7",'o'));

        assertTrue( grid.getCellStatus(2,0,0));
        assertTrue( grid.getCellStatus(1,1,1));
        assertTrue( grid.getCellStatus(0,2,2));
    }

    @Test
    public void check_win_diagonal2() throws PositionInvalidException, PositionUsedException {
        Grid3D grid = new Grid3D(3);
        assertFalse( grid.place("A7",'o'));
        assertFalse( grid.place("B5",'o'));
        assertTrue( grid.place("C3",'o'));

        assertTrue( grid.getCellStatus(0,2,0));
        assertTrue( grid.getCellStatus(1,1,1));
        assertTrue( grid.getCellStatus(2,0,2));

    }

    @Test
    public void check_win_diagonal3() throws NoSuchFieldException, IllegalAccessException, PositionInvalidException, PositionUsedException {
        Grid3D grid = new Grid3D(3);
        assertFalse( grid.place("A1",'o'));
        assertFalse( grid.place("B5",'o'));
        assertTrue( grid.place("C9",'o'));

        assertTrue( grid.getCellStatus(0,0,0));
        assertTrue( grid.getCellStatus(1,1,1));
        assertTrue( grid.getCellStatus(2,2,2));
    }

    @Test
    public void check_win_diagonal4() throws PositionInvalidException, PositionUsedException {
        Grid3D grid = new Grid3D(3);
        assertFalse( grid.place("A9",'o'));
        assertFalse( grid.place("B5",'o'));
        assertTrue( grid.place("C1",'o'));

        assertTrue( grid.getCellStatus(2,2,0));
        assertTrue( grid.getCellStatus(1,1,1));
        assertTrue( grid.getCellStatus(0,0,2));

    }
}
