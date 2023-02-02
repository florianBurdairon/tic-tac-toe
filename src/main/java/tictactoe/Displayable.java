package tictactoe;

/**
 * interface Displayable of every component that need to be displayed
 * @author Halvick Thomas
 * @version 1.1
 */
public interface Displayable {
    /**
     * ANSI reset code
     */
    static String ANSI_RESET = "\u001B[0m";
    /**
     * ANSI black code
     */
    static String ANSI_BLACK = "\u001B[30m";
    /**
     * ANSI bold red code
     */
    static String ANSI_RED = "\u001B[31;1m";
    /**
     * ANSI green code
     */
    String ANSI_GREEN = "\u001B[32m";
    /**
     * ANSI yellow code
     */
    static String ANSI_YELLOW = "\u001B[93m";
    /**
     * ANSI blue code
     */
    static String ANSI_BLUE = "\u001B[94m";
    /**
     * ANSI purple code
     */
    static String ANSI_PURPLE = "\u001B[95m";
    /**
     * ANSI cyan code
     */
    static String ANSI_CYAN = "\u001B[36m";
    /**
     * ANSI white code
     */
    static String ANSI_WHITE = "\u001B[97m";
}
