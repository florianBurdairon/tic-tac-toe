package tictactoe;

import java.util.ArrayList;

public class Grid2D implements Grid {

    private char[][] grid;
    private ArrayList<int[]> gridWinner;

    private int size;

    public Grid2D(int size) {
        this.size = size;
        this.grid = new char[size][size];
        this.gridWinner = new ArrayList();
    }

    public Grid2D(char[][] grid) {
        this.grid = grid;
        System.out.println(grid.length);
        this.size = grid.length;
        this.gridWinner = new ArrayList();
    }



    @Override
    public boolean checkWinner(char player) {

        boolean win = checkColumns(player) | checkRows(player);
        /*for(int i = 0; i < this.gridWinner.size(); i++){
            System.out.println(this.gridWinner.get(i)[0]+" "+this.gridWinner.get(i)[1]);
        }*/
        return win;
    }

    private boolean checkColumns(char player){
        boolean win = false;
        for (int x = 0; x < this.size; x++) {
            boolean columnWin = true;
            for (int y = 0; y < this.size; y++) {
                if (grid[y][x] != player) {
                    columnWin = false;
                    break;
                }
            }
            if(columnWin){
                win = true;
                for (int y = 0; y < this.size; y++) {
                    gridWinner.add(new int[]{x, y});
                }
            }
        }
        return win;
    }

    private boolean checkRows(char player){
        boolean win = false;
        for (int y = 0; y < this.size; y++) {
            boolean columnWin = true;
            for (int x = 0; x < this.size; x++) {
                if (grid[y][x] != player) {
                    columnWin = false;
                    break;
                }
            }
            if(columnWin){
                win = true;
                for (int x = 0; x < this.size; x++) {
                    gridWinner.add(new int[]{x, y});
                }
            }
        }
        return win;
    }
}
