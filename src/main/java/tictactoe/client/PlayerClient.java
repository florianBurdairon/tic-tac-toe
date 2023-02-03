package tictactoe.client;

import com.google.gson.Gson;
import tictactoe.CustomSocket;
import tictactoe.NetworkMessage;
import tictactoe.ProtocolAction;
import tictactoe.Text;
import tictactoe.grid.Grid2D;
import tictactoe.grid.Grid3D;
import tictactoe.grid.exceptions.PositionInvalidException;
import tictactoe.grid.exceptions.PositionUsedException;

import java.io.*;
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
    private final BufferedReader sysIn;

    /**
     * Allow to know the last place
     */
    private String lastPosition = null;

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
     * @return a network message
     */
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

    @Override
    public NetworkMessage resumeGame(String[] saveList) {
        System.out.println(Text.askSave(saveList));

        boolean isSaveSelected = false;
        NetworkMessage answerMessage = new NetworkMessage(ProtocolAction.NONE);
        while (!isSaveSelected){
            try {
                String save = sysIn.readLine();
                if(Integer.parseInt(save) <= saveList.length && Integer.parseInt(save) >= 0){
                    String[] param = {save};
                    answerMessage = new NetworkMessage(ProtocolAction.ResumeGame, param);
                    isSaveSelected = true;
                }
            }
            catch (NumberFormatException ignored){}
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return answerMessage;
    }

    /**
     * Function on reception of "Start Game" by the server. Construct the adequate grid and get its role. If 'X', plays its turn.
     * @param role The role given by the server ('X' or 'O').
     * @param dimension The dimension of the grid (2D or 3D).
     * @param size The size of the grid (its width).
     * @return the message to answer to the server.
     */
    @Override
    public NetworkMessage startGame(String role, String nextPlayer, String dimension, String size, String serializedGrid) {
        this.role = role;
        if(serializedGrid != null) {
            isSavedGame = true;

            //Deserialize the json string
            Gson gson = new Gson();
            if(dimension.equals("3")) this.grid = gson.fromJson(serializedGrid, Grid3D.class);
            else this.grid = gson.fromJson(serializedGrid, Grid2D.class);
        }
        else if(dimension.equals("3")) this.grid = new Grid3D(Integer.parseInt(size));
        else this.grid = new Grid2D(Integer.parseInt(size));

        if (nextPlayer.equals(this.role)){
            return play(null);
        }else {
            System.out.println(Text.otherStarts());
        }
        return new NetworkMessage(ProtocolAction.WaitMessage);
    }

    /**
     * Function on reception of "Play" by the server. If posOpponent is set, add the opponent choice to the display-only grid.
     * Then, waits for user to choose its position. Finally, sends this position back to the server.
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
            } catch (PositionUsedException | PositionInvalidException e) {
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
                lastPosition = position;
                isEntered = true;
            } catch (Exception e) {
                System.out.println(Text.error("s"));
            }
        }
        param[0]=position;
        param[1]=role;

        if(position.equalsIgnoreCase("save")){
            lastPosition = null;
            if(!isSavedGame){
                System.out.println(Text.askSaveName());
                param[0] = "1";
                try {
                    String choix = sysIn.readLine();
                    if(!choix.equals("")) param[1] = choix;
                } catch (Exception e) {
                    System.out.println(Text.error("s"));
                }
            }
            else param[1] = "0";
            return new NetworkMessage(ProtocolAction.Quit,param);
        }
        if(position.equalsIgnoreCase("quit")){
            param[0] = "0";
            return new NetworkMessage(ProtocolAction.Quit, param);
        }
        return new NetworkMessage(ProtocolAction.Place,param);
    }

    /**
     * Function which asks at a first time if the player confirm that he want to place his pawn in this place
     * @return a protocol action if the player answer "Oui" to the question
     */
    @Override
    public NetworkMessage confirmation() {
        while(true){
            try {
                grid.display(lastPosition, role.charAt(0));
                lastPosition = null;
                System.out.println(Text.askConfirm());
                String confirm = sysIn.readLine();
                if(confirm.equalsIgnoreCase("oui") || confirm.isBlank()){
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

    /**
     * Function which ensure at a second time if the player can place his pawn
     * @param position Position of the pawn placed
     * @return a protocol action which indicate to the player that he must wait to his next turn
     */
    @Override
    public NetworkMessage validate(String position) {
        try {
            grid.place(position, role.charAt(0));
        } catch (PositionUsedException | PositionInvalidException e) {
            throw new RuntimeException(e);
        }
        grid.display();
        return new NetworkMessage(ProtocolAction.WaitMessage);
    }

    /**
     * Function which manage the end of the game: display the final grid and the win combinaison in case of victory
     * @param position Position of the pawn placed
     * @param role Role indicate which player plays currently
     * @param isDraw IsDraw is a variable indicating the case of an equality at the end
     * @return a protocol action to the both player
     */
    @Override
    public NetworkMessage endGame(String position, char role, char isDraw) {
        try {
            grid.place(position, role);
        } catch (PositionUsedException | PositionInvalidException e) {
            throw new RuntimeException(e);
        }
        System.out.println(Text.results(this.role, this.role.charAt(0) == role, isDraw == '1'));
        grid.display();
        return new NetworkMessage(ProtocolAction.WaitMessage);
    }

    /**
     * Function which manage the case where an opponent player is unintentionally disconnected from the server
     * It asks the last player what he can do: quit or quit and save the game
     * @return The protocol action link to quit or quit and save
     */
    @Override
    public NetworkMessage opponentDisconnected() {
        System.out.println(Text.opponentDisconnected());
        String[] param = new String[2];
        while (param[0] == null){
            System.out.println(Text.askSaveOrQuit());
            try {
                String choix = sysIn.readLine();
                if(choix.equals("0") || choix.equals("1")) param[0] = choix;
                else System.out.println(Text.error("s"));
            } catch (Exception e) {
                System.out.println(Text.error("s"));
            }
        }
        while (!isSavedGame && param[1] == null){
            System.out.println(Text.askSaveName());
            try {
                String choix = sysIn.readLine();
                if(!choix.equals("")) param[1] = choix;
            } catch (Exception e) {
                System.out.println(Text.error("s"));
            }
        }
        if(isSavedGame) param[1] = "0";
        return new NetworkMessage(ProtocolAction.Quit, param);
    }

    @Override
    public void quit() {
        System.out.println(Text.endGame());
    }
}
