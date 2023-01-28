package tictactoe.server;

import tictactoe.*;
import tictactoe.grid.Grid;
import tictactoe.grid.Grid2D;
import tictactoe.grid.Grid3D;
import tictactoe.grid.exceptions.PositionInvalidException;
import tictactoe.grid.exceptions.PositionUsedException;

import java.net.*;
import java.util.Random;

/**
 * Class to create a server. It will manage two clients once they are connected and allow them to play a tic-tac-toe game.
 * @author Bernard Alban
 * @author Blumet Thomas
 * @author Burdairon Florian
 * @version 1
 */
public class Server extends Thread {
    /**
     * Integer to keep the port of the connexion.
     */
    private int port;

    /**
     * The first client to connect to the server, he will choose the grid dimensions.
     */
    private CustomSocket client1;
    /**
     * The second client to connect to the server.
     */
    private CustomSocket client2;

    /**
     * Boolean to know whose turn it is.
     */
    private boolean isClient1Turn;

    /**
     * The grid of the game, at its actual state.
     */
    private Grid grid = null;

    private String[] lastPlaceTurn = new String[2];

    /**
     * Creates a local server with the default port (9876) open.
     */
    public Server () {
        this(9876);
    }

    /**
     * Creates a local server with the chosen port open.
     * @param port the port to open for the server.
     */
    public Server (int port) {
        this.port = port;
        // Start the server and wait for first client
    }

