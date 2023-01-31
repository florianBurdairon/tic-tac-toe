package tictactoe.server;

import tictactoe.*;
import tictactoe.grid.Grid;
import tictactoe.grid.Grid2D;
import tictactoe.grid.Grid3D;
import tictactoe.grid.exceptions.PositionInvalidException;
import tictactoe.grid.exceptions.PositionUsedException;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
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

    private String lastPlayer = "X";

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
            //System.out.println("En attente de joueur...");

            client1 = new CustomSocket(server.accept(), false);
            //System.out.println("Joueur 1 connecté");
            client2 = new CustomSocket(server.accept(), false);
            //System.out.println("Joueur 2 connecté");

            boolean isNetWorkError = selectDimensions();
            //System.out.println("Dimensions sélectionnées");

            startGame();

            boolean isEndGame = false;

            NetworkMessage msgClient1 = new NetworkMessage(ProtocolAction.NONE);
            NetworkMessage msgClient2 = new NetworkMessage(ProtocolAction.NONE);
            String[] paramClient1 = {};
            String[] paramClient2 = {};
            boolean isMsgClient1Used = true;
            boolean isMsgClient2Used = true;

            while(!isEndGame && !isNetWorkError){
                if(isMsgClient1Used){
                    msgClient1 = client1.read();
                    paramClient1 = msgClient1.getParameters();
                }
                if(isMsgClient2Used){
                    msgClient2 = client2.read();
                    paramClient2 = msgClient2.getParameters();
                }
                if(msgClient1.getProtocolAction() == ProtocolAction.NetworkError || msgClient2.getProtocolAction() == ProtocolAction.NetworkError){
                    CustomSocket client = (msgClient1.getProtocolAction() == ProtocolAction.NetworkError) ? client2 : client1;
                    networkError(client);
                    if (msgClient1.getProtocolAction() == ProtocolAction.NetworkError) {
                        isMsgClient1Used = false;
                        isMsgClient2Used = true;
                    }
                    else{
                        isMsgClient1Used = true;
                        isMsgClient2Used = false;
                    }
                }
                if(msgClient1.getProtocolAction() == ProtocolAction.Quit || msgClient2.getProtocolAction() == ProtocolAction.Quit){
                    CustomSocket client = (msgClient1.getProtocolAction() == ProtocolAction.Quit) ? client1 : client2;
                    quit(client);
                    if (msgClient1.getProtocolAction() == ProtocolAction.Quit) {
                        if(paramClient1[0].equals("0")) quit(client1);
                        else safeQuit(client1);
                        isMsgClient1Used = true;
                        isMsgClient2Used = false;
                    }
                    else {
                        if(paramClient1[0].equals("0")) quit(client2);
                        else safeQuit(client2);
                        isMsgClient1Used = false;
                        isMsgClient2Used = true;
                    }
                    isNetWorkError = true;
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
                    isEndGame = play(client1, client2);
                    isMsgClient1Used = true;
                    isMsgClient2Used = true;
                }
                if(msgClient2.getProtocolAction() == ProtocolAction.Confirmation && msgClient1.getProtocolAction() == ProtocolAction.WaitMessage){
                    isEndGame = play(client2, client1);
                    isMsgClient1Used = true;
                    isMsgClient2Used = true;
                }
            }
            if(isEndGame){
                msgClient1 = client1.read();
                msgClient2 = client2.read();
                if(msgClient1.getProtocolAction() == ProtocolAction.WaitMessage && msgClient2.getProtocolAction() == ProtocolAction.WaitMessage){
                    client1.send(new NetworkMessage(ProtocolAction.Quit));
                    client2.send(new NetworkMessage(ProtocolAction.Quit));
                    client1.disconnect();
                    client2.disconnect();
                }
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
    public boolean selectDimensions(){
        //System.out.println("Sending client1 to select dimensions");
        NetworkMessage msg = new NetworkMessage(ProtocolAction.SelectDimensions);
        client1.send(msg);

        boolean isDimensionSelected = false;

        //Loop if the answer is not correct and wait for another answer
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

            //If the answer is a message with the action AnswerDimension
            if (action == ProtocolAction.AnswerDimensions) {
                String[] parameters = answer.getParameters();
                //If the answer have 2 parameters and the size is greater than 2
                try{
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
                catch (NumberFormatException e){}
            }
            if(action == ProtocolAction.NetworkError) {
                System.out.println("Erreur réseau. Partie annulée.");
                return true;
            }
            //If the client1 didn't answer correctly the server send an error message
            if(!isDimensionSelected) {
                error(client1, "0");
            }
        }
        return false;
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

    public void verification(CustomSocket client, String position, char role){
        try {
            if(!grid.isCellUsed(position)){
                lastPlaceTurn[0] = position;
                lastPlaceTurn[1] = Character.toString(role);
                client.send(new NetworkMessage(ProtocolAction.AskConfirmation));
            }
            else{
                error(client, "1");
            }
        } catch (PositionInvalidException e) {
            error(client, "2");
        }
    }

    public boolean play(CustomSocket client1, CustomSocket client2){
        try {
            ProtocolAction action;
            boolean isWinner = grid.place(lastPlaceTurn[0], lastPlaceTurn[1].charAt(0));
            System.out.println(isWinner);
            lastPlayer = lastPlaceTurn[1];
            int nbCellFree = grid.getRemainingCells();
            if (isWinner || nbCellFree == 0){
                String[] param = new String[3];
                action = ProtocolAction.EndGame;
                param[0] = lastPlaceTurn[0];
                param[1] = lastPlaceTurn[1];
                param[2] = "0";
                if (!isWinner && nbCellFree == 0) param[2] = "1";
                client1.send(new NetworkMessage(action, param));
                Thread.sleep(200);
                client2.send(new NetworkMessage(action, param));
                return isWinner || nbCellFree == 0;
            }
            else{
                String[] param;
                param = lastPlaceTurn;
                client1.send(new NetworkMessage(ProtocolAction.Validate, param));
                Thread.sleep(500);
                client2.send(new NetworkMessage(ProtocolAction.Play, param));
            }
        } catch (PositionUsedException e) {
            throw new RuntimeException(e);
        } catch (PositionInvalidException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public void error(CustomSocket client, String errorCode){
        String[] param = {errorCode};
        NetworkMessage msg = new NetworkMessage(ProtocolAction.Error, param);
        client.send(msg);
    }

    public void networkError(CustomSocket client){
        client.send(new NetworkMessage(ProtocolAction.OpponentDisconnected));
    }

    public void quit(CustomSocket client){
        client.send(new NetworkMessage(ProtocolAction.Quit));
        client.disconnect();
    }

    public void safeQuit(CustomSocket client){
        quit(client);
        client1 = null;
        client2 = null;
        try {
            String path;
            if(System.getProperty("os.name").toUpperCase().equals("WIN")){
                path = System.getenv("APPDATA") + "/TicTacToe";
            }
            else{
                path = System.getenv(("HOME")) + "/.tictactoe";
            }
            Files.createDirectories(Paths.get(path));
            FileOutputStream fileOut = new FileOutputStream(path + "/grid.save");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(grid);
            out.close();
            fileOut.close();
            fileOut = new FileOutputStream(path + "/gameinfo.save");
            out = new ObjectOutputStream(fileOut);
            out.writeObject(lastPlayer);
            out.close();
            fileOut.close();
            System.out.println("Sauvegarde réalisée avec succès.");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void resumeGame(){

    }
}
