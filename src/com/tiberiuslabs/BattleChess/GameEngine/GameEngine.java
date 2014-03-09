package com.tiberiuslabs.BattleChess.GameEngine;

import com.tiberiuslabs.BattleChess.AI.AIEngine;
import com.tiberiuslabs.BattleChess.ChessEngine.GameBoard;
import com.tiberiuslabs.BattleChess.ChessEngine.Init;
import com.tiberiuslabs.BattleChess.ChessEngine.Rules;
import com.tiberiuslabs.BattleChess.Types.AIDifficulty;
import com.tiberiuslabs.BattleChess.Types.Color;
import com.tiberiuslabs.BattleChess.Types.Position;
import com.tiberiuslabs.BattleChess.Types.Unit;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;

import java.util.HashSet;
import java.util.Set;

/**
 * Interfaces with the player to make changes to the game state <ul> <li>makes and verifies moves/recruitment <li>gets
 * moves from AI if the player is not in 'hot seat' mode </ul>
 *
 * @author Amandeep Gill
 */
public class GameEngine {
    private GameBoard board;
    private AIEngine aiEngine;
    private Color playerColor;
    private Color currentPlayer;
    private boolean initialized;

    /**
     * Creates an uninitialized version of the game, must be later initialized with reset()
     */
    public GameEngine() {
        initialized = false;
    }

    /**
     * Initialize the Game with the player's color and the difficulty setting for the AI
     *
     * @param playerColor the color of the human player
     * @param difficulty  the difficulty setting for the AI
     */
    public GameEngine(Color playerColor, AIDifficulty difficulty) {
        this.playerColor = playerColor;
        this.aiEngine = new AIEngine(difficulty, playerColor == Color.BLACK ? Color.WHITE : Color.BLACK);
        board = new GameBoard();
        currentPlayer = Color.WHITE;
        initialized = true;
    }

    /**
     * Checks if the GameEngine has been initialized yet
     *
     * @return true if the GameEngine is initialized, false otherwise
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Reinitialize the Game with updated player color and AI difficulty setting
     *
     * @param playerColor the color of the human player
     * @param difficulty  the difficulty setting for the AI
     */
    public void reset(Color playerColor, AIDifficulty difficulty) {
        this.playerColor = playerColor;
        this.aiEngine = new AIEngine(difficulty, playerColor == Color.BLACK ? Color.WHITE : Color.BLACK);
        board = new GameBoard();
        currentPlayer = Color.WHITE;
        update();
    }

    /**
     * Updates the game state and gets the AI move if it is the AI's turn
     */
    public void update() {
        if (currentPlayer == aiEngine.getAIColor()) {
            GameBoard.Move move = aiEngine.getAIMove(new GameBoard(board));
            if (move != null) {
                if (!board.set(move.attacker, move.finalPos)) {
                    board.move(move.startPos, move.finalPos);
                }
                currentPlayer = playerColor;
            }
        }
    }

    /**
     * Test to see if the player's move is allowed
     *
     * @param unit     the unit that the player wants to move or recruit
     * @param startPos the start position of the unit (use null if recruiting)
     * @param finalPos the final position of the unit
     * @return true if the move/recruitment is valid, false otherwise
     */
    public boolean testMove(Unit unit, Position startPos, Position finalPos) {
        if (currentPlayer == aiEngine.getAIColor()) {
            update();
        }
        // return false if the player is attempting to move the AI's unit, or if the final position is not in bounds
        if (unit.color == playerColor && Rules.inBounds(finalPos)) {
            if (startPos != null) {
                return Rules.inBounds(startPos) && Rules.isValidMove(unit, startPos, finalPos, board);
            } else {
                return Rules.canRecruitUnit(playerColor, unit, finalPos, board);
            }
        }
        return false;
    }

    /**
     * Attempt to make the move/recruitment that the player is requesting
     *
     * @param unit     the unit that the player wants to move or recruit
     * @param startPos the start position of the unit (use null if recruiting)
     * @param finalPos the final position of the unit
     * @return true if the move/recruitment was successful, false otherwise
     */
    public boolean makeMove(Unit unit, Position startPos, Position finalPos) {
        if (testMove(unit, startPos, finalPos)) {
            if (startPos != null) {
                board.move(startPos, finalPos);
            } else {
                board.set(unit, finalPos);
            }
            currentPlayer = currentPlayer == Color.BLACK ? Color.WHITE : Color.BLACK;
            update();
            return true;
        }
        return false;
    }

    /**
     * Attempt to make the move that the player is requesting
     *
     * @param startPos the start position of the unit (must have a unit at this position)
     * @param finalPos the final position of the unit
     * @return true if the attempted move was successful
     */
    public boolean makeMove(Position startPos, Position finalPos) {
        Unit unit = board.get(startPos);
        if (unit != null && unit.color == playerColor) {
            if (testMove(unit, startPos, finalPos)) {
                if (startPos != null) {
                    board.move(startPos, finalPos);
                } else {
                    board.set(unit, finalPos);
                }
                currentPlayer = currentPlayer == Color.BLACK ? Color.WHITE : Color.BLACK;
                update();
                return true;
            }
        }
        return false;
    }

    /**
     * Get the set of all valid moves for the unit at the given position
     *
     * @param position the position of the unit
     * @return the set of all valid moves for the unit at the given position, if there is no unit, returns the empty set
     */
    public Set<Position> getValidMoves(Position position) {
        Set<Position> positions = new HashSet<>();

        Unit unit = board.get(position);
        if (unit != null) {
            positions = Rules.getValidMoves(unit, position, board);
        }

        return positions;
    }

    /**
     * Gets the tile color for the requested tile position
     *
     * @param position the position of the requested tile
     * @return the color of the tile at the requested position, or NEUTRAL if the position is out of bounds
     */
    public Color tileColor(Position position) {
        return Init.tileColors.get(position);
    }

    /**
     * Get the current state of the game board
     *
     * @return the current map of pos -> unit
     */
    public ObservableMap<Position, Unit> getBoard() {
        return board.getBoard();
    }

    /**
     * Gets the graveyard of the given player
     *
     * @param player the player to get the graveyard for
     * @return the player's current graveyard
     */
    public ObservableSet<Unit> getGraveyard(Color player) {
        return board.getGraveyard(player);
    }

    /**
     * Get the unit at the given position
     *
     * @param position the position of the unit
     * @return the unit at the given position or null if there is not one
     */
    public Unit get(Position position) {
        return board.get(position);
    }
}
