package tictactoe.client;

import tictactoe.CustomSocket;
import tictactoe.NetworkMessage;
import tictactoe.ProtocolAction;
import tictactoe.grid.Grid2D;
import tictactoe.grid.Grid3D;
import tictactoe.grid.exceptions.PositionInvalidException;
import tictactoe.grid.exceptions.PositionUsedException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Class to create a player. Made to be used by a real user (human).
 * @author Bernard Alban
 * @author Blumet Thomas
 * @author Burdairon Florian
 * @version 1
 */
public class PlayerClient extends Client{

    /**
     * The entry of the user buffer.
     */
    private BufferedReader sysIn;

    /**
     * Creates a new Player Client, connected in local ("127.0.0.1") on the default port (9876).
     */
    public PlayerClient(){
        this("127.0.0.1", 9876);
    }

    /**
     * Creates a new player client, connected on the given server ip and port.
     * @param serverIP The server ip to connect to.
     * @param port The port to connect to.
     */
    public PlayerClient(String serverIP, int port) {
        super(serverIP, port);
        sysIn = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Main function of the player client. Connects the player to the server and then runs just like any client.
     */
    @Override
    public void run(){
        try{
            server = new CustomSocket(new Socket(this.serverIP, this.port), true);
            System.out.println("Connecté");

            super.run();

        } catch (Exception e) {
            System.out.println("Error on connection to server");
        }
    }

    /**
    * Function which send a message with the grid length and his dimension to the server.
    **/
    @Override
    public NetworkMessage selectDimensions() {

        String[] param = new String[2];

        String GridLength;
        String GridDimension;
        try {
            System.out.print("Choisissez la taille de la grille : ");
            GridLength = sysIn.readLine();
            System.out.print("Choisissez la dimension de la grille : ");
            GridDimension= sysIn.readLine();
        } catch (Exception e) {
            e.printStackTrace();
            GridLength = "3";
            GridDimension = "2";
        }

        param[0]=GridLength;
        param[1]=GridDimension;

        return new NetworkMessage(ProtocolAction.AnswerDimensions,param);
    }

    /**
     * Function on reception of "Start Game" by the server. Construct the adequate grid and get its role. If 'X', plays its turn.
     * @param role The role given by the server ('X' or 'O').
     * @param dimension The dimension of the grid (2D or 3D).
     * @param size The size of the grid (its width).
     * @return the message to answer to the server.
     */
    @Override
    public NetworkMessage startGame(String role, String dimension, String size) {
        this.role = role;
        if(dimension.equals("3")) this.grid = new Grid3D(Integer.parseInt(size));
        else this.grid = new Grid2D(Integer.parseInt(size));

        if (this.role.equals("X")){
            return play(null);
        }
        return new NetworkMessage(ProtocolAction.WaitMessage);
    }

    /**
     * Function on reception of "Play" by the server. If posOpponent is set, add the opponent choice to the display-only grid. Then, waits for user to choose its position. Finally, sends this position back to the server.
     * @param posOpponent The opponent choice (its last turn).
     * @return The new turn of this user.
     */
    @Override
    public NetworkMessage play(String posOpponent) {
        if(posOpponent!=null){
            char opponentRole;
            if(role=="X") opponentRole = 'O';
            else opponentRole = 'X';
            try {
                grid.place(posOpponent, opponentRole);
            } catch (PositionUsedException e) {
                throw new RuntimeException(e);
            } catch (PositionInvalidException e) {
                throw new RuntimeException(e);
            }
        }

        grid.display();

        String[] param = new String[2];

        String position = null;
        boolean isEntered = false;
        while(!isEntered){
            try {
                if(grid.getClass() == Grid3D.class) System.out.print("Choisissez une case où jouer (Ex: A1) : ");
                else System.out.print("Choisissez une case où jouer (Ex: 1) : ");
                position = sysIn.readLine();
                isEntered = true;
            } catch (Exception e) {
                System.out.println("Erreur de saisie");
            }
        }
        param[0]=position;
        param[1]=role;

        return new NetworkMessage(ProtocolAction.Place,param);
    }

    @Override
    public NetworkMessage confirmation() {
        while(true){
            try {
                System.out.println("Etes-vous sûr de vouloir jouer ici ?");
                String confirm = sysIn.readLine();
                if(confirm.toLowerCase().equals("oui")){
                    return new NetworkMessage(ProtocolAction.Confirmation);
                }
                else{
                    return play(null);
                }
            } catch (Exception e) {
                System.out.println("Erreur de saisie");
            }
        }
    }


    @Override
    public NetworkMessage validate(String position) {
        try {
            grid.place(position, role.charAt(0));
        } catch (PositionUsedException e) {
            throw new RuntimeException(e);
        } catch (PositionInvalidException e) {
            throw new RuntimeException(e);
        }
        grid.display();
        return new NetworkMessage(ProtocolAction.WaitMessage);
    }

    @Override
    public NetworkMessage waitPlayer() {
        return new NetworkMessage(ProtocolAction.NONE);

    }

    @Override
    public NetworkMessage addAI() {
        return new NetworkMessage(ProtocolAction.NONE);

    }

    @Override
    public NetworkMessage saveAndQuit() {
        return new NetworkMessage(ProtocolAction.NONE);

    }

    @Override
    public NetworkMessage endGame(String position, char role) {
        try {
            grid.place(position, role);
        } catch (PositionUsedException e) {
            throw new RuntimeException(e);
        } catch (PositionInvalidException e) {
            throw new RuntimeException(e);
        }
        if (this.role.charAt(0) == role){
            System.out.println("Victoire !");
        }
        else{
            System.out.println("Défaite...");
        }
        grid.display();
        return new NetworkMessage(ProtocolAction.WaitMessage);
    }
}