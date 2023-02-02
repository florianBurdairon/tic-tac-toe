package tictactoe.exceptions;

/**
 * Exception class for error on protocol actions
 */
public class ProtocolActionException extends Exception {

    public ProtocolActionException() {
        super("Error : Impossible to get matching protocol action");
    }
}
