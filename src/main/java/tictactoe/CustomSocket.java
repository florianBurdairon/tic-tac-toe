package tictactoe;

import tictactoe.exceptions.ProtocolActionException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

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

    public void send(NetworkMessage networkMessage){
        String msg = "" + networkMessage.getProtocolAction();

        if (networkMessage.getParameters() != null){
            msg += ":";
            for (String s : networkMessage.getParameters()) {
                msg += s + ",";
            }
        }

        out.println(msg);
    }

    public void send(ProtocolAction protocolAction){
        this.send(new NetworkMessage(protocolAction));
    }
    public void send(ProtocolAction protocolAction, String[] param){
        this.send(new NetworkMessage(protocolAction, param));
    }

    public NetworkMessage read() throws ProtocolActionException {
        String msg;
        try {
            while (!in.ready()) {
                TimeUnit.MILLISECONDS.sleep(200);
            }
            msg = in.readLine();
        } catch (Exception e) {
            System.out.println("Impossible to read from socket");
            return new NetworkMessage(ProtocolAction.NetworkError);
        }

        NetworkMessage networkMessage = new NetworkMessage();

        if (msg.contains(":")) {
            String[] message = msg.split(":");
            ProtocolAction protocolAction = ProtocolAction.fromInt(Integer.parseInt(msg));
            if (protocolAction == ProtocolAction.NONE){
                throw new ProtocolActionException();
            }
            networkMessage.setProtocolAction(protocolAction);

            message = message[1].split(",");
            networkMessage.setParameters(message);

            return networkMessage;
        }

        ProtocolAction protocolAction = ProtocolAction.fromInt(Integer.parseInt(msg));
        if (protocolAction == ProtocolAction.NONE){
            throw new ProtocolActionException();
        }
        networkMessage.setProtocolAction(protocolAction);
        return networkMessage;
    }
}
