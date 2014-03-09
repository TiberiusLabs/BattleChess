package com.tiberiuslabs.BattleChess.AI;

import com.tiberiuslabs.BattleChess.AI.Score.ScoreFunc;
import com.tiberiuslabs.BattleChess.AI.Score.ScoreFuncFactory;
import com.tiberiuslabs.BattleChess.ChessEngine.GameBoard;
import com.tiberiuslabs.BattleChess.Types.AIDifficulty;
import com.tiberiuslabs.BattleChess.Types.Color;

/**
 * Sorts through the possible move options and selects the best one
 *
 * @author Amandeep Gill
 */
public class AIEngine {
    private AI ai;

    /**
     * Initialize the AI with the given difficulty setting and player color
     *
     * @param level the difficulty setting for the AI
     * @param color the player color for the AI
     */
    public AIEngine(AIDifficulty level, Color color) {
        // currently only generates a random AI
        this.ai = new AI(ScoreFuncFactory.buildScoreFuncs(color), color);
    }

    /**
     * Get the player color of the AI
     *
     * @return the AI's player color
     */
    public Color getAIColor() {
        return ai.getColor();
    }

    /**
     * Gets the AI's move.
     *
     * @param board the current game state, must not be null. Makes a copy to ensure that the game state is not
     *              changed while the AI calculates the best move to make
     * @return a Move instance representing what the AI sees as the best move for it to make
     * @see com.tiberiuslabs.BattleChess.ChessEngine.GameBoard.Move
     */
    public GameBoard.Move getAIMove(GameBoard board) {
        return ai.getMove(new GameBoard(board));
    }
}
