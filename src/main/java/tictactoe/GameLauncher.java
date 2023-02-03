package tictactoe;

import tictactoe.client.AIClient;
import tictactoe.client.Client;
import tictactoe.client.PlayerClient;
import tictactoe.server.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.concurrent.TimeUnit;

/**
 * Launch the game, with the specified network mode (on running)
 * @author Alban Bernard
 * @author Florian Burdairon
 */
public class GameLauncher {

    /**
     * Enumerator of all existing network mode
     */
    private enum NetworkMode {
        Local,
        Host,
        Client,
        Server;

        public static NetworkMode FromInt(int id) throws InvalidParameterException {
            return switch (id) {
                case 0 -> NetworkMode.Local;
                case 1 -> NetworkMode.Host;
                case 2 -> NetworkMode.Client;
                case 3 -> NetworkMode.Server;
                default -> throw new InvalidParameterException();
            };
        }

        public int getValue(){
            return switch (this) {
                case Local -> 0;
                case Host -> 1;
                case Client -> 2;
                case Server -> 3;
            };
        }
    }

    /**
     * Main function, first to execute
     * @param args arguments from execution
     */
    public static void main(String[] args) throws InterruptedException {
        NetworkMode netmode = null;
        BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));

        // No already defined network mode
        if (args.length == 0) {

            do {
                // Ask user the network mode
                System.out.print(Text.askNetworkMode());
                // Get the network mode chosen by the user
                try {
                    netmode = NetworkMode.FromInt(sysIn.readLine().charAt(0) - '0');
                } catch (Exception e) {
                    System.out.println(Text.error("s"));
                }
            } while (netmode == null);
        }
        // Already defined network mode on build
        else {
            if(args[0].equalsIgnoreCase("LOCAL")) netmode = NetworkMode.Local;
            else if(args[0].equalsIgnoreCase("HOST")) netmode = NetworkMode.Host;
            else if(args[0].equalsIgnoreCase("CLIENT")) netmode = NetworkMode.Client;
            else if(args[0].equalsIgnoreCase("SERVER")) netmode = NetworkMode.Server;
            else {
                System.out.println(Text.wrongArgs(args[0]));
                return;
            }
        }


        // Switch case on the chosen network mode
        //- Local : Start 1 Server and 2 Clients
        //- Host : Start 1 Server and 1 Client
        //- Client : Start 1 Client
        //- Server : Start 1 Server
        switch (netmode) {
            case Local -> { // 1 Server + 2 Client (Ask for human or AI)
                boolean opponentIsHuman = false;
                boolean hasChosen = false;
                do {
                    // Asking for humanity of opponent
                    System.out.print(Text.askHumanity());
                    try {
                        int valueEntered = Integer.parseInt(sysIn.readLine());
                        if (valueEntered == 0 || valueEntered == 1) {
                            opponentIsHuman = (valueEntered == 0);
                            hasChosen = true;
                        } else {
                            System.out.println(Text.error("s"));
                        }
                    } catch (Exception e) {
                        System.out.println(Text.error("s"));
                    }
                } while (!hasChosen);
                // Starting Server
                Server server_local = new Server();
                server_local.start();
                System.out.println(Text.serverStarting(netmode.getValue()));
                TimeUnit.SECONDS.sleep(1);
                //System.out.println("Serveur en attente de client(s) !");

                // Starting first player
                Client client_local_1 = new PlayerClient();
                client_local_1.start();
                TimeUnit.SECONDS.sleep(1);
                // Starting second player based on user's choice
                Client client_local_2;
                if (opponentIsHuman) {
                    client_local_2 = new PlayerClient();
                } else {
                    client_local_2 = new AIClient();
                }
                client_local_2.start();
            }
            case Host -> { // 1 Server + 1 Client
                Server server_host = new Server();
                server_host.start();
                System.out.println(Text.serverStarting(netmode.getValue()));
                TimeUnit.SECONDS.sleep(1);
                //System.out.println("Serveur en attente de client(s) !");
                System.out.println(Text.showIP(server_host.getIpAddress()));
                PlayerClient client_host = new PlayerClient("127.0.0.1", 9876);
                client_host.start();
            }
            case Client -> { // 1 Client
                System.out.print(Text.askIP());
                String ip = null;
                do {
                    try {
                        ip = sysIn.readLine();
                    } catch (Exception e) {
                        System.out.println(Text.error("s"));
                    }
                } while (ip == null);
                PlayerClient client = new PlayerClient(ip, 9876);
                client.start();
            }
            case Server -> { // 1 Server
                Server server = new Server();
                server.start();
                System.out.println(Text.serverStarting(netmode.getValue()));
                TimeUnit.SECONDS.sleep(1);
                //System.out.println("Serveur en attente de client(s) !");
                System.out.println(Text.showIP(server.getIpAddress()));
            }
        }
    }
}
