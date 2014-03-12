package com.tiberiuslabs.BattleChess.ChessEngine;

import com.sun.istack.internal.NotNull;
import com.sun.javafx.collections.ObservableListWrapper;
import com.sun.javafx.collections.ObservableMapWrapper;
import com.sun.javafx.collections.ObservableSetWrapper;
import com.tiberiuslabs.BattleChess.Types.Color;
import com.tiberiuslabs.BattleChess.Types.Position;
import com.tiberiuslabs.BattleChess.Types.Unit;
import com.tiberiuslabs.BattleChess.Types.UnitType;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;

import java.util.*;

/**
 * Holds the current state of the game
 * <ul>
 * <li>which units are where on the board
 * <li>which units are in the graveyard
 * <li>which player's turn it is currently
 * </ul>
 *
 * @author Amandeep Gill
 */
public class GameBoard implements Board {

    @SuppressWarnings("unchecked")
    private final ObservableMap<Position, Unit> board = new ObservableMapWrapper(new HashMap<>(Init.initBoard()));
    private final Stack<Move> moveStack = new Stack<>();

    private final Set<Unit> blackUnits = new HashSet<>();
    private final ObservableList<Unit> blackGraveyard = new ObservableListWrapper<>(new ArrayList<Unit>());
    private boolean blackKing;
    private int numBlackUnits;

    private final Set<Unit> whiteUnits = new HashSet<>();
    private final ObservableList<Unit> whiteGraveyard = new ObservableListWrapper<>(new ArrayList<Unit>());
    private boolean whiteKing;
    private int numWhiteUnits;

    /**
     * Default constructor
     */
    public GameBoard() {
        numWhiteUnits = 0;
        numBlackUnits = 0;
        for (Map.Entry entry : board.entrySet()) {
            Unit unit = (Unit) entry.getValue();
            if (unit != null) {
                unit.position = (Position) entry.getKey();
                if (unit.color == Color.BLACK) {
                    blackUnits.add(unit);
                    numBlackUnits += 1;
                } else {
                    whiteUnits.add(unit);
                    numWhiteUnits += 1;
                }
            }
        }
        blackKing = true;
        whiteKing = true;
    }

    /**
     * Copies the contents of the other GameBoard into this GameBoard
     *
     * @param other the GameBoard to get the game state information from, must not be null
     */
    public GameBoard(GameBoard other) {
        board.putAll(other.board);

        blackGraveyard.addAll(other.blackGraveyard);
        blackUnits.addAll(other.blackUnits);
        blackKing = other.blackKing;

        whiteGraveyard.addAll(other.whiteGraveyard);
        whiteUnits.addAll(other.whiteUnits);
        whiteKing = other.whiteKing;

        numBlackUnits = other.numBlackUnits;
        numWhiteUnits = other.numWhiteUnits;
    }

    /**
     * Gets the Unit at the given position
     *
     * @param pos the board position to get the Unit from
     * @return the Unit at the given position if one exists, return null if there is no Unit at the position
     * or the position is not inBounds
     */
    @Override
    public Unit get(Position pos) {
        return board.get(pos);
    }

    /**
     * Gets the pos -> unit mapping that represents the board
     *
     * @return a mapping of Positions to Units
     */
    @Override
    public ObservableMap<Position, Unit> getBoard() {
        return board;
    }

    @Override
    public Unit[][] getArrayBoard() {
        return null;
    }

    /**
     * Sets the new recruit to the position given if the position is available and the recruit has come from the graveyard
     *
     * @param unit     the unit to recruit
     * @param position the position to put the recruit at
     * @return true if the unit was successfully recruited, false otherwise
     */
    @Override
    public boolean set(@NotNull Unit unit, @NotNull Position position) {
        boolean isBlack = unit.color == Color.BLACK;
        if (isBlack ? whiteGraveyard.contains(unit) : blackGraveyard.contains(unit) && board.get(position) == null) {
            board.put(position, unit);
            unit.position = position;
            if (isBlack) {
                blackGraveyard.remove(unit);
                blackUnits.add(unit);
                blackKing = blackKing || unit.unitType == UnitType.KING;
                numBlackUnits += 1;
            } else {
                whiteGraveyard.remove(unit);
                whiteUnits.add(unit);
                whiteKing = whiteKing || unit.unitType == UnitType.KING;
                numWhiteUnits += 1;
            }

            moveStack.add(new Move(unit, null, null, position));
            return true;
        }
        return false;
    }

