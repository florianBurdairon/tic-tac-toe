package tictactoe.client;

import tictactoe.CustomSocket;
import tictactoe.NetworkMessage;
import tictactoe.ProtocolAction;
import tictactoe.Text;
import tictactoe.grid.Grid;

/**
 * Class to create a client. It needs to connect to a server.
 * @author Bernard Alban
 * @author Blumet Thomas
 * @author Burdairon Florian
 * @version 1
 */
public abstract class Client extends Thread{
    /**
     * Integer to keep the port of the connexion.
     */
    protected int port;
    /**
     * String to keep the ip address of the connexion.
     */
    protected String serverIP;
    /**
     * The server it is connected to.
     */
    protected CustomSocket server;
    /**
     * The last received message from the server. Useful to asks again the user if an error occurs.
     */
    protected NetworkMessage lastReceived;

    /**
     * The client-side grid, only used for display purpose.
     */
    protected Grid grid;

    /**
     * The role of the player ('X' or 'O').
     */
    protected String role;

    /**
     * Creates a client with the given server ip address and the port.
     * @param serverIP The server ip address to connect the client to.
     * @param port The port to connect the client to the server.
     */
    public Client(String serverIP, int port){
        this.serverIP = serverIP;
        this.port = port;
        this.lastReceived = new NetworkMessage(ProtocolAction.NONE);
    }

    /**
     * Main function of the client. Waits for a server message and answer it.
     */
    @Override
    public void run() {

        // The client is still running
        boolean isRunning = true;

        while (isRunning) {
            // Get the message from the server
            NetworkMessage networkMessage;
            try {
                networkMessage = server.read();
            } catch (Exception e) {
                e.getStackTrace();
                networkMessage = new NetworkMessage(ProtocolAction.NONE);
            }

            // The message to send back
            NetworkMessage networkAnswer;
            String[] parameters = networkMessage.getParameters();

            // Action depending on message received
            switch (networkMessage.getProtocolAction()){
                case SelectDimensions:
                    networkAnswer = selectDimensions();
                    break;
                case StartGame:
                    networkAnswer = startGame(parameters[0], parameters[1], parameters[2]);
                    break;
                case Play:
                    networkAnswer = play(parameters[0]);
                    break;
                case Error:
                    System.out.println(Text.error(parameters[0]));
                    if(parameters[0].equals("0")) networkAnswer = selectDimensions();
                    else networkAnswer = play(null);
                    break;
                case AskConfirmation:
                    networkAnswer = confirmation();
                    break;
                case Validate:
                    networkAnswer = validate(parameters[0]);
                    break;
                case EndGame:
                    networkAnswer = endGame(parameters[0], parameters[1].charAt(0), parameters[2].charAt(0));
                    break;
                case OpponentDisconnected:
                    networkAnswer = opponentDisconnected();
                    break;
                case NetworkError:
                    networkAnswer = new NetworkMessage(ProtocolAction.NONE);
                    isRunning = false;
                    System.out.println(Text.selfDisconnected());
                    break;
                case Quit:
                    networkAnswer = new NetworkMessage(ProtocolAction.NONE);
                    isRunning = false;
                    server.disconnect();
                    System.out.println(Text.endGame());
                    break;
                default: networkAnswer = new NetworkMessage(ProtocolAction.NONE);
                    break;
            }

            if (isRunning) server.send(networkAnswer);
        }
    }

    public abstract NetworkMessage selectDimensions();
    public abstract NetworkMessage startGame(String role, String dimension, String size);
    public abstract NetworkMessage play(String posOpponent);
    public abstract NetworkMessage confirmation();
    public abstract NetworkMessage validate(String position);
    public abstract NetworkMessage endGame(String position, char role, char isDraw);
    public abstract NetworkMessage opponentDisconnected();

}