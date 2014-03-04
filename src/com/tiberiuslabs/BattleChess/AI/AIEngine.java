package com.tiberiuslabs.BattleChess.AI;

import com.tiberiuslabs.BattleChess.AI.Score.ScoreFunc;
import com.tiberiuslabs.BattleChess.ChessEngine.GameBoard;
import com.tiberiuslabs.BattleChess.Types.AIDifficulty;
import com.tiberiuslabs.BattleChess.Types.Color;
import com.tiberiuslabs.BattleChess.Types.Position;
import com.tiberiuslabs.BattleChess.Types.Unit;
import com.tiberiuslabs.Collections.Triple;

/**
 * Sorts through the possible move options and selects the best one
 * @author Amandeep Gill
 */
public class AIEngine {
    private AI ai;

    /**
     * Initialize the AI with the given difficulty setting and player color
     * @param level the difficulty setting for the AI
     * @param color the player color for the AI
     */
    public AIEngine(AIDifficulty level, Color color) {
        this.ai = new AI(1, new ScoreFunc[1], new boolean[1], new int[1], color);
    }

    /**
     * Get the player color of the AI
     * @return  the AI's player color
     */
    public Color getAIColor() {
        return ai.getColor();
    }

    /**
     * Get's the AI's move.
     * @param board the current game state, must not be null. Makes a copy to ensure that the game state is not
     *              changed while the AI calculates the best move to make
     * @return      a Triple with the Unit/from/to representing the AI's move
     */
    public Triple<Unit, Position, Position> getAIMove(GameBoard board) {
        return ai.getMove(new GameBoard(board));
    }
}
