package com.tiberiuslabs.BattleChess.AI.Score;

import com.tiberiuslabs.BattleChess.GameState.GameBoard;
import com.tiberiuslabs.BattleChess.Types.Position;
import com.tiberiuslabs.BattleChess.Types.Unit;

/**
 * interface for defining the score function callbacks
 * @author Amandeep Gill
 */
public interface ScoreFunc {
    /**
     * Generate a score based on the state of the game that the AI will use to make a move/recruitment decision.
     * @param board     the current game state
     * @param unit      the unit to move or recruit
     * @param finalPos  the final position to move the unit to
     * @return  returns an int value dependant on the score metric being measured
     */
    public int score(GameBoard board, Unit unit, Position finalPos);
}
