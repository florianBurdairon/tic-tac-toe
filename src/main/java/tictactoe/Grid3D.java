package tictactoe;

import java.util.ArrayList;

public class Grid3D implements Grid {
    private Grid2D[] grid;
    private ArrayList<int[]> gridWinner;
    private int size;


    public Grid3D(int size) {
        this.size = size;
        this.grid = new Grid2D[size];
        for (int i = 0; i < size; i++) {
            this.grid[i] = new Grid2D(size);
        }
        this.gridWinner = new ArrayList();
    }

    public Grid3D(char[][][] grid) {
        this.size = grid.length;
        this.grid = new Grid2D[this.size];
        for(int i =0 ; i < this.size; i++){
            this.grid[i] = new Grid2D(grid[i]);
        }
        this.gridWinner = new ArrayList();
    }

    @Override
    public boolean checkWinner(char player) {
        boolean win = checkGrid2D(player) | checkDepth(player) | check(player);
        return win;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    public ArrayList<int[]> getWinner() {
        return this.gridWinner;
    }

    private boolean checkDepth(char player) {
        boolean win = false;
        for (int x = 0; x < this.size; x++) {
            for (int y = 0; y < this.size; y++) {
                boolean depthWin = true;
                for (int z = 0; z < this.size; z++) {
                    if (grid[z].getValue(x, y) != player) {
                        depthWin = false;
                        break;
                    }
                }
                if (depthWin) {
                    win = true;
                    for (int z = 0; z < this.size; z++) {
                        gridWinner.add(new int[]{z, x, y});
                    }
                }
            }
        }
        return win;
    }

    private boolean checkGrid2D(char player) {
        boolean win = false;
        for (int z = 0; z < this.size; z++) {
            boolean rowWinner = grid[z].checkWinner(player);
            if (rowWinner) {
                win = true;
                for (int[] winner : grid[z].getWinner()) {
                    this.gridWinner.add(new int[]{z,winner[0],winner[1]});
                }
            }
        }
        return win;
    }

    private boolean check(char player) {
        boolean win = false;
        //Verifie les axes (z,x) du bas vers le haut
        for (int y = 0; y < this.size; y++) {
            boolean diagonalWin = true;
            for (int i = 0; i < this.size; i++) {
                if(grid[i].getValue(i, y) != player){
                    diagonalWin=false;
                    break;
                }
            }
            if(diagonalWin){
                win=true;
                for (int i = 0; i < this.size; i++) {
                    this.gridWinner.add(new int[]{i,i,y});
                }
            }
        }

        //Verifie les axes (z,y) du bas vers le haut
        for (int x = 0; x < this.size; x++) {
            boolean diagonalWin = true;
            for (int i = 0; i < this.size; i++) {
                if(grid[i].getValue(x, i) != player){
                    diagonalWin=false;
                    break;
                }
            }
            if(diagonalWin){
                win=true;
                for (int i = 0; i < this.size; i++) {
                    this.gridWinner.add(new int[]{i,x,i});
                }
            }
        }

        //Verifie les axes (z,x) du haut vers le bas
        for (int y = 0; y < this.size; y++) {
            boolean diagonalWin = true;
            for (int i = this.size-1; i >= 0; i--) {
                if(grid[i].getValue(i, y) != player){
                    diagonalWin=false;
                    break;
                }
            }
            if(diagonalWin){
                win=true;
                for (int i = this.size-1; i >= 0; i--) {
                    this.gridWinner.add(new int[]{i,i,y});
                }
            }
        }

        //Verifie les axes (z,y) du haut vers le bas
        for (int x = 0; x < this.size; x++) {
            boolean diagonalWin = true;
            for (int i = this.size-1; i >=0 ; i--) {
                if(grid[i].getValue(x, i) != player){
                    diagonalWin=false;
                    break;
                }
            }
            if(diagonalWin){
                win=true;
                for (int i = this.size-1; i >=0 ; i--) {
                    this.gridWinner.add(new int[]{i,x,i});
                }
            }
        }

        boolean diagonalWin = true;
        for (int i = 0; i < this.size; i++) {
            if(grid[i].getValue(i, i) != player){
                diagonalWin = false;
                break;
            }
        }
        if(diagonalWin){
            win=true;
            for (int i = 0; i < this.size; i++) {
                this.gridWinner.add(new int[]{i,i,i});
            }
        }

        diagonalWin = true;
        for (int i = this.size-1; i >= 0 ; i--) {
            if(grid[this.size-1-i].getValue(i, i) != player){
                diagonalWin = false;
                break;
            }
        }
        if(diagonalWin){
            win=true;
            for (int i = this.size-1; i >= 0 ; i--) {
                this.gridWinner.add(new int[]{this.size-1-1,i,i});
            }
        }

        diagonalWin = true;
        for (int i = 0; i < this.size; i++) {
            if(grid[i].getValue(i, this.size-1-i) != player){
                diagonalWin = false;
                break;
            }
        }
        if(diagonalWin){
            win=true;
            for (int i = 0; i < this.size; i++) {
                this.gridWinner.add(new int[]{i,i,this.size-1-i});
            }
        }

        diagonalWin = true;
        for (int i = 0; i < this.size; i++) {
            if(grid[i].getValue(this.size-1-i,i ) != player){
                diagonalWin = false;
                break;
            }
        }
        if(diagonalWin){
            win=true;
            for (int i = 0; i < this.size; i++) {
                this.gridWinner.add(new int[]{i,this.size-1-i,i});
            }
        }

        return win;
    }
}
