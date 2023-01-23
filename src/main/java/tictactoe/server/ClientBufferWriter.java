package tictactoe.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ClientBufferWriter extends Thread {
    private PrintWriter writer;

    public ClientBufferWriter(PrintWriter _writer) {
        writer = _writer;
    }

    @Override
    public void run() {
        try {
            BufferedReader cons = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                writer.println(cons.readLine());
            }
        } catch (Exception e) {
            System.out.println("Error on client buffer writing");
        }
    }
}
