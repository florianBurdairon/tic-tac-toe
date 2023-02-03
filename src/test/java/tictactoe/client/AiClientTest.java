package tictactoe.client;

import org.junit.jupiter.api.Test;
import tictactoe.NetworkMessage;
import tictactoe.ProtocolAction;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Class AiClientTest
 * @author Halvick Thomas
 * @version 1
 */
public class AiClientTest {
    @Test
    public void ai_start() {
        String input = "A1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Client client = new AIClient();
        NetworkMessage networkMessage = client.startGame("O","X","3","3",null);
        assertEquals(ProtocolAction.WaitMessage, networkMessage.getProtocolAction());
    }

    @Test
    public void ai_confirmation() {
        Client client = new AIClient();
        NetworkMessage networkMessage = client.confirmation();
        assertEquals( ProtocolAction.Confirmation, networkMessage.getProtocolAction());
    }

    @Test
    public void ai_end_game() {
        Client client = new AIClient();
        NetworkMessage networkMessage = client.endGame("1",'X','1');
        assertEquals( ProtocolAction.WaitMessage, networkMessage.getProtocolAction());
    }

    @Test
    public void ai_opponent_disconnected() {
        Client client = new AIClient();
        NetworkMessage networkMessage = client.opponentDisconnected();
        assertEquals( ProtocolAction.NONE, networkMessage.getProtocolAction());
    }

}
