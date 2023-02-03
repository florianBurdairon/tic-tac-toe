package tictactoe.client;

import tictactoe.CustomSocket;
import tictactoe.NetworkMessage;
import tictactoe.ProtocolAction;
import tictactoe.Text;
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
    * Function which send a message with the grid length and his dimension to the server.
    **/
    @Override
    public NetworkMessage selectDimensions() {
        String[] param = new String[2];

        String GridLength;
        String GridDimension;
        try {
            System.out.print(Text.askWidth());
            GridLength = sysIn.readLine();
            System.out.print(Text.askDimension());
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
            if(role.equals("X")) opponentRole = 'O';
            else opponentRole = 'X';
            try {
                grid.place(posOpponent, opponentRole);
            } catch (PositionUsedException e) {
                throw new RuntimeException(e);
            } catch (PositionInvalidException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(Text.turn(role));
        grid.display();

        String[] param = new String[2];

        String position = null;
        boolean isEntered = false;
        while(!isEntered){
            try {
                System.out.println(Text.askPlay(grid.getClass() == Grid3D.class));
                position = sysIn.readLine();
                isEntered = true;
            } catch (Exception e) {
                System.out.println(Text.error("s"));
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
                System.out.println(Text.askConfirm());
                String confirm = sysIn.readLine();
                if(confirm.equalsIgnoreCase("oui")){
                    return new NetworkMessage(ProtocolAction.Confirmation);
                }
                else{
                    return play(null);
                }
            } catch (Exception e) {
                System.out.println(Text.error("s"));
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
    public NetworkMessage endGame(String position, char role, char isDraw) {
        try {
            grid.place(position, role);
        } catch (PositionUsedException e) {
            throw new RuntimeException(e);
        } catch (PositionInvalidException e) {
            throw new RuntimeException(e);
        }
        /*String out = "Joueur " + this.role + " : ";
        if (isDraw == '1'){
            out += "Egalité...";
        }
        else if (this.role.charAt(0) == role){
            out += "Victoire !";
        }
        else{
            out += "Défaite...";
        }*/
        System.out.println(Text.results(this.role, this.role.charAt(0) == role, isDraw == '1'));
        grid.display();
        return new NetworkMessage(ProtocolAction.WaitMessage);
    }

    @Override
    public NetworkMessage opponentDisconnected() {
        System.out.println(Text.opponentDisconnected());
        while (true){
            System.out.println(Text.askSaveOrQuit());
            try {
                String choix = sysIn.readLine();
                String[] param = {choix};
                return new NetworkMessage(ProtocolAction.Quit, param);
            } catch (Exception e) {
                System.out.println(Text.error("s"));
            }
        }
    }
}
