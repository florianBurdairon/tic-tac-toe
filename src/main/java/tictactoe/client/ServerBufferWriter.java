package tictactoe.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerBufferWriter extends Thread{

    private Socket socket;
    private PrintWriter out;
    private BufferedReader cons;

    @Override
    public void run() {
        try {
            while(true){
                out.println(cons.readLine());
            }
        }
        catch (Exception e){
            e.printStackTrace();;
        }
    }

    public ServerBufferWriter(Socket socket) {
        this.socket = socket;
        try{
            this.out = new PrintWriter(socket.getOutputStream(), true);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        this.cons = new BufferedReader(new InputStreamReader(System.in));
    }
}
