package com.tiberiuslabs.BattleChess.GameState;

import com.tiberiuslabs.BattleChess.GameEngine.Init;
import com.tiberiuslabs.BattleChess.Types.Color;
import com.tiberiuslabs.BattleChess.Types.Position;
import com.tiberiuslabs.BattleChess.Types.Unit;
import com.tiberiuslabs.BattleChess.Types.UnitType;

import java.util.*;

/**
 * @author Amandeep Gill
 *
 * Holds the current state of the game
 *  - which units are where on the board
 *  - which units are in the graveyard
 *  - which player's turn it is currently
 */
public class GameBoard {
    private static List<Position> cities = Init.cities;
    private final Map<Position, Unit> board = Init.initBoard();

    private final Set<Unit> blackUnits = new HashSet<>();
    private final Set<Unit> blackGraveyard = new HashSet<>();
    private boolean blackMonarch;
    private int numBlackUnits;

    private final Set<Unit> whiteUnits = new HashSet<>();
    private final Set<Unit> whiteGraveyard = new HashSet<>();
    private boolean whiteMonarch;
    private int numWhiteUnits;

    /**
     * Default constructor
     */
    public GameBoard() {
        numWhiteUnits = 0;
        numBlackUnits = 0;
        for (Unit unit : board.values()) {
            if (unit != null) {
                if (unit.color == Color.BLACK) {
                    blackUnits.add(unit);
                    numBlackUnits++;
                } else {
                    whiteUnits.add(unit);
                    numWhiteUnits++;
                }
            }
        }
        blackMonarch = true;
        whiteMonarch = true;
    }

    /**
     * Copies the contents of the other GameBoard into this GameBoard
     * @param other the GameBoard to get the game state information from, must not be null
     */
    public GameBoard(GameBoard other) {
        board.putAll(other.board);

        blackGraveyard.addAll(other.blackGraveyard);
        blackMonarch = other.blackMonarch;

        whiteGraveyard.addAll(other.whiteGraveyard);
        whiteMonarch = other.whiteMonarch;

        numBlackUnits = other.numBlackUnits;
        numWhiteUnits = other.numWhiteUnits;
    }

    /**
     * Gets the Unit at the given position
     * @param pos   the board position to get the Unit from
     * @return      the Unit at the given position if one exists, return null if there is no Unit at the position
     *              or the position is not inBounds
     */
    public Unit get(Position pos) {
        return board.get(pos);
    }

    /**
     * Moves the Unit at startPos to finalPos, if a unit is already at finalPos it gets removed from the board and
     * added to the graveyard. Performs no validity or sanity checks.
     * @param startPos  the starting position of the unit that is moving
     * @param finalPos  the final position to move the unit to
     * @return          the unit that was removed from play, null otherwise
     */
    public Unit move(Position startPos, Position finalPos) {
        Unit unit = board.get(finalPos);
        if (unit != null) {
            if (unit.color == Color.BLACK) {
                blackGraveyard.add(unit);
                blackUnits.remove(unit);
                numBlackUnits--;
                blackMonarch = !(unit.unitType == UnitType.Monarch);
            } else {
                whiteGraveyard.add(unit);
                whiteUnits.remove(unit);
                numWhiteUnits--;
                whiteMonarch = !(unit.unitType == UnitType.Monarch);
            }
        }

        board.put(finalPos, board.get(startPos));
        board.put(startPos, null);

        return unit;
    }

    /**
     * Gets the number of units that the player has on the board
     * @param player    the player's color
     * @return          a head count of all the player's units still alive
     */
    public int numActiveUnits(Color player) {
        return player == Color.BLACK ? numBlackUnits : numWhiteUnits;
    }

    /**
     * Gets the set of the players active units
     * @param player    the player's color
     * @return          the set of all the units that the player has on the board
     */
    public Set<Unit> getActiveUnits(Color player) {
        return player == Color.BLACK ? blackUnits : whiteUnits;
    }

    /**
     * Gets a copy of the player's current graveyard
     * @param player    the player's color
     * @return          the copy of the player's graveyard, returns the white player's graveyard if the player color
     *                  is not black
     */
    public Set<Unit> getGraveyard(Color player) {
        return new HashSet<>(player == Color.BLACK ? blackGraveyard : whiteGraveyard);
    }
}
