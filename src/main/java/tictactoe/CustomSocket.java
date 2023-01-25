package tictactoe;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;

public class CustomSocket {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public CustomSocket (Socket socket) {
        this.socket = socket;

        try {
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (Exception e) {
            System.out.println("Erreur sur la création de socket");
        }
    }
}
