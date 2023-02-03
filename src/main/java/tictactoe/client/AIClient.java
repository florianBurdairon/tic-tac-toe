package tictactoe.client;


import tictactoe.CustomSocket;
import tictactoe.NetworkMessage;
import tictactoe.ProtocolAction;
import tictactoe.Text;
import tictactoe.grid.Grid;
import tictactoe.grid.Grid2D;
import tictactoe.grid.Grid3D;
import tictactoe.grid.exceptions.PositionInvalidException;
import tictactoe.grid.exceptions.PositionUsedException;

import java.net.Socket;

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

    public AIClient() {
        super("127.0.0.1", 9876);
    }

    @Override
    public NetworkMessage selectDimensions() {
        String[] param = new String[2];

        param[0]="3";
        param[1]="2";

        return new NetworkMessage(ProtocolAction.AnswerDimensions,param);
    }

    @Override
    public NetworkMessage startGame(String role, String dimension, String size) {
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
        if(posOpponent!=null){
            char opponentRole;
            if(role.equals("X")) opponentRole = 'O';
            else opponentRole = 'X';
            try {
                grid.place(posOpponent, opponentRole);
            } catch (PositionUsedException e) {
                throw new RuntimeException(e);
            } catch (PositionInvalidException e) {
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
}
