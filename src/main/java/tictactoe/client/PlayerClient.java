package tictactoe.client;

import tictactoe.CustomSocket;
import tictactoe.NetworkMessage;
import tictactoe.ProtocolAction;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Class
 */
public class PlayerClient extends Client{

    private BufferedReader sysIn;

    public PlayerClient(){
        this("127.0.0.1", 9876);
    }

    public PlayerClient(String serverIP, int port) {
        super(serverIP, port);
        sysIn = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run(){
        try{
            server = new CustomSocket(new Socket(this.serverIP, this.port));
            System.out.println("Connecté");

            super.run();

        } catch (Exception e) {
            System.out.println("Error on connection to server");
        }
    }

    /**
    * Function which send a message with the gridlength and his dimension to the server.
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

    @Override
    public NetworkMessage startGame(String role) {
        this.role = role;
        if (this.role == "X"){
            return play(null);
        }
        return new NetworkMessage(ProtocolAction.WaitMessage);
    }

    @Override
    public NetworkMessage play(String posOpponent) {
        if(posOpponent!=null){
            //TODO Update player grid
        }
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

        return new NetworkMessage(ProtocolAction.Place,param);
    }

    @Override
    public NetworkMessage validate() {
        return new NetworkMessage(ProtocolAction.NONE);

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
    public NetworkMessage quit() {
        return new NetworkMessage(ProtocolAction.NONE);

    }

    @Override
    public NetworkMessage saveAndQuit() {
        return new NetworkMessage(ProtocolAction.NONE);

    }
}
