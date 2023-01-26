package tictactoe;

/**
 * Class grid3D
 * @author Halvick Thomas
 * @version 1
 */
public class Grid3D implements Grid {

    /**
     * 3d grid
     */
    private Grid2D[] grid;

    /**
     * size of the grid
     */
    private int size;


    /**
     * @param size size of the grid
     */
    public Grid3D(int size) {
        this.size = size;
        this.grid = new Grid2D[size];
        for (int i = 0; i < size; i++) {
            this.grid[i] = new Grid2D(size);
        }
    }

    /**
     * @param grid grid to copy
     */
    public Grid3D(char[][][] grid) {
        this.size = grid.length;
        this.grid = new Grid2D[this.size];
        for(int i =0 ; i < this.size; i++){
            this.grid[i] = new Grid2D(grid[i]);
        }
    }


    /**
     * check if player 'player' won
     * @param player player charactere to check
     * @return true if the 'player' won
     */
    @Override
    public boolean checkWinner(char player) {
        boolean win = checkGrid2D(player) | checkDepth(player) | checkCrossX(player) |  checkCrossY(player) | checkDiagonals(player);
        return win;
    }

    /**
     * @return size of the grid
     */
    @Override
    public int getSize() {
        return this.size;
    }

    /**
     * @return lines representing the grid
     */
    @Override
    public String[] getGridAsStrings() {
        String[] out = new String[this.size+1];
        for(int i = 0; i < out.length;i++){
            out[i] = "";
        }
        for(int z = 0; z < this.size; z++){
            String[] out2D = this.grid[z].getGridAsStrings();

            for(int i = 0; i < this.size;i++){
                out[i+1] += (out[i+1] != "" ? "\t" : "") + out2D[i];
            }
        }
        //number of caracter needed for the bigest number
        int log = (int)Math.log10(this.size*this.size)+1;
        //Size of one grid = size * log + space (= size + 1)
        //midle is Size/2
        String space = " ".repeat(((log*this.size+this.size+1)/2));
        for(int i = 0; i < this.size;i++){
            out[0] += (out[0] != "" ? "\t" : "") +space +"("+(char)(i+65)+")"+ space;
        }
        return out;
    }

    /**
     * @param player player charactere to check
     * @return true if at least one of z axes is completed
     */
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
                        grid[z].setCellWinner(x, y);
                    }
                }
            }
        }
        return win;
    }

    /**
     * @param x x position
     * @param y y position
     * @param z z position
     * @return cell's value
     */
    public int getValue(int x, int y,int z) {
        return grid[z].getValue(x,y);
    }

    /**
     * @param x x position
     * @param y y position
     * @param z z position
     * @return true if cell is a winning cell
     */
    public boolean getCellStatus(int x, int y,int z) {
        return grid[z].getCellStatus(x,y);
    }


    /**
     * @param player player charactere to check
     * @return true if at least one of grid2d's conditions is completed
     */
    private boolean checkGrid2D(char player) {
        boolean win = false;
        for (int z = 0; z < this.size; z++) {
            boolean rowWinner = grid[z].checkWinner(player);
            if (rowWinner) {
                win = true;
            }
        }
        return win;
    }

    /**
     * @param player player charactere to check
     * @return true if one of the X diagonals parallel to the Y axis is completed
     */
    private boolean checkCrossX(char player){
        boolean win = false;
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
                    this.grid[i].setCellWinner(i,y);
                }
            }
        }

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
                    this.grid[i].setCellWinner(i,y);
                }
            }
        }

        return win;
    }

    /**
     * @param player player charactere to check
     * @return true if one of the Y diagonals parallel to the X axis is completed
     */
    private boolean checkCrossY(char player){
        boolean win = false;
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
                    this.grid[i].setCellWinner(x,i);
                }
            }
        }

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
                    this.grid[i].setCellWinner(x,i);

                }
            }
        }
        return win;
    }

    /**
     * @param player player charactere to check
     * @return true if one of the diagonals between 2 of the 8 corners is completed
     */
    private boolean checkDiagonals(char player) {
        boolean win = false;

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
                this.grid[i].setCellWinner(i,i);
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
                this.grid[this.size-1-i].setCellWinner(i,i);
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
                this.grid[i].setCellWinner(i,this.size-1-1);
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
                this.grid[i].setCellWinner(this.size-1-1,i);

            }
        }

        return win;
    }

    /**
     * place a player cell
     * @param x x position
     * @param y y position
     * @param z z position
     * @param player player charactere
     * @return true if the player won
     */
    public boolean place(int x, int y, int z, char player){
        this.grid[z].place(x,y,player);
        return this.checkWinner(player);
    }

    /**
     * Print 3D grid
     */
    @Override
    public void display() {
        for (String ligne: this.getGridAsStrings()) {
            System.out.println(ligne);
        }
    }
}
