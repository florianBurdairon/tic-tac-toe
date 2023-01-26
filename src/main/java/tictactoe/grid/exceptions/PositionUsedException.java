package tictactoe.grid.exceptions;

public class PositionUsedException extends Exception {
    public PositionUsedException(String position) {
        super("The position : " + position + " is already used");
    }
}