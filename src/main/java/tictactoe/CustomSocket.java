package tictactoe;

import tictactoe.exceptions.ProtocolActionException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class CustomSocket {
    class ServerPingSender extends Thread {
        @Override
        public void run() {
            do {
                try {
                    Thread.sleep(2000);
                    if (isConnected) {
                        //System.out.println("Sent ping");
                        out.println("p");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (isConnected);
        }
    }

    class Firewall extends Thread {

        private long timeLastPing;
        private boolean isServer;

        public Firewall(boolean isServer){
            this.isServer = isServer;
        }

        @Override
        public void run() {
            timeLastPing = System.currentTimeMillis();
            do {
                // Get message from buffer
                String msg = "";
                try {
                    while (!in.ready() && isConnected) {
                        TimeUnit.MILLISECONDS.sleep(500);
                        if (System.currentTimeMillis() - timeLastPing > 10000) {
                            System.out.println("DISCONNECTED");
                            isConnected = false;
                        }
                    }
                    if (isConnected)
                        msg = in.readLine();
                    else
                        msg = "";
                } catch (Exception e) {
                    System.out.println("Impossible to read from socket");
                    isConnected = false;
                    msg = "";
                }

                // If there is a message :
                if (msg != "") {
                    // If the message is a ping :
                    if (msg.charAt(0) == 'p') { // We send pack a ping
                        //System.out.println("Received ping");
                        timeLastPing = System.currentTimeMillis();
                        if (!isServer) {
                            //System.out.println("Answer ping");
                            out.println("p");
                        }
                    }
                    else {
                        msg_to_listener = msg;
                    }
                }
            } while (isConnected);


            msg_to_listener = "" + ProtocolAction.NetworkError.getValue();
        }
    }

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    /**
     * Thread to manage the heartbeat system + communicate normal message
     */
    private Firewall firewall;
    private ServerPingSender pingSender;

    private volatile boolean isConnected;
    private boolean isServer;

    private volatile String msg_to_listener;

    public CustomSocket (Socket socket, boolean isServer) {
        this.isServer = true;
        this.socket = socket;

        try {
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (Exception e) {
            System.out.println("Erreur sur la création de socket");
        }

        isConnected = true;
        msg_to_listener = "";
        if (isServer) {
            pingSender = new ServerPingSender();
            pingSender.start();
        }
        firewall = new Firewall(isServer);
        firewall.start();
    }

    public boolean isConnected(){
        return this.isConnected;
    }

    public void send(NetworkMessage networkMessage){
        String msg = "" + networkMessage.getProtocolAction().getValue();

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

    public NetworkMessage read() throws ProtocolActionException, InterruptedException {
        String msg;

        try {
            while (msg_to_listener == "") {
                TimeUnit.MILLISECONDS.sleep(500);
            }
            msg = msg_to_listener;
            msg_to_listener = "";
        } catch (Exception e) {
            System.out.println("INTerrupted");
            throw new InterruptedException();
        }

        NetworkMessage networkMessage = new NetworkMessage();

        if (msg.contains(":")) {
            String[] message = msg.split(":");

            ProtocolAction protocolAction = ProtocolAction.fromInt(Integer.parseInt(message[0]));
            if (protocolAction == ProtocolAction.NONE){
                System.out.println("No detected protocol action - param");
                throw new ProtocolActionException();
            }
            networkMessage.setProtocolAction(protocolAction);

            message = message[1].split(",");
            networkMessage.setParameters(message);

            return networkMessage;
        }

        ProtocolAction protocolAction = ProtocolAction.fromInt(Integer.parseInt(msg));
        if (protocolAction == ProtocolAction.NONE){
            System.out.println("No detected protocol action");
            throw new ProtocolActionException();
        }
        networkMessage.setProtocolAction(protocolAction);
        return networkMessage;
    }
}
