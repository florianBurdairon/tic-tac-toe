package tictactoe;

import java.util.ArrayList;

/**
 * Classe de grid3D
 * @author Halvick Thomas
 * @version 1
 */
public class Grid2D implements Grid {
    /**
     * Grille 2 dimensions
     */
    private char[][] grid;

    /**
     * Liste des coups gagnant
     */
    private ArrayList<int[]> gridWinner;

    /**
     * Taille de la grille
     */
    private int size;

    /**
     * @param size Taille de la grille
     */
    public Grid2D(int size) {
        this.size = size;
        this.grid = new char[size][size];
        this.gridWinner = new ArrayList();
    }

    /**
     * @param grid grille a copier
     */
    public Grid2D(char[][] grid) {
        this.grid = grid;
        this.size = grid.length;
        this.gridWinner = new ArrayList();
    }

    /**
     * Verifie si le joueur 'player' a gagné
     * @param player joueur à verifier
     * @return true si le le joueur 'player' à gagné
     */
    @Override
    public boolean checkWinner(char player) {
        boolean win = checkColumns(player) | checkRows(player) | checkDiagonals(player);
        return win;
    }

    /**
     * @return Retourne la liste des case gagnante
     */
    public ArrayList<int[]> getWinner(){
        return  this.gridWinner;
    }

    /**
     * @return la taille de la grid
     */
    @Override
    public int getSize() {
        return this.size;
    }

    /**
     * @param x
     * @param y
     * @return la valeur de la case
     */
    public int getValue(int x, int y) {
        return grid[x][y];
    }

    /**
     * @param player joueur à verifier
     * @return true si une des colonnes est gagnante
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

    /**
     * @param player joueur à verifier
     * @return true si une des lignes est gagnante
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
                    gridWinner.add(new int[]{x, y});
                }
            }
        }
        return win;
    }

    /**
     * @param player joueur à verifier
     * @return true si une des diagonales est gagnante
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
