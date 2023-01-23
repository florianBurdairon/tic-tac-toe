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
        this.size = grid.length;
        this.gridWinner = new ArrayList();
    }

    @Override
    public boolean checkWinner(char player) {
        boolean win = checkColumns(player) | checkRows(player) | checkDiagonals(player);
        return win;
    }

    public ArrayList<int[]> getWinner(){
        return  this.gridWinner;
    }
    @Override
    public int getSize() {
        return this.size;
    }

    public int getValue(int x, int y) {
        return grid[x][y];
    }

    private boolean checkColumns(char player){
        boolean win = false;
        for (int x = 0; x < this.size; x++) {
            boolean columnWin = true;
            for (int y = 0; y < this.size; y++) {
                if (grid[x][y] != player) {
                    columnWin = false;
                    break;
                }
            }
            //on stocke les coups gagnants
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
            boolean rowWin = true;
            for (int x = 0; x < this.size; x++) {
                if (grid[x][y] != player) {
                    rowWin = false;
                    break;
                }
            }
            //on stocke les coups gagnants
            if(rowWin){
                win = true;
                for (int x = 0; x < this.size; x++) {
                    gridWinner.add(new int[]{x, y});
                }
            }
        }
        return win;
    }

    private boolean checkDiagonals(char player){
        boolean winDiag1 = true;
        for (int i = 0; i < this.size; i++) {
            if(this.grid[i][i] !=player){
                winDiag1 = false;
                break;
            }
        }
        if(winDiag1 ) {
            for (int i = 0; i < this.size; i++) {
                this.gridWinner.add(new int[]{i, i});
            }
        }
        boolean winDiag2 = true;
        for (int i = 0; i < this.size; i++) {
            if(this.grid[this.size-1-i][this.size-1-i] !=player){
                winDiag2 = false;
                break;
            }
        }
        if(winDiag2){
            for (int i = 0; i < this.size; i++) {
                this.gridWinner.add(new int[]{this.size-1-i, this.size-1-i});
            }
        }
        return winDiag1 || winDiag2;
    }
}
