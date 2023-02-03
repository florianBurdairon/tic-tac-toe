package tictactoe.client;


import com.google.gson.Gson;
import tictactoe.network.NetworkMessage;
import tictactoe.network.ProtocolAction;
import tictactoe.Text;
import tictactoe.grid.Grid2D;
import tictactoe.grid.Grid3D;
import tictactoe.grid.exceptions.PositionInvalidException;
import tictactoe.grid.exceptions.PositionUsedException;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class AIClient extends Client{

    /**
     * Max depth use by min max
     */
    static final int MINMAX_MAX_DEPTH = 6;

    /**
     * Store the next play by the minmax algo
     */
    private int nextPlay;

    /**
     *Creates a new AI client
     */
    public AIClient() {
        super("127.0.0.1", 9876);
    }

    /**
     * Function which send a message with the grid length and his dimension to the server.
     * @return a network message
     */
    @Override
    public NetworkMessage selectDimensions() {
        String[] param = new String[2];

        param[0]="3";
        param[1]="2";

        return new NetworkMessage(ProtocolAction.AnswerDimensions,param);
    }

    /**
     *The function manages the case of the restart of a game which was in paused
     * @param saveList
     * @return
     */
    @Override
    public NetworkMessage resumeGame(String[] saveList) {
        return new NetworkMessage(ProtocolAction.NONE);
    }

    /**
     * Function on reception of "Start Game" by the server. Construct the adequate grid and get its role. If 'X', plays its turn.
     * @param role The role given by the server ('X' or 'O').
     * @param nextPlayer The next player
     * @param dimension The dimension of the grid (2D or 3D).
     * @param size The size of the grid (its width).
     * @param serializedGrid The grid which is serialized.
     * @return the message to answer to the server.
     */
    @Override
    public NetworkMessage startGame(String role, String nextPlayer, String dimension, String size, String serializedGrid) {
        this.role = role;
        if(serializedGrid != null) {
            isSavedGame = true;

            //Deserialize the json string
            Gson gson = new Gson();
            if(dimension.equals("3")) this.grid = gson.fromJson(serializedGrid, Grid3D.class);
            else this.grid = gson.fromJson(serializedGrid, Grid2D.class);
        }
        else if(dimension.equals("3")) this.grid = new Grid3D(Integer.parseInt(size));
        else this.grid = new Grid2D(Integer.parseInt(size));

        if (nextPlayer.equals(this.role)){
            return play(null);
        }
        return new NetworkMessage(ProtocolAction.WaitMessage);
    }

    /**
     * Function on reception of "Play" by the server. If posOpponent is set, add the opponent choice to the display-only grid.
     * Then, waits for user to choose its position. Finally, sends this position back to the server.
     * @param posOpponent The opponent choice (its last turn).
     * @return The new turn of this user.
     */
    @Override
    public NetworkMessage play(String posOpponent) {
        if(posOpponent!=null){
            char opponentRole;
            if(role.equals("X")) opponentRole = 'O';
            else opponentRole = 'X';
            try {
                grid.place(posOpponent, opponentRole);
            } catch (PositionUsedException | PositionInvalidException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(Text.iaPlay(this.role));

        try {
            this.minmax(MINMAX_MAX_DEPTH,true,false,Integer.MIN_VALUE,Integer.MAX_VALUE);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        String[] param = new String[2];
        if(grid instanceof Grid2D){
            param[0]= this.nextPlay+1+"";
        }
        else{
            //65 = A in ASCII code
            char z = (char)((this.nextPlay)/(this.grid.getSize()*this.grid.getSize())+65);
            String pos = this.nextPlay%(this.grid.getSize()*this.grid.getSize()) + 1 + "";
            param[0]= z + pos;
        }
        param[1]=role;
        return new NetworkMessage(ProtocolAction.Place,param);
    }

    /**
     * Minmax algorithm to choose the best (if the AI is not too stupid) move
     * @param depth the current depth
     * @param maximizing if maximizing is true we chose the best move for the AI else the best move for his opponent
     * @param win return if last move is winner or not
     * @param alpha Alpha–beta pruning to improve performances
     * @param beta Alpha–beta pruning to improve performances
     * @return a value representing the move (0 = nothing |draw, < 0 = losing, >0 = winning)
     * @throws PositionInvalidException
     * @throws PositionUsedException
     * @see <a href="https://en.wikipedia.org/wiki/Minimax">MinMax algorithm</a>
     * @see <a href="https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning">Alpha–beta pruning</a>
     */
    private int minmax(int depth, boolean maximizing,boolean win, int alpha, int beta) throws PositionInvalidException, PositionUsedException {
        if (win){
            return maximizing ? -10 : 10;
        }
        if (depth == 0 || grid.getRemainingCells() == 0) {
            return 0;
        }
        int val;
        if (maximizing) {
            val = Integer.MIN_VALUE;
            for (int x = 0; x < this.grid.getTotalSize(); x++) {
                if (this.grid.getValue(x) != '\0') continue;
                int iteration = minmax(depth - 1, false, this.grid.place(x, this.role.charAt(0)),alpha,beta);
                this.grid.setValue(x, '\0');
                if (iteration >= val) {
                    if (depth == MINMAX_MAX_DEPTH) {
                        this.nextPlay = x;
                    }
                    val = iteration;
                }
                if (val > beta) return val;
                alpha = max(alpha, val);
            }

        } else {
            val = Integer.MAX_VALUE;
            for (int x = 0; x < this.grid.getTotalSize(); x++) {
                if (this.grid.getValue(x) != '\0') continue;
                int result=  minmax(depth - 1, true,this.grid.place(x, this.role.charAt(0) == 'X' ? 'O' : 'X'),alpha,beta);
                if(result < val)val=result;
                this.grid.setValue(x,'\0');
                if (val < alpha)  return val;
                beta = min(beta, val);
            }
        }
        return val;
    }

    /**
     * Function which asks at a first time if the player confirm that he want to place his pawn in this place
     * @return a protocol action if the player answer "Oui" to the question
     */
    @Override
    public NetworkMessage confirmation() {
        return new NetworkMessage(ProtocolAction.Confirmation);
    }

    /**
     * Function which ensure at a second time if the player can place his pawn
     * @param position Position of the pawn placed
     * @return a protocol action which indicate to the player that he must wait to his next turn
     */
    @Override
    public NetworkMessage validate(String position) {
        try {
            grid.place(position, role.charAt(0));
        } catch (PositionUsedException | PositionInvalidException e) {
            throw new RuntimeException(e);
        }
        return new NetworkMessage(ProtocolAction.WaitMessage);
    }

    /**
     * Function which manage the end of the game: display the final grid and the win combinaison in case of victory
     * @param position Position of the pawn placed
     * @param role Role indicate which player plays currently
     * @param isDraw IsDraw is a variable indicating the case of an equality at the end
     * @return a protocol action to the both player
     */
    @Override
    public NetworkMessage endGame(String position, char role, char isDraw) {
        return new NetworkMessage(ProtocolAction.WaitMessage);
    }

    /**
     * Function which manage the case where an opponent player is unintentionally disconnected from the server
     * It asks the last player what he can do: quit or quit and save the game
     * @return The protocol action link to quit or quit and save
     */
    @Override
    public NetworkMessage opponentDisconnected() {
        return new NetworkMessage(ProtocolAction.NONE);
    }

    /**
     * Function which show only the endgame message
     */
    @Override
    public void quit() {}
}
