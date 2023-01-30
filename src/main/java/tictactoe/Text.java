package tictactoe;

public class Text {
    /**
     * ANSI reset code
     */
    static String ANSI_RESET = "\u001B[0m";
    /**
     * ANSI black code
     */
    static String ANSI_BLACK = "\u001B[30m";
    /**
     * ANSI red code
     */
    static String ANSI_RED = "\u001B[31;1m";
    /**
     * ANSI green code
     */
    String ANSI_GREEN = "\u001B[32m";
    /**
     * ANSI yellow code
     */
    static String ANSI_YELLOW = "\u001B[93m";
    /**
     * ANSI blue code
     */
    static String ANSI_BLUE = "\u001B[94m";
    /**
     * ANSI purple code
     */
    static String ANSI_PURPLE = "\u001B[95m";
    /**
     * ANSI cyan code
     */
    static String ANSI_CYAN = "\u001B[36m";
    /**
     * ANSI white code
     */
    static String ANSI_WHITE = "\u001B[97m";

    public static String error(String errorCode){
        String out = ANSI_RED;
        switch (errorCode){
            // Local error
            case "s": out += "Erreur de saisie"; break;

            // Server error
            case "0": out += "Erreur sur la sélection des dimensions."; break;
            case "1": out += "Impossible de jouer ici - Cette case est déjà utilisée."; break;
            case "2": out += "Impossible de jouer ici - Cette case ne fait pas partie de la grille."; break;
            default: out += "Erreur inconnue - N°" + errorCode; break;
        }
        out += ANSI_RESET;
        return out;
    }

    public static String connected(boolean connectionSuccess){
        String out = "";

        if (connectionSuccess){
            out = ANSI_BLUE + "Connexion réalisée avec succès";
        } else {
            out = ANSI_RED + "Connexion échouée";
        }
        out += ANSI_RESET;
        return out;
    }

    public static String askWidth(){
        return ANSI_WHITE + "Choisissez la taille de la grille : " + ANSI_RESET;
    }

    public static String askDimension(){
        return ANSI_WHITE + "Choisissez la dimension de la grille (2 pour 2D ou 3 pour 3D) : " + ANSI_RESET;
    }

    public static String turn(String role){
        return ANSI_WHITE + "\nA votre tour," + (role.equals("X")?ANSI_YELLOW:ANSI_BLUE) + " joueur " + role + ANSI_RESET;
    }

    public static String askPlay(boolean is3D){
        String out = ANSI_WHITE;
        if (is3D){
            out += "Choisissez une case où jouer (Ex: B3) : ";
        } else {
            out += "Choisissez une case où jouer (Ex: 4) : ";
        }
        out += ANSI_RESET;
        return out;
    }

    public static String askConfirm(){
        return ANSI_WHITE + "Etes-vous sûr de vouloir jouer ici ? (oui / non)" + ANSI_RESET;
    }

    public static String results(String role, boolean isVictory, boolean isDraw){
        String out = "\n" + (role.equals("X")?ANSI_YELLOW:ANSI_BLUE) + "Joueur " + role + " : ";
        if (isVictory){
            out += "Victoire !";
        }else {
            if (isDraw)
                out += "Égalité...";
            else
                out += "Défaite...";
        }
        out += ANSI_RESET;
        return out;
    }

    public static String opponentDisconnected(){
        return ANSI_RED + "Votre adversaire a été déconnecté." + ANSI_RESET;
    }

    public static String askSaveOrQuit(){
        return ANSI_WHITE +
                "Que souhaitez-vous faire ?" +
                "\n  0 - Quitter" +
                "\n  1 - Sauvegarder et quitter"
                + ANSI_RESET;
    }

    public static String selfDisconnected() {
        return ANSI_RED + "Vous avez été déconnecté du serveur." + ANSI_RESET;
    }

    public static String endGame(){
        return ANSI_PURPLE +
                "Partie terminée !" +
                "\nMerci d'avoir joué à notre jeu <3"
                + ANSI_RESET;
    }
}
