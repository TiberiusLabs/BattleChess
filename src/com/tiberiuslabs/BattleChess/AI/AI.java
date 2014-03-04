package com.tiberiuslabs.BattleChess.AI;

import com.tiberiuslabs.BattleChess.AI.Score.ScoreFunc;
import com.tiberiuslabs.BattleChess.ChessEngine.GameBoard;
import com.tiberiuslabs.BattleChess.Types.Color;
import com.tiberiuslabs.BattleChess.Types.Position;
import com.tiberiuslabs.BattleChess.Types.Unit;
import com.tiberiuslabs.Collections.Pair;
import com.tiberiuslabs.Collections.Triple;

import java.util.*;

/**
 * Interface between the AI engine and the game state
 * @author Amandeep Gill
 */
public class AI {
    private final int numFuncs;
    private final ScoreFunc[] scoreFuncs;
    private final boolean[] useFunc;
    private final int[] weights;
    private final Color color;

    /**
     * initialize the metrics that this AI will use to evaluate the next move
     * @param numFuncs      the number of score functions for this AI to use
     * @param scoreFuncs    the score function objects to use
     * @param useFunc       a boolean flag for whether to use a specific score function
     * @param weights       the weight to apply to each of the score functions
     */
    public AI(int numFuncs, ScoreFunc[] scoreFuncs, boolean[] useFunc, int[] weights, Color color) {
        this.numFuncs = numFuncs;
        this.scoreFuncs = scoreFuncs;
        this.useFunc = useFunc;
        this.weights = weights;
        this.color = color;
    }

    /**
     * Determine the best move for the AI to make
     * @param board a copy of the current game state
     * @return  a Unit/from/to Triple reflecting the AI's move
     */
    public Triple<Unit, Position, Position> getMove(GameBoard board) {
        SortedSet<Pair<Triple<Unit, Position, Position>, Integer>> moves = new TreeSet<>(new Comparator<Pair<Triple<Unit, Position, Position>, Integer>>() {
            @Override
            public int compare(Pair<Triple<Unit, Position, Position>, Integer> o1, Pair<Triple<Unit, Position, Position>, Integer> o2) {
                return o2.snd.compareTo(o1.snd);
            }
        });


        return moves.first().fst;
    }

    /**
     * Get the player color for this AI
     * @return  this AI's color
     */
    public Color getColor() {
        return color;
    }
}
