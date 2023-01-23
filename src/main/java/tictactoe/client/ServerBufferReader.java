package tictactoe.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerBufferReader extends Thread{

    private Socket socket;
    private BufferedReader in;

    @Override
    public void run() {
        try{
            while(true){
                if(in.ready()) System.out.println(in.readLine());
            }
        }
        catch (Exception e){
            System.out.println("reader");;
        }

    }

    public ServerBufferReader(Socket socket) {
        this.socket = socket;
        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
