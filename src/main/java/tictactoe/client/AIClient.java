package tictactoe.client;

import tictactoe.NetworkMessage;
import tictactoe.ProtocolAction;
import tictactoe.grid.Grid2D;
import tictactoe.grid.Grid3D;

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
    public NetworkMessage startGame(String role, String dimension, String size) {
        this.role = role;
        if(dimension == "3") this.grid = new Grid3D(Integer.parseInt(size));
        else this.grid = new Grid2D(Integer.parseInt(size));

        if (this.role == "X"){
            return play(null);
        }
        return new NetworkMessage(ProtocolAction.WaitMessage);
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
