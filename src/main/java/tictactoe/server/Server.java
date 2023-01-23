package tictactoe.server;
import java.net.*;
import java.io.*;

public class Server{
    final static int port = 9876;

    public static void main(String[] args){
        try {
            System.out.println("Creation of server...");
            ServerSocket server = new ServerSocket(port);
            System.out.println("Server socket created. \nConnect yourself on " + server.getInetAddress() + ":" + server.getLocalPort());

            System.out.println("Waiting for client...");
            Socket client = server.accept();
            System.out.println("Connected with " + client.getInetAddress());

            System.out.println("Reading client buffer");
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            System.out.println("Writing to client buffer");
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            System.out.println("Send 'Thomas le méchant' to client");
            out.println("Thomas le méchant");
            System.out.println("Sent message to client");

            System.out.println("Closing client connection...");
            client.close();
            System.out.println("Client connection stopped");

        } catch (Exception e){
            System.out.println("Error on server socket creation");
        }
    }
}
