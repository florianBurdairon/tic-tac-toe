package tictactoe.client;

import tictactoe.CustomSocket;
import tictactoe.NetworkMessage;
import tictactoe.ProtocolAction;
import tictactoe.grid.Grid;

public abstract class Client extends Thread{
    protected int port;
    protected String serverIP;
    protected CustomSocket server;
    protected NetworkMessage lastReceived;

    protected Grid grid;
    protected String role;

    public Client(String serverIP, int port){
        this.serverIP = serverIP;
        this.port = port;
        this.lastReceived = new NetworkMessage(ProtocolAction.NONE);
    }

    @Override
    public void run() {

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
                case Validate:
                    networkAnswer = validate();
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
    public abstract NetworkMessage validate();
    public abstract NetworkMessage waitPlayer();
    public abstract NetworkMessage addAI();
    public abstract NetworkMessage quit();
    public abstract NetworkMessage saveAndQuit();
}