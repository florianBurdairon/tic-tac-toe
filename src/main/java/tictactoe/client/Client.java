package tictactoe.client;

import tictactoe.CustomSocket;
import tictactoe.NetworkMessage;
import tictactoe.ProtocolAction;
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

            // If the message is an error
            if (networkMessage.getProtocolAction() != ProtocolAction.Error && networkMessage.getProtocolAction() != ProtocolAction.NetworkError){
                lastReceived = networkMessage;
            } else {
                System.out.println(networkMessage.getParameters()[0]);
                networkMessage = lastReceived;
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
                case AskConfirmation:
                    networkAnswer = confirmation();
                    break;
                case Validate:
                    networkAnswer = validate(parameters[0]);
                    break;
                case EndGame:
                    networkAnswer = endGame(parameters[0], parameters[1].charAt(0));
                    break;
                case Quit:
                    networkAnswer = new NetworkMessage(ProtocolAction.NONE);
                    isRunning = false;
                    break;
                default: networkAnswer = new NetworkMessage(ProtocolAction.NONE);
                    break;
            }

            server.send(networkAnswer);
        }
    }

    public abstract NetworkMessage selectDimensions();
    public abstract NetworkMessage startGame(String role, String dimension, String size);
    public abstract NetworkMessage play(String posOpponent);
    public abstract NetworkMessage confirmation();
    public abstract NetworkMessage validate(String position);
    public abstract NetworkMessage waitPlayer();
    public abstract NetworkMessage addAI();
    public abstract NetworkMessage saveAndQuit();
    public abstract NetworkMessage endGame(String position, char role);

}