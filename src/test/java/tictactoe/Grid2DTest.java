package tictactoe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class Grid2DTest {

    @Test
    public void no_winner() {
        Grid grid = new Grid2D(3);
        assertEquals(false, grid.checkWinner('x'));
        assertEquals(false, grid.checkWinner('o'));
    }
    @Test
    public void x_winner_line_1() {
        char[][] charGrid = {{'x','x','x'},{'0','\0','o'},{'\0','\0','o'}};
        Grid grid = new Grid2D(charGrid);
        assertEquals(true, grid.checkWinner('x'));
    }

    @Test
    public void x_winner_line_2() {
        char[][] charGrid = {{'0','\0','o'},{'x','x','x'},{'\0','\0','o'}};
        Grid grid = new Grid2D(charGrid);
        assertEquals(true, grid.checkWinner('x'));
    }

    @Test
    public void x_winner_line_3() {
        char[][] charGrid = {{'0','\0','o'},{'\0','\0','o'},{'x','x','x'}};
        Grid grid = new Grid2D(charGrid);
        assertEquals(true, grid.checkWinner('x'));
    }

    @Test
    public void x_winner_column_1() {
        char[][] charGrid = {{'x','\0','o'},{'x','\0','o'},{'x','o','o'}};
        Grid grid = new Grid2D(charGrid);
        assertEquals(true, grid.checkWinner('x'));
    }

    @Test
    public void x_winner_column_2() {
        char[][] charGrid = {{'\0','x','o'},{'\0','x','o'},{'\0','x','o'}};
        Grid grid = new Grid2D(charGrid);
        assertEquals(true, grid.checkWinner('x'));
    }

    @Test
    public void x_winner_column_3() {
        char[][] charGrid = {{'\0','\0','x'},{'\0','\0','x'},{'\0','\0','x'}};
        Grid grid = new Grid2D(charGrid);
        assertEquals(true, grid.checkWinner('x'));
    }

    @Test
    public void check_multi_win() {
        char[][] charGrid = {{'x','x','x'},{'\0','\0','x'},{'\0','\0','x'}};
        Grid grid = new Grid2D(charGrid);
        assertEquals(true, grid.checkWinner('x'));
    }
}
