package tictactoe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class Grid3DTest {

    @Test
    public void no_winner() {
        Grid grid = new Grid3D(3);
        assertEquals(false, grid.checkWinner('x'));
        assertEquals(false, grid.checkWinner('o'));
    }

    @Test
    public void x_winner_line_1() {
        char[][][] charGrid = {
                {
                        {'x','x','x'},
                        {'0','\0','o'},
                        {'\0','\0','o'}
                },
                {
                        {'x','x','x'},
                        {'0','\0','o'},
                        {'\0','\0','o'}
                },
                {
                        {'x','x','x'},
                        {'0','\0','o'},
                        {'\0','\0','o'}
                },
        };
        Grid grid = new Grid3D(charGrid);
        assertEquals(true, grid.checkWinner('x'));
    }

    @Test
    public void x_winner_line_2() {
        char[][][] charGrid = {
                {
                        {'x','x','x'},
                        {'0','\0','o'},
                        {'\0','\0','o'}
                },
                {
                        {'x','x','x'},
                        {'0','\0','o'},
                        {'\0','\0','o'}
                },
                {
                        {'x','x','x'},
                        {'0','\0','o'},
                        {'\0','\0','o'}
                },
        };
        Grid grid = new Grid3D(charGrid);
        assertEquals(true, grid.checkWinner('x'));
    }

    @Test
    public void x_winner_line_3() {
        char[][][] charGrid = {
                {
                        {'x','x','x'},
                        {'0','\0','o'},
                        {'\0','\0','o'}
                },
                {
                        {'x','x','x'},
                        {'0','\0','o'},
                        {'\0','\0','o'}
                },
                {
                        {'x','x','x'},
                        {'0','\0','o'},
                        {'\0','\0','o'}
                },
        };
        Grid grid = new Grid3D(charGrid);
        assertEquals(true, grid.checkWinner('x'));
    }

    @Test
    public void x_winner_column_1() {
        char[][][] charGrid = {
                {
                        {'x','x','x'},
                        {'0','\0','o'},
                        {'\0','\0','o'}
                },
                {
                        {'x','x','x'},
                        {'0','\0','o'},
                        {'\0','\0','o'}
                },
                {
                        {'x','x','x'},
                        {'0','\0','o'},
                        {'\0','\0','o'}
                },
        };
        Grid grid = new Grid3D(charGrid);
        assertEquals(true, grid.checkWinner('x'));
    }

    @Test
    public void x_winner_column_2() {
        char[][][] charGrid = {
                {
                        {'x','x','x'},
                        {'0','\0','o'},
                        {'\0','\0','o'}
                },
                {
                        {'x','x','x'},
                        {'0','\0','o'},
                        {'\0','\0','o'}
                },
                {
                        {'x','x','x'},
                        {'0','\0','o'},
                        {'\0','\0','o'}
                },
        };
        Grid grid = new Grid3D(charGrid);
        assertEquals(true, grid.checkWinner('x'));
    }

    @Test
    public void x_winner_column_3() {
        char[][][] charGrid = {
                {
                        {'x','x','x'},
                        {'0','\0','o'},
                        {'\0','\0','o'}
                },
                {
                        {'x','x','x'},
                        {'0','\0','o'},
                        {'\0','\0','o'}
                },
                {
                        {'x','x','x'},
                        {'0','\0','o'},
                        {'\0','\0','o'}
                },
        };
        Grid grid = new Grid3D(charGrid);
        assertEquals(true, grid.checkWinner('x'));
    }

    @Test
    public void check_win_depth() {
        char[][][] charGrid = {
                {
                        {'o','o','x'},
                        {'o','\0','o'},
                        {'\0','\0','o'}
                },
                {
                        {'x','o','x'},
                        {'0','\0','o'},
                        {'\0','\0','o'}
                },
                {
                        {'o','x','x'},
                        {'0','\0','o'},
                        {'\0','\0','o'}
                },
        };
        Grid grid = new Grid3D(charGrid);
        assertEquals(true, grid.checkWinner('x'));
    }

    @Test
    public void check_win_diagonal() {
        char[][][] charGrid = {
                {
                        {'x','o','\0'},
                        {'o','\0','o'},
                        {'\0','\0','x'}
                },
                {
                        {'\0','o','x'},
                        {'0','x','\0'},
                        {'\0','\0','o'}
                },
                {
                        {'o','x','x'},
                        {'\0','\0','o'},
                        {'\0','\0','x'}
                },
        };
        Grid grid = new Grid3D(charGrid);
        assertEquals(true, grid.checkWinner('x'));
    }

    @Test
    public void check_win_diagonal2() {
        char[][][] charGrid = {
                {
                        {'\0','o','\0'},
                        {'o','\0','o'},
                        {'\0','\0','x'}
                },
                {
                        {'\0','o','x'},
                        {'0','x','\0'},
                        {'\0','\0','o'}
                },
                {
                        {'x','\0','x'},
                        {'\0','\0','o'},
                        {'\0','\0','x'}
                },
        };
        Grid grid = new Grid3D(charGrid);
        assertEquals(true, grid.checkWinner('x'));
    }

    @Test
    public void check_win_diagonal3() {
        char[][][] charGrid = {
                {
                        {'\0','o','x'},
                        {'o','\0','o'},
                        {'\0','\0','\0'}
                },
                {
                        {'\0','o','\0'},
                        {'0','x','\0'},
                        {'\0','\0','o'}
                },
                {
                        {'x','\0','o'},
                        {'\0','\0','o'},
                        {'x','\0','\0'}
                },
        };
        Grid grid = new Grid3D(charGrid);
        assertEquals(true, grid.checkWinner('x'));
    }

    @Test
    public void check_win_diagonal4() {
        char[][][] charGrid = {
                {
                        {'\0','o','o'},
                        {'o','\0','o'},
                        {'x','\0','\0'}
                },
                {
                        {'\0','o','\0'},
                        {'0','x','\0'},
                        {'\0','\0','o'}
                },
                {
                        {'x','\0','x'},
                        {'\0','\0','o'},
                        {'o','\0','\0'}
                },
        };
        Grid grid = new Grid3D(charGrid);
        assertEquals(true, grid.checkWinner('x'));
    }


    @Test
    public void check_multi_win() {
        char[][][] charGrid = {
                {
                        {'x','x','x'},
                        {'0','\0','o'},
                        {'\0','\0','o'}
                },
                {
                        {'x','x','x'},
                        {'0','\0','o'},
                        {'\0','\0','o'}
                },
                {
                        {'x','x','x'},
                        {'0','\0','o'},
                        {'\0','\0','o'}
                },
        };
        Grid grid = new Grid3D(charGrid);
        assertEquals(true, grid.checkWinner('x'));
    }
}
