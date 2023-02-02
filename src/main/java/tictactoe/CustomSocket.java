package tictactoe;

import tictactoe.exceptions.ProtocolActionException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * Class for our custom sockets. It allows to create a connexion between two instances of this class using Java sockets and server sockets.
 * @author Bernard Alban
 * @version 2
 */
public class CustomSocket {
    /**
     * Thread HeartbeatEmitter.
     * Internal class to emit the heartbeat on the server side of connexion.
     * It sends a message every 2 seconds to the other side (client) until the connexion ends.
     */
    class HeartbeatEmitter extends Thread {
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

    /**
     * Thread Firewall
     * Internal class acting as a firewall to dispatch received messages to the good location.
     * Manage automatically the heartbeat signal and mark as readable by the {@link CustomSocket#read() read()} method what is not a heartbeat.
     */
    class Firewall extends Thread {

        private long timeOfLastHeartbeat;
        private boolean isServer;

        /**
         * Create the firewall.
         * @param isServer is this side of the connexion the server side?
         */
        public Firewall(boolean isServer){
            this.isServer = isServer;
        }

        @Override
        public void run() {
            timeOfLastHeartbeat = System.currentTimeMillis();
            do {
                // Get message from buffer
                String msg = "";
                try {
                    while (!in.ready() && isConnected) {
                        TimeUnit.MILLISECONDS.sleep(150);
                        if (System.currentTimeMillis() - timeOfLastHeartbeat > 10000) {
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
                    // If the message is a heartbeat :
                    if (msg.charAt(0) == 'p') { // We send pack a heartbeat
                        //System.out.println("Received heartbeat");
                        timeOfLastHeartbeat = System.currentTimeMillis();
                        if (!isServer) {
                            //System.out.println("Answer heartbeat");
                            out.println("p");
                        }
                    }
                    // It is not a heartbeat
                    else {
                        msg_to_listener = msg;
                    }
                }
            } while (isConnected);

            msg_to_listener = "" + ProtocolAction.NetworkError.getValue();
        }
    }

    /**
     * The original java socket to manage connexion.
     */
    private Socket socket;
    /**
     * The connexion buffer to read from.
     */
    private BufferedReader in;
    /**
     * The connexion buffer to write to.
     */
    private PrintWriter out;

    private Firewall firewall;
    private HeartbeatEmitter heartbeatEmitter;

    /**
     * Boolean to keep if this connexion is still on.
     */
    private volatile boolean isConnected;
    /**
     * Boolean to know if this side of the connexion is the server side.
     */
    private boolean isServer;

    /**
     * Message read from the connexion, never a heartbeat
     */
    private volatile String msg_to_listener;

    /**
     * Create a custom socket using a java socket and a boolean to set this side as the server side
     * @param socket The socket that creates the connexion
     * @param isServer Boolean to know if this side of the connexion is the server side.
     */
    public CustomSocket (Socket socket, boolean isServer) {
        this.isServer = isServer;
        this.socket = socket;

        try {
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (Exception e) {
            System.out.println("Error on socket creation");
        }

        isConnected = true;
        msg_to_listener = "";
        if (isServer) {
            heartbeatEmitter = new HeartbeatEmitter();
            heartbeatEmitter.start();
        }
        firewall = new Firewall(isServer);
        firewall.start();
    }

    /**
     * Returns if the connexion is still on.
     * @return true if the connexion is still on.
     */
    public boolean isConnected(){
        return this.isConnected;
    }

    /**
     * Disconnects this socket from the connexion. It will take 10 seconds for the other side to detect this disconnection via the heartbeat system.
     */
    public void disconnect() {
        this.isConnected = false;
        try {
            firewall.join();
            if (this.isServer){
                heartbeatEmitter.join();
            }
        } catch (Exception e) {}
    }

    /**
     * Allow this side to send a network message to the other side of the connexion.
     * @param networkMessage The network message to send to the other side of the connexion.
     */
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

    /**
     * Allow this side to send a network message (containing only a protocol action) to the other side of the connexion.
     * @param protocolAction The protocol action to send to the other side of the connexion.
     */
    public void send(ProtocolAction protocolAction){
        this.send(new NetworkMessage(protocolAction));
    }

    /**
     * Allow this side to send a network message to the other side of the connexion.
     * @param protocolAction The protocol action to send to the other side of the connexion.
     * @param param The parameters of the protocol action.
     */
    public void send(ProtocolAction protocolAction, String[] param){
        this.send(new NetworkMessage(protocolAction, param));
    }

    /**
     * Wait for a message to be sent from the other side and then returns it.
     * @return The network message read sent from the other side of the connexion.
     * @throws ProtocolActionException if the protocol does not exist.
     * @throws InterruptedException if impossible to read from the connexion.
     */
    public NetworkMessage read() throws ProtocolActionException, InterruptedException {
        String msg;

        // Get the message once it has been through the firewall
        try {
            while (msg_to_listener == "") {
                TimeUnit.MILLISECONDS.sleep(500);
            }
            msg = msg_to_listener;
            msg_to_listener = "";
        } catch (Exception e) {
            System.out.println("Interrupted");
            throw new InterruptedException();
        }

        // Recreation of the message received
        NetworkMessage networkMessage = new NetworkMessage();

        // If there are parameters
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

        // No parameters
        ProtocolAction protocolAction = ProtocolAction.fromInt(Integer.parseInt(msg));
        if (protocolAction == ProtocolAction.NONE){
            System.out.println("No detected protocol action");
            throw new ProtocolActionException();
        }
        networkMessage.setProtocolAction(protocolAction);
        return networkMessage;
    }
}
