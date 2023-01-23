package tictactoe.server;

import java.io.BufferedReader;

public class ClientBufferReader extends Thread {
    private BufferedReader reader;

    public ClientBufferReader(BufferedReader _reader) {
        reader = _reader;
    }

    @Override
    public void run() {
        try {
            while (true) {
                if (reader.ready()) {
                    System.out.println("Client : " + reader.readLine());
                }
            }
        } catch (Exception e) {
            System.out.println("Error on client buffer reading");
        }
    }
}
