package tictactoe.client;


import tictactoe.NetworkMessage;
import tictactoe.ProtocolAction;
import tictactoe.grid.Grid2D;
import tictactoe.grid.Grid3D;
import tictactoe.grid.exceptions.PositionInvalidException;
import tictactoe.grid.exceptions.PositionUsedException;

import java.util.Optional;

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
    public NetworkMessage resumeGame(String[] saveList) {
        return new NetworkMessage(ProtocolAction.NONE);
    }

    @Override
    public NetworkMessage startGame(String role, String nextPlayer, String dimension, String size, String serializedGrid) {
        this.role = role;
        if(dimension.equals("3")) this.grid = new Grid3D(Integer.parseInt(size));
        else this.grid = new Grid2D(Integer.parseInt(size));

        if (this.role.equals("X")){
            return play(null);
        }
        return new NetworkMessage(ProtocolAction.WaitMessage);
    }

    @Override
    public NetworkMessage play(String posOpponent) {
        return new NetworkMessage(ProtocolAction.NONE);

    }

    @Override
    public NetworkMessage confirmation() {
        return new NetworkMessage(ProtocolAction.Confirmation);
    }

    @Override
    public NetworkMessage validate(String position) {
        try {
            grid.place(position, role.charAt(0));
        } catch (PositionUsedException e) {
            throw new RuntimeException(e);
        } catch (PositionInvalidException e) {
            throw new RuntimeException(e);
        }
        return new NetworkMessage(ProtocolAction.WaitMessage);
    }

    @Override
    public NetworkMessage endGame(String position, char role, char isDraw) {
        return new NetworkMessage(ProtocolAction.WaitMessage);
    }

    @Override
    public NetworkMessage opponentDisconnected() {
        return new NetworkMessage(ProtocolAction.NONE);
    }

    @Override
    public void quit() {}
}
