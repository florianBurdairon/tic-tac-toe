package tictactoe.grid;

import tictactoe.grid.exceptions.PositionInvalidException;
import tictactoe.grid.exceptions.PositionUsedException;

/**
 * interface grid
 * @author Halvick Thomas
 * @version 1
 */
public interface Grid extends Displayable {
    /**
     * place a player cell
     * @param position the case number
     * @param player player charactere
     * @return true if the player won
     * @throws PositionUsedException
     * @throws PositionInvalidException
     */
    public boolean place(String position, char player) throws PositionUsedException,PositionInvalidException;

    /**
     * place a player cell
     * @param position the case number
     * @return true if the cell is used
     * @throws PositionInvalidException
     */
    public boolean isCellUsed(String position) throws PositionInvalidException;

    /**
     * @return size size of the grid
     */
    public int getSize();

    /**
     * @param position [1,n^x] x=dimension of the grid
     * @return cell's value
     */
    public char getValue(int position);

    /**
     * set cell value
     * @param position [1,n^x] x=dimension of the grid
     * @param value value to be set
     */
    public void setCellValue(int position, char value);

    /**
     * @return count remaining cell
     */
    public int getRemainingCells();

    /**
     * Print grid
     */
    public void display();

    /**
     * Print the grid with the cell selected by player
     */
    public void display(String position,char player);

}
