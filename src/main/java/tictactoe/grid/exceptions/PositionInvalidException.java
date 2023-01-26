package tictactoe.grid.exceptions;

public class PositionInvalidException extends Exception {
    public PositionInvalidException(String position) {
        super("The position : " + position + " is not valid");
    }
}