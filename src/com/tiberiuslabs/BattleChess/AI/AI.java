package com.tiberiuslabs.BattleChess.AI;

import com.tiberiuslabs.BattleChess.AI.Score.ScoreFunc;
import com.tiberiuslabs.BattleChess.ChessEngine.Board;
import com.tiberiuslabs.BattleChess.ChessEngine.Board;
import com.tiberiuslabs.BattleChess.ChessEngine.Rules;
import com.tiberiuslabs.BattleChess.Types.Color;
import com.tiberiuslabs.BattleChess.Types.Position;
import com.tiberiuslabs.BattleChess.Types.Unit;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import sun.net.www.content.text.plain;

import java.lang.reflect.Array;
import java.util.*;

import static com.tiberiuslabs.BattleChess.ChessEngine.Board.Move;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Interface between the AI engine and the game state
 *
 * @author Amandeep Gill
 */
public class AI {
    private final int numFuncs;
    private final ScoreFunc[] scoreFuncs;
    private final boolean[] useFunc;
    private final int[] weights;
    private Color color;

    /**
     * create a random set of metrics for this AI instance
     *
     * @param scoreFuncs the score function objects to use
     * @param color      the color of the AI player
     */
    public AI(ScoreFunc[] scoreFuncs, Color color) {
        this.numFuncs = scoreFuncs.length;
        this.scoreFuncs = scoreFuncs;
        this.color = color;
        this.useFunc = new boolean[numFuncs];
        this.weights = new int[numFuncs];

        Random random = new Random();
        for (int i = 0; i < numFuncs; i += 1) {
            useFunc[i] = random.nextBoolean();
            weights[i] = random.nextInt(100) + 1;
        }
    }

    /**
     * initialize the metrics that this AI will use to evaluate the next move
     *
     * @param numFuncs   the number of score functions for this AI to use
     * @param scoreFuncs the score function objects to use
     * @param useFunc    a boolean flag for whether to use a specific score function
     * @param weights    the weight to apply to each of the score functions
     * @param color      the color of the AI player
     */
    public AI(int numFuncs, ScoreFunc[] scoreFuncs, boolean[] useFunc, int[] weights, Color color) {
        this.numFuncs = numFuncs;
        this.scoreFuncs = scoreFuncs;
        this.useFunc = useFunc;
        this.weights = weights;
        this.color = color;
    }

    /**
     * Construct a new AI by combining this AI with the given AI
     *
     * @param ai the other AI to combine with this AI, if it the same AI, a random AI will be used
     * @return a new AI from the combination of this AI with the given AI
     */
    public AI generateChild(AI ai) {
        AI other = ai;
        if (this == other) {
            other = new AI(this.scoreFuncs, this.color);
        }

        Random random = new Random();
        boolean[] newUseFunc = new boolean[numFuncs];
        int[] newWeights = new int[numFuncs];
        for (int i = 0; i < numFuncs; i += 1) {
            if (this.useFunc[i] && other.useFunc[i]) {
                newUseFunc[i] = this.useFunc[i];
            } else {
                newUseFunc[i] = (random.nextBoolean() ? this.useFunc[i] : other.useFunc[i]);
            }

            int dw = this.weights[i] - other.weights[i];
            newWeights[i] = this.weights[i] + (dw / 4) * random.nextInt(5);
        }

        return new AI(this.numFuncs, this.scoreFuncs, newUseFunc, newWeights, this.color);
    }

    /**
     * Determine the best move for the AI to make
     *
     *
     * @param board a copy of the current game state
     * @return a Unit/from/to Triple reflecting the AI's move
     */
    public Move getMove(Board board) throws NoMoveException {
        AIBoard aiBoard = new AIBoard(board);
        int alpha = Integer.MIN_VALUE;
        Set<Move> moves = generateMoves(aiBoard, this.color);
        if (moves.size() < 1) {
            throw new NoMoveException();
        }
        Move maxMove = moves.iterator().next();

        for (Move move : moves) {
            assert move != null;
            if (maxMove == null) {
                maxMove = move;
            }
            if (move.startPos == null) {
                aiBoard.set(move.attacker, move.finalPos);
            } else {
                aiBoard.move(move.startPos, move.finalPos);
            }
            aiBoard.undoMove();
            int a = max(alpha, alphabeta(aiBoard, 3, alpha, Integer.MAX_VALUE, false));
            if (a > alpha) {
                alpha = a;
                maxMove = move;
            }
        }

        assert maxMove != null;
        return maxMove;
    }

