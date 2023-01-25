package tictactoe.client;

import tictactoe.CustomSocket;

import java.net.Socket;

/**
 *
 */
public class PlayerClient extends Client{

    public PlayerClient(){
        super("127.0.0.1", 9876);
    }

    public PlayerClient(String serverIP, int port) {
        super(serverIP, port);
    }

    @Override
    public void run(){
        try{
            System.out.println("Connection to server");
            server = new CustomSocket(new Socket(this.serverIP, this.port));
            System.out.println("Connected");

        } catch (Exception e) {
            System.out.println("Error on connection to server");
        }
    }
}
