package tictactoe.client;
import tictactoe.CustomSocket;
import tictactoe.Grid;
import tictactoe.NetworkMessage;
import tictactoe.ProtocolAction;

public abstract class Client extends Thread{
    protected int port;
    protected String serverIP;
    protected CustomSocket server;
    protected NetworkMessage lastReceived;

    protected Grid grid;

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
                System.out.println(e.getStackTrace());
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

            // Action depending on message received
            switch (networkMessage.getProtocolAction()){
                case SelectDimensions:
                    networkAnswer = selectDimensions();
                    break;
                case StartGame:
                    networkAnswer = startGame();
                    break;
                case Play:
                    networkAnswer = play(networkMessage.getParameters()[0]);
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
    public abstract NetworkMessage startGame();
    public abstract NetworkMessage play(String posOpponent);
    public abstract NetworkMessage validate();
    public abstract NetworkMessage waitPlayer();
    public abstract NetworkMessage addAI();
    public abstract NetworkMessage quit();
    public abstract NetworkMessage saveAndQuit();
}