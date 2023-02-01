package tictactoe.grid;

/**
 * interface Displayable of every components that need to be displayed
 * @author Halvick Thomas
 * @version 1.1
 */
public interface Displayable {
    /**
     * ANSI reset code
     */
    String ANSI_RESET = "\u001B[0m";
    /**
     * ANSI black code
     */
    String ANSI_BLACK = "\u001B[30m";
    /**
     * ANSI red code
     */
    String ANSI_RED = "\u001B[31m";
    /**
     * ANSI green code
     */
    String ANSI_GREEN = "\u001B[32m";
    /**
     * ANSI yellow code
     */
    String ANSI_YELLOW = "\u001B[33m";
    /**
     * ANSI blue code
     */
    String ANSI_BLUE = "\u001B[34m";
    /**
     * ANSI purple code
     */
    String ANSI_PURPLE = "\u001B[35m";
    /**
     * ANSI cyan code
     */
    String ANSI_CYAN = "\u001B[36m";
    /**
     * ANSI white code
     */
    String ANSI_WHITE = "\u001B[37m";
}
