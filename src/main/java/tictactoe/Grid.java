package tictactoe;

import java.util.ArrayList;

/**
 * interface grid
 * @author Halvick Thomas
 * @version 1
 */
public interface Grid extends Displayable {
    /**
     * Verifie si le joueur 'player' a gagné
     * @param player joueur à verifier
     * @return true si le le joueur 'player' à gagné
     */
    public boolean checkWinner(char player);
    /**
     * @return la taille de la grid
     */
    public int getSize();
    /**
     * @return Retourne la liste des case gagnante
     */
    public ArrayList<int[]> getWinner();

    /**
     * @return les lignes representant la grid
     */
    public String[] getGridAsStrings();

}
