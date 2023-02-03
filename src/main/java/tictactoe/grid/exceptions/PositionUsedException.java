package tictactoe.grid.exceptions;

/**
 * Exception class for error when a pawn is placed on an used position
 */
public class PositionUsedException extends Exception {
    public PositionUsedException() {
        super();
    }
}