    /**
     * Main function of the server, manage the connexion and dimensions selection.
     * TODO: Implements a loop if players want to play again on the same network mode.
     */
    @Override
    public void run(){
        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("En attente de joueur...");

            client1 = new CustomSocket(server.accept(), false);
            System.out.println("Joueur 1 connecté");
            client2 = new CustomSocket(server.accept(), false);
            System.out.println("Joueur 2 connecté");

            selectDimensions();
            System.out.println("Dimensions sélectionnées");

            startGame();

            boolean isEndGame = false;

            NetworkMessage msgClient1 = new NetworkMessage(ProtocolAction.NONE);
            NetworkMessage msgClient2 = new NetworkMessage(ProtocolAction.NONE);
            String[] paramClient1 = {};
            String[] paramClient2 = {};
            boolean isMsgClient1Used = true;
            boolean isMsgClient2Used = true;

            while(!isEndGame){
                if(isMsgClient1Used){
                    msgClient1 = client1.read();
                    paramClient1 = msgClient1.getParameters();
                }
                if(isMsgClient2Used){
                    msgClient2 = client2.read();
                    paramClient2 = msgClient2.getParameters();
                }
                if(msgClient1.getProtocolAction() == ProtocolAction.Place && msgClient2.getProtocolAction() == ProtocolAction.WaitMessage){
                    verification(client1, paramClient1[0], paramClient1[1].charAt(0));
                    isMsgClient1Used = true;
                    isMsgClient2Used = false;
                }
                if(msgClient2.getProtocolAction() == ProtocolAction.Place && msgClient1.getProtocolAction() == ProtocolAction.WaitMessage){
                    verification(client2, paramClient2[0], paramClient2[1].charAt(0));
                    isMsgClient1Used = false;
                    isMsgClient2Used = true;
                }
                if(msgClient1.getProtocolAction() == ProtocolAction.Confirmation && msgClient2.getProtocolAction() == ProtocolAction.WaitMessage){
                    play(client1, client2);
                    isMsgClient1Used = true;
                    isMsgClient2Used = true;
                }
                if(msgClient2.getProtocolAction() == ProtocolAction.Confirmation && msgClient1.getProtocolAction() == ProtocolAction.WaitMessage){
                    play(client2, client1);
                    isMsgClient1Used = true;
                    isMsgClient2Used = true;
                }
                if(msgClient1.getProtocolAction() == ProtocolAction.Quit && msgClient2.getProtocolAction() == ProtocolAction.Quit){
                    isEndGame = true;
                }
            }
            msgClient1 = client1.read();
            msgClient2 = client2.read();
            if(msgClient1.getProtocolAction() == ProtocolAction.WaitMessage && msgClient2.getProtocolAction() == ProtocolAction.WaitMessage){
                client1.send(new NetworkMessage(ProtocolAction.Quit));
                client2.send(new NetworkMessage(ProtocolAction.Quit));
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Returns the ip address of the server to be able to connect to it locally (on the same network).
     * @return a String which is the ip address.
     */
    public String getIpAddress(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "0.0.0.0";
        }
    }

    /**
     * Function that send a message to ask client1 to choose the size and the dimension of the grid and process the answer.
     */
    public void selectDimensions(){
        //System.out.println("Sending client1 to select dimensions");
        NetworkMessage msg = new NetworkMessage(ProtocolAction.SelectDimensions);
        client1.send(msg);

        boolean isDimensionSelected = false;

        //Loop if the answer is not correct and wait for an other answer
        while(!isDimensionSelected){
            NetworkMessage answer;
            try{
                answer = client1.read();
            }
            //Problem with reading the answer
            catch (Exception e){
                //Set answer to null
                answer = new NetworkMessage(ProtocolAction.NONE);
                System.out.println("Error on reading");
            }
            ProtocolAction action = answer.getProtocolAction();

            //If the answer is an message with the action AnswerDimension
            if (action == ProtocolAction.AnswerDimensions) {
                String[] parameters = answer.getParameters();
                //If the answer have 2 parameters and the size is greater than 2
                if(parameters.length == 2 && Integer.parseInt(parameters[0]) > 2){
                    //If the dimension is 2D
                    if(Integer.parseInt(parameters[1]) == 2){
                        grid = new Grid2D(Integer.parseInt(parameters[0]));
                        isDimensionSelected = true;
                    }
                    //If the dimension is 3D
                    else if(Integer.parseInt(parameters[1]) == 3){
                        grid = new Grid3D(Integer.parseInt(parameters[0]));
                        isDimensionSelected = true;
                    }
                }
            }
            //If the client1 didn't answer correctly the server send an error message
            if(!isDimensionSelected) {
                error(client1, "Erreur lors de la saisi des dimensions de la grille.");
            }
        }
    }

    /**
     * Run the game from beginning to end, without any regards to what is extern of the game
     */
    public void startGame(){
        Random rand = new Random();
        NetworkMessage msgClient1;
        NetworkMessage msgClient2;
        String[] param1 = new String[3];
        String[] param2 = new String[3];

        isClient1Turn = rand.nextBoolean();

        if(isClient1Turn){
            param1[0] = "X";
            param2[0] = "O";
        }
        else {
            param1[0] = "O";
            param2[0] = "X";
        }

        if(grid.getClass() == Grid3D.class){
            param1[1] = "3";
            param2[1] = "3";
        }
        else{
            param1[1] = "2";
            param2[1] = "2";
        }

        param1[2] = Integer.toString(grid.getSize());
        param2[2] = Integer.toString(grid.getSize());

        msgClient1 = new NetworkMessage(ProtocolAction.StartGame, param1);
        msgClient2 = new NetworkMessage(ProtocolAction.StartGame, param2);
        client1.send(msgClient1);
        client2.send(msgClient2);
    }

    /**
     * Ends the game.
     */
    public void endGame(){
        NetworkMessage msgClient1 = new NetworkMessage(ProtocolAction.EndGame);
        NetworkMessage msgClient2 = new NetworkMessage(ProtocolAction.EndGame);
        client1.send(msgClient1);
        client2.send(msgClient2);
    }

    public void verification(CustomSocket client, String position, char role){
        Grid tmp = grid;
        try {
            tmp.place(position, role);
            lastPlaceTurn[0] = position;
            lastPlaceTurn[1] = Character.toString(role);
            client.send(new NetworkMessage(ProtocolAction.AskConfirmation));
        } catch (PositionUsedException e) {
            error(client, "Cette case est déjà utilisée.");
        } catch (PositionInvalidException e) {
            error(client, "La case n'est pas valide.");
        }
    }

    public void play(CustomSocket client1, CustomSocket client2){
        try {
            ProtocolAction action;
            String[] param;
            boolean isWinner = grid.place(lastPlaceTurn[0], lastPlaceTurn[1].charAt(0));
            if (isWinner){
                action = ProtocolAction.EndGame;
                param = lastPlaceTurn;
                client1.send(new NetworkMessage(action, param));
                client2.send(new NetworkMessage(action, param));
            }
            else{
                param = lastPlaceTurn;
                client1.send(new NetworkMessage(ProtocolAction.Validate, param));
                client2.send(new NetworkMessage(ProtocolAction.Play, param));
            }
        } catch (PositionUsedException e) {
            throw new RuntimeException(e);
        } catch (PositionInvalidException e) {
            throw new RuntimeException(e);
        }
    }

    public void validate(){

    }

    public void error(CustomSocket client, String message){
        String[] param = {message};
        NetworkMessage msg = new NetworkMessage(ProtocolAction.Error, param);
        client.send(msg);
    }

    public void networkError(){

    }

    public void resumeGame(){

    }
}
