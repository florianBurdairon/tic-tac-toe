package tictactoe.client;

import org.junit.jupiter.api.Test;
import tictactoe.NetworkMessage;
import tictactoe.ProtocolAction;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerClientTest {

    @Test
    public void player_start() {
        String input = "A1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Client client = new PlayerClient();
        NetworkMessage networkMessage = client.startGame("O","3","3");
        assertEquals(ProtocolAction.WaitMessage, networkMessage.getProtocolAction());
    }

    @Test
    public void player_play_valid() {
        String input = "A1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Client client = new PlayerClient();
        client.startGame("O","3","3");
        NetworkMessage networkMessage = client.play(null);
        assertEquals(ProtocolAction.Place, networkMessage.getProtocolAction());
    }

    @Test
    public void player_play_invalid() {
        String role = "O";
        String position = "A1";
        String input = position+"\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Client client = new PlayerClient();
        client.startGame(role,"3","3");
        NetworkMessage networkMessage = client.play(null);
        assertEquals(ProtocolAction.Place, networkMessage.getProtocolAction());
        assertEquals(position, networkMessage.getParameters()[0]);
        assertEquals(role, networkMessage.getParameters()[1]);
    }

    @Test
    public void opponent_play_valid() {
        Client client = new PlayerClient();
        client.startGame("O","3","3");
        NetworkMessage networkMessage = client.play("A1");
        assertEquals(ProtocolAction.Place, networkMessage.getProtocolAction());
    }

    @Test
    public void player_confirmation_yes() {
        String input = "oui\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Client client = new PlayerClient();
        client.startGame("O","3","3");
        NetworkMessage networkMessage = client.confirmation();
        assertEquals( ProtocolAction.Confirmation, networkMessage.getProtocolAction());
    }

    @Test
    public void player_confirmation_no() {
        String input = "non\nA1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Client client = new PlayerClient();
        client.startGame("O","3","3");
        NetworkMessage networkMessage = client.confirmation();
        assertEquals( ProtocolAction.Place, networkMessage.getProtocolAction());
    }

    @Test
    public void player_end_game_loose() {
        String input = "non\nA1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        PrintStream originalPrintStream = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Client client = new PlayerClient();
        client.startGame("O","3","3");
        NetworkMessage networkMessage = client.endGame("A1", 'X');
        assertEquals( "Joueur O : Défaite...", outContent.toString().split("\n")[0].trim());
    }

    @Test
    public void player_end_game_win() {
        String input = "non\nA1\n";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        PrintStream originalPrintStream = System.out;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        Client client = new PlayerClient();
        client.startGame("O","3","3");
        NetworkMessage networkMessage = client.endGame("A1", 'O');
        assertEquals( "Joueur O : Victoire !", outContent.toString().split("\n")[0].trim());
    }

    @Test
    public void player_validate() {
        Client client = new PlayerClient();
        client.startGame("O","3","3");
        NetworkMessage networkMessage = client.validate("A1");
        assertEquals( ProtocolAction.WaitMessage, networkMessage.getProtocolAction());
    }
}
