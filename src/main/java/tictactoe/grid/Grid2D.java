package tictactoe.grid;

import tictactoe.grid.exceptions.PositionInvalidException;
import tictactoe.grid.exceptions.PositionUsedException;

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
     * Remaining cell to be played
     */
    private int remainingCells;

    /**
     * @param size size of the grid
     */
    public Grid2D(int size) {
        this.size = size;
        this.grid = new char[size][size];
        this.gridWinner = new boolean[size][size];
        this.remainingCells = size*size;
    }

    /**
     * @return count remaining cell
     */
    @Override
    public int getRemainingCells() {
        return this.remainingCells;
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
                else if(gridWinner[x][y]){
                    out[y] += " "
                            //Add color green to display
                            + ANSI_GREEN
                            //complete smaller number to be as long as the bigest number
                            + String.format("%1$" + log + "s", this.grid[x][y])
                            //end color
                            + ANSI_RESET;
                }
                else {
                    out[y] += " "
                            //Add color yellow to x player and blue to o player
                            + (this.grid[x][y] == 'X' ? ANSI_YELLOW : ANSI_BLUE)
                            //complete smaller number to be as long as the bigest number
                            + String.format("%1$" + log + "s", this.grid[x][y])
                            //end color
                            + ANSI_RESET;
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
            if(this.grid[i][i] != player){
                winDiag1 = false;
                break;
            }
        }
        if(winDiag1) {
            for (int i = 0; i < this.size; i++) {
                gridWinner[i][i] = true;
            }
        }
        boolean winDiag2 = true;
        for (int i = 0; i < this.size; i++) {
            if(this.grid[i][this.size-1-i] != player){
                winDiag2 = false;
                break;
            }
        }
        if(winDiag2){
            for (int i = 0; i < this.size; i++) {
                gridWinner[i][this.size-1-i] = true;
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
     * @param position the case number
     * @param player player charactere
     * @return true if the player won
     * @throws PositionUsedException
     * @throws PositionInvalidException
     */
    public boolean place(String position, char player) throws PositionUsedException,PositionInvalidException {
        int[] positionArray = this.getPosition(position,this.size);
        int x = positionArray[0];
        int y = positionArray[1];
        return  this.place(x,y,player);
    }

    /**
     * @param x
     * @param y
     * @param player player charactere
     * @return true if the player won
     * @throws PositionUsedException
     * @throws PositionInvalidException
     */
    public boolean place(int x, int y, char player) throws PositionUsedException,PositionInvalidException {
        //check if cell is not already used
        if (this.grid[x][y] != '\0')
            throw new PositionUsedException();
        this.grid[x][y] = player;
        this.remainingCells--;
        return checkColumns(player) | checkRows(player) | checkDiagonals(player);
    }

    /**
     * place a player cell
     * @param position the case number
     * @return true if the cell is used
     * @throws PositionInvalidException
     */
    @Override
    public boolean isCellUsed(String position) throws PositionInvalidException {
        int[] positionArray = getPosition(position,this.size);
        return  this.isCellUsed(positionArray[0],positionArray[1]);
    }

    /**
     * place a player cell
     * @param x
     * @param y
     * @return true if the cell is used
     * @throws PositionInvalidException
     */
    public boolean isCellUsed(int x, int y) throws PositionInvalidException {
        return  this.grid[x][y] != '\0';
    }

    /**
     * Return a position from a string
     * @param position
     * @param size of the grid
     * @return [x,y]
     * @throws PositionInvalidException
     */
    static public int[] getPosition(String position,int size) throws PositionInvalidException {
        try {
            int number = Integer.parseInt(position)-1;
            //check if cell is in this grid
            if(number < 0 || number >= size*size)
                throw new PositionInvalidException();

            int x = number % size;
            int y = (int) number / size;
            return new int[]{x,y};
        }
        catch(NumberFormatException e){
            throw new PositionInvalidException();
        }
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
