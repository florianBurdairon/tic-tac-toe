package tictactoe;

import java.util.ArrayList;

/**
 * Classe de grid3D
 * @author Halvick Thomas
 * @version 1
 */
public class Grid3D implements Grid {

    /**
     * Tableau d'axe Z de Grille2D
     */
    private Grid2D[] grid;

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
    public Grid3D(int size) {
        this.size = size;
        this.grid = new Grid2D[size];
        for (int i = 0; i < size; i++) {
            this.grid[i] = new Grid2D(size);
        }
        this.gridWinner = new ArrayList();
    }

    /**
     * @param grid grille a copier
     */
    public Grid3D(char[][][] grid) {
        this.size = grid.length;
        this.grid = new Grid2D[this.size];
        for(int i =0 ; i < this.size; i++){
            this.grid[i] = new Grid2D(grid[i]);
        }
        this.gridWinner = new ArrayList();
    }


    /**
     * Verifie si le joueur 'player' a gagné
     * @param player joueur à verifier
     * @return true si le le joueur 'player' à gagné
     */
    @Override
    public boolean checkWinner(char player) {
        boolean win = checkGrid2D(player) | checkDepth(player) | checkCrossX(player) |  checkCrossY(player) | checkDiagonals(player);
        return win;
    }

    /**
     * @return la taille de la grid
     */
    @Override
    public int getSize() {
        return this.size;
    }

    /**
     * @return Retourne la liste des case gagnante
     */
    public ArrayList<int[]> getWinner() {
        return this.gridWinner;
    }


    /**
     * @return les lignes representant la grid
     */
    @Override
    public String[] getGridAsStrings() {
        String[] out = new String[this.size+1];
        for(int i = 0; i < out.length;i++){
            out[i] = "";
        }
        for(int z = 0; z < this.size; z++){
            String[] out2D = this.grid[0].getGridAsStrings();

            for(int i = 0; i < this.size;i++){
                out[i+1] += (out[i+1] != "" ? "\t" : "") + out2D[i];
            }
        }
        //espace a gauche et a droite du label Z =  longueur total/nombre d'element - 1 tab
        String espace = " ".repeat(((out[1].length()/this.size)/2-1));
        for(int i = 0; i < this.size;i++){
            out[0] += (out[0] != "" ? "\t" : "") +espace +"("+(char)(i+65)+")"+ espace;
        }
        return out;
    }

    /**
     * @param player joueur à verifier
     * @return true si les axes Z sont gagnante
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
                        gridWinner.add(new int[]{z, x, y});
                    }
                }
            }
        }
        return win;
    }

    /**
     * @param player joueur à verifier
     * @return true si un axe des axes des grid2D est gagnante
     */
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

    /**
     * @param player joueur à verifier
     * @return true si un des diagonales X parallèle a l'axe Y est gagnante
     */
    private boolean checkCrossX(char player){
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

        return win;
    }

    /**
     * @param player joueur à verifier
     * @return true si un des diagonales Y parallèle a l'axe X est gagnante
     */
    private boolean checkCrossY(char player){
        boolean win = false;
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
        return win;
    }

    /**
     * @param player joueur à verifier
     * @return true si une des diagonales entre 2 des 8 coins est gagnante
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

    /**
     * Affiche la grid3D
     */
    @Override
    public void display() {
        for (String ligne: this.getGridAsStrings()) {
            System.out.println(ligne);
        }
    }
}