    /**
     * Get the player color for this AI
     *
     * @return this AI's color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Set the AI's color
     *
     * @param color the color of the AI player
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Get the total value of the AI player given the current board state
     *
     * @param board a copy of the current game state
     * @return the sum of the score determined by the score functions
     */
    public int getScore(Board board) {
        int score = 0;
        for (int i = 0; i < numFuncs; i += 1) {
            score += useFunc[i] ? scoreFuncs[i].score(board, this.color) : 0;
        }
        return score;
    }

    /**
     * Implementation of alpha/beta pruning min/max function for determining the optimal move. <p/>
     *
     * @param board     the copy of the current game state
     * @param depth     the distance from the max depth to check
     * @param alpha     the current alpha value
     * @param beta      the current beta value
     * @param maxPlayer the boolean to determine if this level is a max or min level
     * @return the max or min value of the children, depending on maxPlayer
     * @see <a href="https://en.wikipedia.org/wiki/Alpha%E2%80%93beta_pruning">Wikipedia article on alpha/beta pruning</a>
     */
    private int alphabeta(Board board, int depth, int alpha, int beta, boolean maxPlayer) {
        Color winner = Rules.winner(board);
        if (winner != Color.NEUTRAL) {
            // check if we have reached a win state
            // return the max possible value if the AI player has won, min otherwise
            return winner == this.color ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        } else if (depth == 0) {
            // return the value of the current board state if we have hit the max depth
            return getScore(board);
        } else if (maxPlayer) {
            // return the max value of the child nodes
            for (Move move : generateMoves(board, this.color)) {
                // change the board state to reflect the current recruitment or move
                if (move.startPos == null) {
                    board.set(move.attacker, move.finalPos);
                } else {
                    board.move(move.startPos, move.finalPos);
                }

                // get the max of all the child nodes from this board state
                alpha = max(alpha, alphabeta(board, depth - 1, alpha, beta, false));

                // undo the current recruitment or move in preparation for the next sibling or for
                // returning to the parent board state
                board.undoMove();

                if (beta <= alpha) {
                    // we have hit the beta cutoff point, stop checking and move on to the next sibling board state
                    return alpha;
                }
            }

            // no nodes were pruned, return the current alpha
            return alpha;
        } else {
            // we are checking for the min player, so the moves generated need to be for that player
            Color player = this.color == Color.BLACK ? Color.WHITE : Color.BLACK;
            for (Move move : generateMoves(board, player)) {
                // change the board state to reflect the current recruitment or move
                if (move.startPos == null) {
                    board.set(move.attacker, move.finalPos);
                } else {
                    board.move(move.startPos, move.finalPos);
                }

                // get the min of all the child nodes from this board state
                beta = min(beta, alphabeta(board, depth - 1, alpha, beta, true));

                // undo the current recruitment of move in preparation for the next sibling or for
                // returning to the parent board state
                board.undoMove();

                if (beta <= alpha) {
                    // we have hit the alpha cutoff point, stop checking and move tho the next sibling board state
                    return beta;
                }
            }

            // no nodes were pruned, return the current beta
            return beta;
        }
    }

    /**
     * Generate all possible moves and recruitments for the given player
     *
     * @param board  the copy of the current game state
     * @param player the player for whom we are generating the moves
     * @return a set of all possible moves and recruitments for the player
     */
    private Set<Move> generateMoves(Board board, Color player) {
        Set<Move> moves = new HashSet<>();

        for (Unit unit : board.getActiveUnits(player)) {
            if (unit != null) {
                Position startPos = unit.position;
                for (Position finalPos : Rules.getValidMoves(unit, startPos, board)) {
                    moves.add(new Move(unit, startPos, board.get(finalPos), finalPos));
                }
            }
        }

        for (Unit recruit : board.getGraveyard(player)) {
            if (recruit != null) {
                for (Position finalPos : Rules.getValidRecruitments(player, recruit, board)) {
                    moves.add(new Move(recruit, null, null, finalPos));
                }
            }
        }

        return moves;
    }

    @Override
    public String toString() {
        return "AI{" + this.hashCode() +  ": weights=" + Arrays.toString(weights) +
               " funcs used: " + Arrays.toString(useFunc) + '}';
    }

    public class NoMoveException extends Exception {
        public NoMoveException() {
            super("No moves available");
        }
    }

}
