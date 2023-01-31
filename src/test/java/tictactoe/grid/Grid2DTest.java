package tictactoe.grid;

import org.junit.jupiter.api.Test;
import tictactoe.grid.Grid;
import tictactoe.grid.Grid2D;
import tictactoe.grid.exceptions.PositionInvalidException;
import tictactoe.grid.exceptions.PositionUsedException;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


public class Grid2DTest {

    @Test
    public void position_valid() throws Exception {
        Grid grid = new Grid2D(3);
        assertFalse(grid.place("1", 'x'));
        assertFalse(grid.place("5", 'x'));
    }

    @Test
    public void position_already_used() throws Exception {
        Grid grid = new Grid2D(3);
        assertFalse(grid.isCellUsed("5"));

        grid.place("5", 'x');
        PositionUsedException thrown = assertThrows(
                PositionUsedException.class,
                () -> grid.place("5", 'x')
        );
        assertTrue(grid.isCellUsed("5"));

    }

    @Test
    public void position_invalid() throws Exception {
        Grid grid = new Grid2D(3);
        PositionInvalidException thrown = assertThrows(
                PositionInvalidException.class,
                () -> grid.place("0", 'x')
        );
    }

    @Test
    public void no_winner() throws Exception {
        Grid grid = new Grid2D(3);
        assertFalse( grid.place("1",'o'));
        assertFalse( grid.place("2",'x'));
        assertFalse( grid.place("3",'o'));

        assertFalse( grid.place("4",'x'));
        assertFalse( grid.place("5",'o'));
        assertFalse( grid.place("6",'x'));

        assertFalse( grid.place("7",'x'));
        assertFalse( grid.place("8",'o'));
        assertFalse( grid.place("9",'x'));
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

    @Test
    public void check_diagonal_1() throws Exception {
        Grid grid = new Grid2D(3);
        assertFalse( grid.place("1",'x'));
        assertFalse( grid.place("5",'x'));
        assertTrue( grid.place("9",'x'));

        assertTrue(((Grid2D)grid).getCellStatus(0,0));
        assertTrue(((Grid2D)grid).getCellStatus(1,1));
        assertTrue(((Grid2D)grid).getCellStatus(2,2));
    }

    @Test
    public void check_diagonal_2() throws Exception {
        Grid grid = new Grid2D(3);
        assertFalse( grid.place("3",'x'));
        assertFalse( grid.place("5",'x'));
        assertTrue( grid.place("7",'x'));

        assertTrue(((Grid2D)grid).getCellStatus(2,0));
        assertTrue(((Grid2D)grid).getCellStatus(1,1));
        assertTrue(((Grid2D)grid).getCellStatus(0,2));
    }

    @Test
    public void check_remaining_cells_count() throws Exception {
        int size = 3;
        Grid grid = new Grid2D(size);
        assertEquals(size*size, grid.getRemainingCells());
        grid.place("3",'x');
        assertEquals(size*size-1, grid.getRemainingCells());
        grid.place("1",'x');
        assertEquals(size*size-2, grid.getRemainingCells());
        grid.place("2",'x');
        assertEquals(size*size-3, grid.getRemainingCells());
        grid.place("4",'x');
        assertEquals(size*size-4, grid.getRemainingCells());
    }

    @Test
    public void get_set_value() throws Exception {
        Random random = new Random();
        int size = 3;
        Grid grid = new Grid2D(size);
        for (int i = 1; i <= size*size;i++){
            char player = random.nextInt(2) == 0 ? 'O' :'X';
            grid.setCellValue(i,player);
            assertEquals(player,grid.getValue(i));
        }
    }
}
