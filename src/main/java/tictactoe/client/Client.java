package tictactoe.client;
import tictactoe.CustomSocket;

import java.net.*;
import java.io.*;

import java.util.concurrent.TimeUnit;

public abstract class Client extends Thread{
    protected int port;
    protected String serverIP;
    protected CustomSocket server;

    public Client(String serverIP, int port){
        this.serverIP = serverIP;
        this.port = port;
    }

    /*@Override
    public void run(){
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
    }*/
}