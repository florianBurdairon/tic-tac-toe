package tictactoe.client;

import org.junit.jupiter.api.Test;
import tictactoe.NetworkMessage;
import tictactoe.ProtocolAction;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AiClientTest {
    @Test
    public void ai_start() {
        String input = "A1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Client client = new AIClient();
        NetworkMessage networkMessage = client.startGame("O","3","3");
        assertEquals(ProtocolAction.WaitMessage, networkMessage.getProtocolAction());
    }

}