    /**
     * Moves the Unit at startPos to finalPos, if a unit is already at finalPos it gets removed from the board and
     * added to the graveyard. Performs no validity or sanity checks.
     *
     * @param startPos the starting position of the unit that is moving
     * @param finalPos the final position to move the unit to
     */
    @Override
    public void move(@NotNull Position startPos, @NotNull Position finalPos) {
        Unit attacker = board.get(startPos);
        Unit defender = board.get(finalPos);
        if (attacker == null) {
            return;
        }
        moveStack.push(new Move(attacker, startPos, defender, finalPos));
        attacker.position = finalPos;
        if (defender != null) {
            defender.position = null;
            if (defender.color == Color.BLACK) {
                blackGraveyard.add(defender);
                blackUnits.remove(defender);
                numBlackUnits -= 1;
                blackKing = !(defender.unitType == UnitType.KING);
            } else {
                whiteGraveyard.add(defender);
                whiteUnits.remove(defender);
                numWhiteUnits -= 1;
                whiteKing = !(defender.unitType == UnitType.KING);
            }
        }

        board.put(startPos, null);
        board.put(finalPos, attacker);
    }

    /**
     * Reverses the last move or recruitment that was made
     */
    @Override
    public void undoMove() {
        if (!moveStack.empty()) {
            Move lastMove = moveStack.pop();
            if (lastMove.startPos == null) {
                if (lastMove.attacker.color == Color.BLACK) {
                    blackGraveyard.add(lastMove.attacker);
                    blackUnits.remove(lastMove.attacker);
                    numBlackUnits -= 1;
                    blackKing = !(lastMove.attacker.unitType == UnitType.KING);
                } else {
                    whiteGraveyard.add(lastMove.attacker);
                    whiteUnits.remove(lastMove.attacker);
                    numWhiteUnits -= 1;
                    whiteKing = !(lastMove.attacker.unitType == UnitType.KING);
                }
                board.put(lastMove.finalPos, lastMove.attacker);
                return;
            } else if (lastMove.defender != null) {
                lastMove.defender.position = lastMove.finalPos;
                if (lastMove.defender.color == Color.BLACK) {
                    blackGraveyard.remove(lastMove.defender);
                    blackUnits.add(lastMove.defender);
                    numBlackUnits += 1;
                    blackKing = lastMove.defender.unitType == UnitType.KING;
                } else {
                    whiteGraveyard.remove(lastMove.defender);
                    whiteUnits.add(lastMove.defender);
                    numWhiteUnits += 1;
                    whiteKing = lastMove.defender.unitType == UnitType.KING;
                }
            }

            lastMove.attacker.position = lastMove.startPos;
            board.put(lastMove.startPos, lastMove.attacker);
            board.put(lastMove.finalPos, lastMove.defender);
        }
    }

    /**
     * Gets the number of units that the player has on the board
     *
     * @param player the player's color
     * @return a head count of all the player's units still alive
     */
    @Override
    public int numActiveUnits(Color player) {
        return player == Color.BLACK ? numBlackUnits : numWhiteUnits;
    }

    /**
     * Gets the set of the players active units
     *
     * @param player the player's color
     * @return the set of all the units that the player has on the board
     */
    @Override
    public Set<Unit> getActiveUnits(Color player) {
        return player == Color.BLACK ? blackUnits : whiteUnits;
    }

    /**
     * Checks whether the player has an active monarch
     *
     * @param player the player's color
     * @return returns true if the player has an active monarch, false otherwise
     */
    @Override
    public boolean hasKing(Color player) {
        return player == Color.BLACK ? blackKing : whiteKing;
    }

    /**
     * Gets a copy of the player's current graveyard
     *
     * @param player the player's color
     * @return the copy of the player's graveyard, returns the white player's graveyard if the player color
     * is not black
     */
    @Override
    public ObservableList<Unit> getGraveyard(Color player) {
        return player == Color.BLACK ? blackGraveyard : whiteGraveyard;
    }

    /**
     * Get the number of cities that the given player currently holds
     *
     * @param player the color of the player to check
     * @return the number of cities that the player has a unit on, the unit must be an "officer" or non-pawn unit
     */
    @Override
    public int numCitiesHeld(Color player) {
        int cities = 0;

        for (Position position : Init.cities) {
            Unit city = board.get(position);
            if (city != null && city.color == player && city.unitType != UnitType.PAWN) {
                cities += 1;
            }
        }

        return cities;
    }


}
