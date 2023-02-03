package tictactoe;

public class Text implements  Displayable{

    public static String error(String errorCode){
        String out = ANSI_RED;
        switch (errorCode) {
            // Local error
            case "s" -> out += "Erreur de saisie";
            case "n" -> out += "Erreur réseau. Partie annulée.";


            // Server error
            case "0" -> out += "Erreur sur la sélection des dimensions.";
            case "1" -> out += "Impossible de jouer ici - Cette case est déjà utilisée.";
            case "2" -> out += "Impossible de jouer ici - Cette case ne fait pas partie de la grille.";
            default -> out += "Erreur inconnue - N°" + errorCode;
        }
        out += ANSI_RESET;
        return out;
    }

    public static String connected(boolean connectionSuccess){
        String out;

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

    public static String iaPlay(String role){
        return ANSI_WHITE + "\nAu tour de l'" + (role.equals("X")?ANSI_YELLOW:ANSI_BLUE) + "ia " + role + ANSI_RESET;
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
        if (isDraw){
            out += "Egalité...";
        }
        else if (isVictory){
            out += "Victoire !";
        }
        else{
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

    public static String saved(boolean saveSuccessful){
        String out = ANSI_BLUE;
        if (saveSuccessful)
            out += "Sauvegarde réalisée avec succès !";
        else
            out += "Echec de la sauvegarde.";
        out += ANSI_RESET;
        return out;
    }

    public static String selfDisconnected() {
        return ANSI_RED + "Vous avez été déconnecté du serveur." + ANSI_RESET;
    }

    public static String otherStarts(){
        return ANSI_WHITE + "C'est au tour du " + ANSI_YELLOW + "joueur X" + ANSI_RESET;
    }

    public static String endGame(){
        return ANSI_PURPLE +
                "Partie terminée !" +
                "\nMerci d'avoir joué à notre jeu <3"
                + ANSI_RESET;
    }

    public static String askNetworkMode(){
        return ANSI_WHITE +
                "\nVeuillez choisir votre mode de réseau pour jouer." +
                "\n Disponible : 0 - Jeu en local (1vs1 ou 1vsIA)" +
                "\n              1 - Hébergeur" +
                "\n              2 - Client" +
                "\n              3 - Serveur uniquement" +
                "\nVotre choix : "
                + ANSI_RESET;
    }

    public static String wrongArgs(String arg0){
        return ANSI_RED + "\nMode réseau invalide : " + arg0 + ANSI_RESET;
    }

    public static String askHumanity(){
        return ANSI_WHITE +
                "\nVeuillez choisir votre type d'adversaire :" +
                "\n  0 - Humain" +
                "\n  1 - Intelligence Artificielle" +
                "\nVotre choix : "
                + ANSI_RESET;
    }

    public static String serverStarting(int value_mode){
        String out = ANSI_BLUE + "\nLancement du serveur en mode ";
        switch (value_mode) {
            case 0 -> out += "local";
            case 1 -> out += "hébergeur";
            case 3 -> out += "serveur";
        }
        out += "..." + ANSI_RESET;
        return out;
    }

    public static String showIP(String ip) {
        return ANSI_BLUE + "Adresse IP du serveur : " + ip + ANSI_RESET;
    }

    public static String askIP(){
        return ANSI_WHITE + "Adresse IP du serveur : " + ANSI_RESET;
    }

    public static String askSave(String[] saveList){
        String out = ANSI_WHITE +
                "Des sauvegardes ont été détectées sur le serveur." +
                "\nSaisissez le numéro associé à la sauvegarde que vous souhaitez utiliser." +
                "\n  0 - Commencer une nouvelle partie";
        int i = 1;
        for (String save : saveList) {
            out += "\n  " + i + " - " + save;
            i++;
        }
        out += ANSI_RESET;
        return out;
    }

    public static String askSaveName(){
        return ANSI_WHITE + "Saisissez un nom pour la sauvegarde." + ANSI_RESET;
    }
}
