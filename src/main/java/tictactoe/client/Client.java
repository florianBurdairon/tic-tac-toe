package tictactoe.client;
import java.net.*;
import java.io.*;

import java.util.concurrent.TimeUnit;

public class Client {
    final static int port = 9876;


    public static void main(String[] args){
        try{
            System.out.println("Connection to local host");
            Socket me = new Socket("192.168.169.176", port);
            System.out.println("Connected");

            ServerBufferWriter writer = new ServerBufferWriter(me);
            ServerBufferReader reader = new ServerBufferReader(me);

            writer.start();
            reader.start();

            writer.join();
            reader.join();

            me.close();
        } catch (Exception e) {
            System.out.println("Error on connection to server");
        }
    }
}