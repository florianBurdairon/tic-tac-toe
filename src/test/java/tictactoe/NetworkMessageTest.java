package tictactoe;

import org.junit.jupiter.api.Test;
import tictactoe.client.Client;
import tictactoe.client.PlayerClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Class NetworkMessageTest
 * @author Halvick Thomas
 * @version 1
 */
public class NetworkMessageTest {

    @Test
    public void network_message_constructor() {
        ProtocolAction protocol = ProtocolAction.NONE;
        NetworkMessage netMess = new NetworkMessage(protocol);
        assertEquals(protocol, netMess.getProtocolAction());
    }

    @Test
    public void network_message_constructor2() {
        ProtocolAction protocol = ProtocolAction.WaitMessage;
        String[] parameters = {"params1","params2"};
        NetworkMessage netMess = new NetworkMessage(protocol,parameters);
        assertEquals(protocol, netMess.getProtocolAction());
        assertEquals(parameters, netMess.getParameters());
    }

    @Test
    public void network_message_set_parameters() {
        ProtocolAction protocol = ProtocolAction.WaitMessage;
        String[] parameters = {"params1","params2"};
        NetworkMessage netMess = new NetworkMessage(protocol,parameters);
        parameters = new String[]{"params3","params4"};
        netMess.setParameters(parameters);
        assertEquals(parameters, netMess.getParameters());
    }

    @Test
    public void network_message_set_protocol() {
        ProtocolAction protocol = ProtocolAction.WaitMessage;
        NetworkMessage netMess = new NetworkMessage(protocol);
        protocol = ProtocolAction.Place;
        netMess.setProtocolAction(protocol);
        assertEquals(protocol, netMess.getProtocolAction());
    }
}
