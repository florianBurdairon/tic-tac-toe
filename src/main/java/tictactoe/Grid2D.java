package tictactoe;

/**
 * Class grid2D
 * @author Halvick Thomas
 * @version 1
 */
public class Grid2D implements Grid {
    /**
     * 2d grid
     */
    private final char[][] grid;

    /**
     * grid of winning cells
     */
    private final boolean[][] gridWinner;

    /**
     * size of the grid
     */
    private final int size;

    /**
     * @param size size of the grid
     */
    public Grid2D(int size) {
        this.size = size;
        this.grid = new char[size][size];
        this.gridWinner = new boolean[size][size];
    }

    /**
     * @param grid grid to copy
     */
    public Grid2D(char[][] grid) {
        this.grid = grid;
        this.size = grid.length;
        this.gridWinner = new boolean[this.size][this.size];
    }

    /**
     * check if player 'player' won
     * @param player player charactere to check
     * @return true if the 'player' won
     */
    @Override
    public boolean checkWinner(char player) {
        return checkColumns(player) | checkRows(player) | checkDiagonals(player);
    }

    /**
     * @return lines representing the grid
     */
    @Override
    public String[] getGridAsStrings() {
        String[] out = new String[this.size];
        //number of caracter needed for the bigest number
        int log = (int)Math.log10(this.size*this.size)+1;
        for(int y = 0; y < this.size; y++){
            out[y] = "|";
            for(int x = 0; x < this.size; x++){
                if(this.grid[x][y] == '\0'){
                    //complete smaller number to be as long as the bigest number
                    out[y] += " "
                            + String.format("%1$" + log + "s", x+y*this.size+1);
                }
                else{
                    out[y] += " "
                            //Add color green to display
                            + (gridWinner[x][y] ? "\u001B[32m" : "")
                            //complete smaller number to be as long as the bigest number
                            + String.format("%1$" + log + "s", this.grid[x][y])
                            //end color
                            + (gridWinner[x][y] ? "\u001B[0m" : "");

                }
            }
            out[y] += " |";
        }
        return out;
    }

    /**
     * @return grid's size
     */
    @Override
    public int getSize() {
        return this.size;
    }

    /**
     * @param x x position
     * @param y y position
     * @return cell's value
     */
    public int getValue(int x, int y) {
        return grid[x][y];
    }

    /**
     * @param x x position
     * @param y y position
     * @return true if cell is a winning cell
     */
    public boolean getCellStatus(int x, int y) {
        return gridWinner[x][y];
    }


    /**
     * @param player joueur à verifier
     * @return true if at least one column is completed
     */
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
            if(columnWin){
                win = true;
                for (int y = 0; y < this.size; y++) {
                    gridWinner[x][y] = true;
                }
            }
        }
        return win;
    }

    /**
     * @param player player charactere to check
     * @return true if at least one row is completed
     */
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
                    gridWinner[x][y] = true;
                }
            }
        }
        return win;
    }

    /**
     * @param player player charactere to check
     * @return true if at least one of 4 diagonals is completed
     */
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
                gridWinner[i][i] = true;
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
                gridWinner[this.size-1-i][this.size-1-i] = true;
            }
        }
        return winDiag1 || winDiag2;
    }


    /**
     * Set cell as winning cell
     * @param x x position
     * @param y y position
     */
    public void setCellWinner(int x, int y){
        this.gridWinner[x][y] = true;
    }


    /**
     * place a player cell
     * @param x x position
     * @param y y position
     * @param player player charactere
     * @return true if the player won
     */
    public boolean place(int x, int y, char player){
        this.grid[x][y] = player;
        return this.checkWinner(player);
    }

    /**
     * Print 2D grid
     */
    @Override
    public void display() {
        for (String ligne: this.getGridAsStrings()) {
            System.out.println(ligne);
        }
    }
}
