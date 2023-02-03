package tictactoe;

import org.junit.jupiter.api.Test;
import tictactoe.client.Client;
import tictactoe.client.PlayerClient;
import tictactoe.grid.Grid;
import tictactoe.grid.Grid3D;
import tictactoe.grid.Grid3DTest;
import tictactoe.server.Server;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    public void server_save() throws  NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        server = new Server();

        boolean isClient1TurnValue = true;
        String lastPlayerValue = "X";
        int gridDimension =3;
        int gridWidth =3;

        Method setGrid = Server.class.getDeclaredMethod("setGrid", int.class, int.class);
        setGrid.setAccessible(true);
        setGrid.invoke(server,gridDimension,gridWidth);

        Field isClient1Turn = Server.class.getDeclaredField("isClient1Turn");
        isClient1Turn.setAccessible(true);
        isClient1Turn.set(server,isClient1TurnValue);

        Field lastPlayer = Server.class.getDeclaredField("lastPlayer");
        lastPlayer.setAccessible(true);
        lastPlayer.set(server,lastPlayerValue);

        server.save("test");

        Server serverLoaded = new Server();
        Method loadGame = Server.class.getDeclaredMethod("loadGame", String.class);
        loadGame.setAccessible(true);
        loadGame.invoke(serverLoaded,"test");

        Field isClient1TurnLoaded = Server.class.getDeclaredField("isClient1Turn");
        isClient1TurnLoaded.setAccessible(true);

        Field lastPlayerLoaded = Server.class.getDeclaredField("lastPlayer");
        lastPlayerLoaded.setAccessible(true);

        Field grid = Server.class.getDeclaredField("grid");
        grid.setAccessible(true);

        assertEquals(lastPlayerValue,lastPlayerLoaded.get(serverLoaded));
        assertEquals(Grid3D.class.getName(),grid.get(serverLoaded).getClass().getName());
        assertEquals(gridWidth,((Grid)grid.get(serverLoaded)).getSize());
        assertEquals(isClient1TurnValue,isClient1TurnLoaded.get(serverLoaded));
    }
}

