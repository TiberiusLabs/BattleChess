package com.tiberiuslabs.BattleChess.GameState;

import com.tiberiuslabs.BattleChess.AI.*;
import com.tiberiuslabs.BattleChess.ChessEngine.Init;
import com.tiberiuslabs.BattleChess.ChessEngine.Rules;
import com.tiberiuslabs.BattleChess.Types.AIDifficulty;
import com.tiberiuslabs.BattleChess.Types.Color;
import com.tiberiuslabs.BattleChess.Types.Position;
import com.tiberiuslabs.BattleChess.Types.Unit;
import com.tiberiuslabs.Collections.Triple;

import java.util.Map;

/**
 * Interfaces with the player to make changes to the game state
 * <ul>
 * <li>makes and verifies moves/recruitment
 * <li>gets moves from AI if the player is not in 'hot seat' mode
 * </ul>
 * @author Amandeep Gill
 */
public class GameStateInterface {
    private GameBoard board;
    private AIEngine aiEngine;
    private Color playerColor;
    private Color currentPlayer;

    /**
     * Initialize the Game with the player's color and the difficulty setting for the AI
     * @param playerColor   the color of the human player
     * @param difficulty    the difficulty setting for the AI
     */
    public GameStateInterface(Color playerColor, AIDifficulty difficulty) {
        this.playerColor = playerColor;
        this.aiEngine = new AIEngine(difficulty, playerColor == Color.BLACK ? Color.WHITE : Color.BLACK);
        board = new GameBoard();
        currentPlayer = Color.WHITE;
    }

    /**
     * Updates the game state and gets the AI move if it is the AI's turn
     */
    public void update() {
        if (currentPlayer == aiEngine.getAIColor()) {
            Triple<Unit, Position, Position> move = aiEngine.getAIMove(new GameBoard(board));
            if (!board.set(move.fst, move.thd)) {
                board.move(move.snd, move.thd);
            }
            currentPlayer = playerColor;
        }
    }

    /**
     * Test to see if the player's move is allowed
     * @param unit      the unit that the player wants to move or recruit
     * @param startPos  the start position of the unit (use null if recruiting)
     * @param finalPos  the final position of the unit
     * @return          true if the move/recruitment is valid, false otherwise
     */
    public boolean testMove(Unit unit, Position startPos, Position finalPos) {
        if (currentPlayer != playerColor) {
            update();
        }

        // return false if the player is attempting to move the AI's unit, or if the final position is not in bounds
        if (unit.color == playerColor && Rules.inBounds(finalPos)) {
            if (startPos != null) {
                return Rules.inBounds(startPos) && Rules.isValidMove(unit, startPos, finalPos, board);
            } else {
                return Rules.canRecruit(playerColor, unit, finalPos, board);
            }
        }
        return false;
    }

    /**
     * Attempt to make the move/recruitment that the player is requesting
     * @param unit      the unit that the player wants to move or recruit
     * @param startPos  the start position of the unit (use null if recruiting)
     * @param finalPos  the final position of the unit
     * @return          true if the move/recruitment was successful, false otherwise
     */
    public boolean makeMove(Unit unit, Position startPos, Position finalPos) {
        if (testMove(unit, startPos, finalPos)) {
            if (startPos != null) {
                board.move(startPos, finalPos);
            } else {
                board.set(unit, finalPos);
            }
            return true;
        }
        return false;
    }

    public Color tileColor(Position position) {
        return Init.tileColors.get(position);
    }
    public Map<Position, Unit> getBoard() {
        return board.getBoard();
    }
}
