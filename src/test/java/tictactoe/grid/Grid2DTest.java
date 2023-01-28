package tictactoe.grid;

import org.junit.jupiter.api.Test;
import tictactoe.grid.Grid;
import tictactoe.grid.Grid2D;
import tictactoe.grid.exceptions.PositionInvalidException;
import tictactoe.grid.exceptions.PositionUsedException;

import static org.junit.jupiter.api.Assertions.*;


public class Grid2DTest {

    @Test
    public void positionValid() throws Exception {
        Grid grid = new Grid2D(3);
        assertFalse(grid.place("1", 'x'));
        assertFalse(grid.place("5", 'x'));
    }

    @Test
    public void positionAlreadyUsed() throws Exception {
        Grid grid = new Grid2D(3);
        grid.place("5", 'x');
        PositionUsedException thrown = assertThrows(
                PositionUsedException.class,
                () -> grid.place("5", 'x')
        );
        assertTrue(thrown.getMessage().contentEquals("The position : 5 is already used"));
    }

    @Test
    public void positionInvalid() throws Exception {
        Grid grid = new Grid2D(3);
        PositionInvalidException thrown = assertThrows(
                PositionInvalidException.class,
                () -> grid.place("0", 'x')
        );
        assertTrue(thrown.getMessage().contentEquals("The position : 0 is not valid"));
    }

    @Test
    public void no_winner() throws Exception {
        Grid grid = new Grid2D(3);
        assertFalse( grid.place("1",'x'));
        assertFalse( grid.place("2",'x'));
        assertFalse( grid.place("6",'x'));
        assertFalse( grid.place("8",'x'));
        assertFalse( grid.place("3",'o'));
        assertFalse( grid.place("4",'o'));
        assertFalse( grid.place("5",'o'));
        assertFalse( grid.place("7",'o'));
        assertFalse( grid.place("9",'o'));
    }

    @Test
    public void x_winner_line_1() throws Exception {
        Grid grid = new Grid2D(3);
        assertFalse( grid.place("1",'x'));
        assertFalse( grid.place("2",'x'));
        assertTrue( grid.place("3",'x'));

        assertTrue(((Grid2D)grid).getCellStatus(0,0));
        assertTrue(((Grid2D)grid).getCellStatus(1,0));
        assertTrue(((Grid2D)grid).getCellStatus(2,0));
    }

    @Test
    public void x_winner_line_2() throws Exception {
        Grid grid = new Grid2D(3);
        assertFalse( grid.place("4",'x'));
        assertFalse( grid.place("5",'x'));
        assertTrue( grid.place("6",'x'));

        assertTrue(((Grid2D)grid).getCellStatus(0,1));
        assertTrue(((Grid2D)grid).getCellStatus(1,1));
        assertTrue(((Grid2D)grid).getCellStatus(2,1));
    }

    @Test
    public void x_winner_line_3() throws Exception {
        Grid grid = new Grid2D(3);
        assertFalse( grid.place("7",'x'));
        assertFalse( grid.place("8",'x'));
        assertTrue( grid.place("9",'x'));

        assertTrue(((Grid2D)grid).getCellStatus(0,2));
        assertTrue(((Grid2D)grid).getCellStatus(1,2));
        assertTrue(((Grid2D)grid).getCellStatus(2,2));
    }

    @Test
    public void x_winner_column_1() throws Exception {
        Grid grid = new Grid2D(3);
        assertFalse( grid.place("1",'x'));
        assertFalse( grid.place("4",'x'));
        assertTrue( grid.place("7",'x'));

        assertTrue(((Grid2D)grid).getCellStatus(0,0));
        assertTrue(((Grid2D)grid).getCellStatus(0,1));
        assertTrue(((Grid2D)grid).getCellStatus(0,2));
    }

    @Test
    public void x_winner_column_2() throws Exception {
        Grid grid = new Grid2D(3);
        assertFalse( grid.place("2",'x'));
        assertFalse( grid.place("5",'x'));
        assertTrue( grid.place("8",'x'));

        assertTrue(((Grid2D)grid).getCellStatus(1,0));
        assertTrue(((Grid2D)grid).getCellStatus(1,1));
        assertTrue(((Grid2D)grid).getCellStatus(1,2));
    }

    @Test
    public void x_winner_column_3() throws Exception {
        Grid grid = new Grid2D(3);
        assertFalse( grid.place("3",'x'));
        assertFalse( grid.place("6",'x'));
        assertTrue( grid.place("9",'x'));

        assertTrue(((Grid2D)grid).getCellStatus(2,0));
        assertTrue(((Grid2D)grid).getCellStatus(2,1));
        assertTrue(((Grid2D)grid).getCellStatus(2,2));
    }

    @Test
    public void check_multi_win() throws Exception {
        Grid grid = new Grid2D(3);
        assertFalse( grid.place("2",'x'));
        assertFalse( grid.place("3",'x'));
        assertFalse( grid.place("4",'x'));
        assertFalse( grid.place("7",'x'));
        assertTrue( grid.place("1",'x'));

        assertTrue(((Grid2D)grid).getCellStatus(0,0));
        assertTrue(((Grid2D)grid).getCellStatus(1,0));
        assertTrue(((Grid2D)grid).getCellStatus(2,0));
        assertTrue(((Grid2D)grid).getCellStatus(0,1));
        assertTrue(((Grid2D)grid).getCellStatus(0,2));
    }
}