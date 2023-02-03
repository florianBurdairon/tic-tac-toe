package tictactoe.grid;

import tictactoe.grid.exceptions.PositionInvalidException;
import tictactoe.grid.exceptions.PositionUsedException;

import java.io.Serializable;

/**
 * Class grid2D
 * @author Halvick Thomas
 * @version 2
 */
public class Grid2D implements Grid, Serializable {
    /**
     * 2d grid
     */
    private final char[][] grid;

    /**
     * grid of winning cells
     */
    private boolean[][] gridWinner;

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
    public int getRemainingCells() {
        return this.remainingCells;
    }

    /**
     * @return lines representing the grid
     */
    public String[] getGridAsStrings(){
        return getGridAsStrings(-1,-1,'\0');
    }

    /**
     * @param position string position ex "1"
     * @param player player character
     * @return lines representing the grid with the selection
     */
    public String[] getGridAsStrings(String position,char player){
        try {
            int[] positionArray = this.getPosition(position,this.size);
            return getGridAsStrings(positionArray[0],positionArray[1],player);
        }catch (Exception e){
            //If selection not valid return default grid
            return getGridAsStrings(-1,-1,'\0');
        }
    }

    /**
     * @param selectedX selected cell's x axe
     * @param selectedY selected cell's y axe
     * @param player player character
     * @return lines representing the grid with the selection
     */
    public String[] getGridAsStrings(int selectedX, int selectedY,char player){
        String[] out = new String[this.size];
        //number of character needed for the biggest number
        int log = (int)Math.log10(this.size*this.size)+1;
        for(int y = 0; y < this.size; y++){
            out[y] = "|";
            for(int x = 0; x < this.size; x++){

                if(selectedX == x && selectedY == y){
                    out[y] += ANSI_RED + ">" +  String.format("%1$" + log + "s", player) + "<"+ ANSI_RESET;
                    continue;
                }

                if (!(selectedX == x-1 &&selectedY == y)) {
                    out[y] += " ";
                }

                if(this.grid[x][y] == '\0'){
                    //complete smaller number to be as long as the biggest number
                    out[y] += String.format("%1$" + log + "s", x+y*this.size+1);
                }
                else if(gridWinner[x][y]){
                    out[y] +=
                            //Add color green to display
                            ANSI_GREEN
                            //complete smaller number to be as long as the biggest number
                            + String.format("%1$" + log + "s", this.grid[x][y])
                            //end color
                            + ANSI_RESET;
                }
                else {
                    out[y] += //Add color yellow to x player and blue to o player
                            (this.grid[x][y] == 'X' ? ANSI_YELLOW : ANSI_BLUE)
                                    //complete smaller number to be as long as the biggest number
                                    + String.format("%1$" + log + "s", this.grid[x][y])
                                    //end color
                                    + ANSI_RESET;

                }
            }
            if (!(selectedX == this.size-1 && selectedY == y)) {
                out[y] += " ";
            }
            out[y] += "|";
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
     * @return grid's total size
     */
    @Override
    public int getTotalSize() {
        return this.size * this.size;
    }

    /**
     * @param x x position
     * @param y y position
     * @return cell's value
     */
    public char getValue(int x, int y) {
        return grid[x][y];
    }

    /**
     * @param position [0,n*n[
     * @return cell's value
     */
    public char getValue(int position) {
        return grid[position%this.size][position /this.size];
    }

    /**
     * set value
     * @param position [0,n*n[
     * @param value value to be set
     */
    public void setValue(int position, char value){
        this.setValue(position%this.size,position /this.size,value);
    }

    /**
     * set cell value
     * @param x x position
     * @param y y position
     * @param value value to be set
     */
    public void setValue(int x, int y, char value){
        if(grid[x][y] == '\0' && value != '\0' ){
            this.remainingCells--;
        }
        else if(grid[x][y] != '\0' && value == '\0'){
            this.remainingCells++;
        }
        grid[x][y] = value;
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
     * @return true the column is completed
     */
    private boolean checkColumn(int x, char player){
        boolean win = true;
        for (int y = 0; y < this.size; y++) {
            if (grid[x][y] != player) {
                win = false;
                break;
            }
        }
        if(win){
            for (int y = 0; y < this.size; y++) {
                gridWinner[x][y] = true;
            }
        }
        return win;
    }

    /**
     * @param player player charactere to check
     * @return true row is completed
     */
    private boolean checkRow(int y,char player){
        boolean win = true;
        for (int x = 0; x < this.size; x++) {
            if (grid[x][y] != player) {
                win = false;
                break;
            }
        }
        //on stocke les coups gagnants
        if(win){
            for (int x = 0; x < this.size; x++) {
                gridWinner[x][y] = true;
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
     * place a player cell
     * @param position the case number
     * @param player player charactere
     * @return true if the player won
     * @throws PositionUsedException
     * @throws PositionInvalidException
     */
    public boolean place(int position, char player) throws PositionUsedException,PositionInvalidException {
        return  this.place(position%this.size,position/this.size,player);
    }

    /**
     * @param x
     * @param y
     * @param player player charactere
     * @return true if the player won
     * @throws PositionUsedException
     * @throws PositionInvalidException
     */
    public boolean place(int x, int y, char player) throws PositionUsedException {
        //check if cell is not already used
        if (this.grid[x][y] != '\0')
            throw new PositionUsedException();
        this.gridWinner = new boolean[this.size][this.size];
        this.grid[x][y] = player;
        this.remainingCells--;
        return checkColumn(x,player) | checkRow(y,player) | checkDiagonals(player);
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

    /**
     * Print 2D grid
     */
    @Override
    public void display(String position,char player) {
        for (String ligne: this.getGridAsStrings(position,player)) {
            System.out.println(ligne);
        }
    }

    /**
     * 2d Grid as string
     */
    public String toString() {
        String result = "";
        for (String ligne: this.getGridAsStrings()) {
            result+=ligne+'\n';
        }
        return result;
    }
}
