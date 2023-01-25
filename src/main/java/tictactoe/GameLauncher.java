package tictactoe;

import tictactoe.client.AIClient;
import tictactoe.client.Client;
import tictactoe.client.PlayerClient;
import tictactoe.server.Server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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

          public static NetworkMode FromInt(int id){
               switch (id){
                    // case 0: return NetworkMode.Local;
                    case 1: return NetworkMode.Host;
                    case 2: return NetworkMode.Client;
                    case 3: return NetworkMode.Server;
                    default: return NetworkMode.Local;
               }
          }
     }

     /**
      * Main function, first to execute
      * @param args
      */
     public static void main(String[] args) throws InterruptedException {
          NetworkMode netmode = NetworkMode.Local;
          BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));

          // No already defined network mode
          if (args.length == 0) {

               // Ask user the network mode
               System.out.print("\nVeuillez choisir votre mode de réseau pour jouer." +
                       "\n Disponible : 0 - Jeu en local (1vs1 ou 1vsIA)" +
                       "\n              1 - Hébergeur" +
                       "\n              2 - Client" +
                       "\n              3 - Serveur uniquement" +
                       "\nVotre choix : ");
               // Get the network mode chosen by the user
               try {
                    netmode = NetworkMode.FromInt(sysIn.readLine().charAt(0) - '0');
               } catch (Exception e) {
                    System.out.println("Erreur de saisie sur le choix de réseau." +
                            "\nMode réseau par défaut : Jeu en local");
                    netmode = NetworkMode.Local;
               }
          }
          // Already defined network mode on build
          else {

          }

          /**
           * Switch case on the chosen network mode
           *  - Local : Start 1 Server and 2 Clients
           *  - Host : Start 1 Server and 1 Client
           *  - Client : Start 1 Client
           *  - Server : Start 1 Server
           */
          switch (netmode){
               case Local: // 1 Server + 2 Client (Ask for human or AI)
                    // Asking for humanity of opponent
                    System.out.print("\nVeuillez choisir votre type d'adversaire :" +
                            "\n  0 - Humain" +
                            "\n  1 - Intelligence Artificielle" +
                            "\nVotre choix : ");
                    boolean opponentIsHuman = false;
                    try {
                         opponentIsHuman = (Integer.parseInt(sysIn.readLine()) == 0);
                    } catch (Exception e) {
                         System.out.println("Erreur de saisie sur le choix de l'adversaire en jeu local" +
                                 "\n Adversaire par défaut : Intelligence Artificielle");
                    }
                    // Starting Server
                    Server server_local = new Server();
                    server_local.start();
                    System.out.println("Lancement du serveur en mode local...");
                    TimeUnit.SECONDS.sleep(1);

                    // Starting first player
                    Client client_local_1 = new PlayerClient();
                    client_local_1.start();
                    // Starting second player based on user's choice
                    Client client_local_2;
                    if (opponentIsHuman){
                         client_local_2 = new PlayerClient();
                    } else {
                         client_local_2 = new AIClient();
                    }
                    client_local_2.start();
                    break;


               case Host: // 1 Server + 1 Client
                    Server server_host = new Server();
                    server_host.start();
                    System.out.println("Lancement du serveur en mode hébergeur...");
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("Adresse IP du serveur : " + server_host.getIpAddress());
                    PlayerClient client_host = new PlayerClient("127.0.0.1", 9876);
                    client_host.start();
                    break;
               case Client: // 1 Client
                    System.out.print("Adresse IP du serveur : ");
                    String ip = "127.0.0.1";
                    try {
                         ip = sysIn.readLine();
                    } catch (Exception e) {
                         System.out.println("Erreur lors de la récupération de l'adresse IP du server");
                    }
                    PlayerClient client = new PlayerClient(ip, 9876);
                    client.start();

                    break;
               case Server: // 1 Server
                    Server server = new Server();
                    server.start();
                    System.out.println("Lancement du serveur en mode autonome...");
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("Adresse IP du serveur : " + server.getIpAddress());
                    break;
          }
     }
}
