package tictactoe.client;
import java.net.*;
import java.io.*;

import java.util.concurrent.TimeUnit;

public class Client {
    final static int port = 9876;


    public static void main(String[] args){
        try{
            System.out.println("Connection to local host");
            Socket me = new Socket("127.0.0.1", port);
            System.out.println("Connected");

            System.out.println("Waiting 3 seconds...");
            TimeUnit.SECONDS.sleep(3);

            System.out.println("Reading IN buffer");
            BufferedReader in = new BufferedReader(new InputStreamReader(me.getInputStream()));
            //PrintStream out = new PrintStream(me.getOutputStream());
            //out.println(50);

            System.out.println("Printing IN buffer");
            System.out.println(in.readLine());
        } catch (Exception e) {
            System.out.println("Error on connection to server");
        }
    }
}
