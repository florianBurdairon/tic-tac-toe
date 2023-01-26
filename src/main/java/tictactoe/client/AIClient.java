package tictactoe.client;

import tictactoe.NetworkMessage;
import tictactoe.ProtocolAction;

public class AIClient extends Client{

    public AIClient() {
        super("127.0.0.1", 9876);
    }

    @Override
    public void run(){

    }

    @Override
    public NetworkMessage selectDimensions() {
        return new NetworkMessage(ProtocolAction.NONE);
    }

    @Override
    public NetworkMessage startGame(String role) {
        return null;
    }

    @Override
    public NetworkMessage play(String posOpponent) {
        return new NetworkMessage(ProtocolAction.NONE);

    }

    @Override
    public NetworkMessage validate() {
        return new NetworkMessage(ProtocolAction.NONE);

    }

    @Override
    public NetworkMessage waitPlayer() {
        return new NetworkMessage(ProtocolAction.NONE);

    }

    @Override
    public NetworkMessage addAI() {
        return new NetworkMessage(ProtocolAction.NONE);

    }

    @Override
    public NetworkMessage quit() {
        return new NetworkMessage(ProtocolAction.NONE);

    }

    @Override
    public NetworkMessage saveAndQuit() {
        return new NetworkMessage(ProtocolAction.NONE);

    }
}
