package tictactoe;

import java.util.ArrayList;

/**
 * interface grid
 * @author Halvick Thomas
 * @version 1
 */
public interface Grid extends Displayable {
    /**
     * check if player 'player' won
     * @param player player charactere to check
     * @return true if the 'player' won
     */
    public boolean checkWinner(char player);
    /**
     * @return size size of the grid
     */
    public int getSize();

    /**
     * @return lines representing the grid
     */
    public String[] getGridAsStrings();

}
