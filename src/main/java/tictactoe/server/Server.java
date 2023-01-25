package tictactoe.server;

import java.net.*;
import java.io.*;


public class Server extends Thread {
    private int port;
    private Socket client1;
    private Socket client2;

    private int gridLength;
    private int gridDim;

    public Server () {
        this(9876);
    }

    public Server (int port) {
        this.port = port;
        // Start the server and wait for first client
    }

    @Override
    public void run(){
        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("Waiting for client...");

            client1 = server.accept();
            System.out.println("Connected with " + client1.getInetAddress());
            client2 = server.accept();
            System.out.println("Connected with " + client2.getInetAddress());

        } catch (Exception e){
            System.out.println("Error on server socket");
        }
    }

    public String getIpAddress(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "0.0.0.0";
        }
    }
}
