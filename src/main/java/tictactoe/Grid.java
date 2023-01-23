package tictactoe;

import java.util.ArrayList;

public interface Grid {
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
}
