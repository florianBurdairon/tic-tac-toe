package tictactoe.server;

import java.net.*;
import java.io.*;

public class Server{
    final static int port = 9876;

    public static void main(String[] args){
        try {
            System.out.println("Creation of server...");
            ServerSocket server = new ServerSocket(port);
            System.out.println("Server socket created. \nConnect yourself on " + InetAddress.getLocalHost().getHostAddress() + ":" + server.getLocalPort());

            System.out.println("Waiting for client...");
            Socket client = server.accept();
            System.out.println("Connected with " + client.getInetAddress());

            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            ClientBufferReader reader = new ClientBufferReader(in);
            ClientBufferWriter writer = new ClientBufferWriter(out);

            reader.start();
            writer.start();

            reader.join();
            writer.join();

//            while (true){
//                while(in.ready()) {}
//                    System.out.println(in.readLine());
//                out.println(cons.readLine());
//            }

//            while (!in.ready() && !client.isClosed()) {
//                TimeUnit.SECONDS.sleep(2);
//                System.out.println("Waiting..." + client.isClosed() + ", " + client.isConnected());
//            }
//            if (!client.isClosed())
//                System.out.println(in.readLine());
//
//            System.out.println("Closing client connection...");
//            client.close();
//            System.out.println("Client connection stopped");

        } catch (Exception e){
            System.out.println("Error on server socket");
        }
    }
}
