package tictactoe;

import org.junit.jupiter.api.Test;
import tictactoe.client.Client;
import tictactoe.client.PlayerClient;
import tictactoe.server.Server;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class ServerTest
 * @author Halvick Thomas
 * @version 1
 */
public class ServerTest {
    static final long TIMEOUT = 1000;

    static Server server;
    static Client client1;
    static Client client2;

   @Test
    public void connect_to_server() throws InterruptedException {
        server = new Server();
        server.start();
        //waiting the server to start
        Thread.sleep(100);
        client1 = new PlayerClient();
        client1.start();
        client2 = new PlayerClient();
        client2.start();
        long startTime = System.currentTimeMillis();
        while(true){
            if((server.getClient1() != null && server.getClient2() != null &&server.getClient1().isConnected() && server.getClient2().isConnected())){
                assertTrue(true);
                break;
            }
            if(System.currentTimeMillis()-startTime > TIMEOUT){
                assertTrue(false);
                break;
            }
        }
    }

}

