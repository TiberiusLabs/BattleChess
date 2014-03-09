package com.tiberiuslabs.BattleChess.AI.Score;

import com.tiberiuslabs.BattleChess.ChessEngine.GameBoard;
import com.tiberiuslabs.BattleChess.Types.Color;

/**
 * interface for defining the score function callbacks
 *
 * @author Amandeep Gill
 */
public interface ScoreFunc {
    /**
     * Generate a score based on the state of the game that the AI will use to make a move/recruitment decision.
     *
     *
     * @param board the current game state
     * @param player the current player to generate the score for
     * @return returns an int value dependant on the score metric being measured
     * @see com.tiberiuslabs.BattleChess.AI.Score.ScoreFuncFactory
     * @see com.tiberiuslabs.BattleChess.AI.AI
     */
    public int score(GameBoard board, Color player);
}
