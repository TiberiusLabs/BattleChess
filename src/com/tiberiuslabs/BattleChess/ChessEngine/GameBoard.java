package com.tiberiuslabs.BattleChess.ChessEngine;

import com.sun.javafx.collections.ObservableMapWrapper;
import com.sun.javafx.collections.ObservableSetWrapper;
import com.tiberiuslabs.BattleChess.Types.Color;
import com.tiberiuslabs.BattleChess.Types.Position;
import com.tiberiuslabs.BattleChess.Types.Unit;
import com.tiberiuslabs.BattleChess.Types.UnitType;
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
public class GameBoard {
    private final ObservableMap<Position, Unit> board = new ObservableMapWrapper(new HashMap<>(Init.initBoard()));
    private final Stack<Move> moveStack = new Stack<>();

    private final Set<Unit> blackUnits = new HashSet<>();
    private final ObservableSet<Unit> blackGraveyard = new ObservableSetWrapper<>(new HashSet<Unit>());
    private boolean blackKing;
    private int numBlackUnits;

    private final Set<Unit> whiteUnits = new HashSet<>();
    private final ObservableSet<Unit> whiteGraveyard = new ObservableSetWrapper<>(new HashSet<Unit>());
    private boolean whiteKing;
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
    public Unit get(Position pos) {
        return board.get(pos);
    }

    /**
     * Gets the pos -> unit mapping that represents the board
     *
     * @return a mapping of Positions to Units
     */
    public ObservableMap<Position, Unit> getBoard() {
        return board;
    }

    /**
     * Sets the new recruit to the position given if the position is available and the recruit has come from the graveyard
     *
     * @param unit     the unit to recruit
     * @param position the position to put the recruit at
     * @return true if the unit was successfully recruited, false otherwise
     */
    public boolean set(Unit unit, Position position) {
        boolean isBlack = unit.color == Color.BLACK;
        if (isBlack ? whiteGraveyard.contains(unit) : blackGraveyard.contains(unit) &&
                board.get(position) == null) {
            board.put(position, unit);
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
     * @return the unit that was removed from play, null otherwise
     */
    public Unit move(Position startPos, Position finalPos) {
        Unit attacker = board.get(startPos);
        Unit defender = board.get(finalPos);
        moveStack.push(new Move(attacker, startPos, defender, finalPos));

        if (defender != null) {
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

        return defender;
    }

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
    public int numActiveUnits(Color player) {
        return player == Color.BLACK ? numBlackUnits : numWhiteUnits;
    }

    /**
     * Gets the set of the players active units
     *
     * @param player the player's color
     * @return the set of all the units that the player has on the board
     */
    public Set<Unit> getActiveUnits(Color player) {
        return player == Color.BLACK ? blackUnits : whiteUnits;
    }

    /**
     * Checks whether the player has an active monarch
     *
     * @param player the player's color
     * @return returns true if the player has an active monarch, false otherwise
     */
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
    public ObservableSet<Unit> getGraveyard(Color player) {
        return player == Color.BLACK ? blackGraveyard : whiteGraveyard;
    }

    public Position getPosition(Unit unit) {
        if (unit != null) {
            for (Map.Entry entry : board.entrySet()) {
                if (unit.equals(entry.getValue())) {
                    return (Position) entry.getKey();
                }
            }
        }

        return null;
    }

    public int numCitiesHeld(Color player) {
        int cities = 0;

        for (Position position : Init.cities) {
            Unit city = board.get(position);
            if (city != null && city.color == player) {
                cities += 1;
            }
        }

        return cities;
    }

    public static class Move {
        public final Unit attacker;
        public final Unit defender;
        public final Position startPos;
        public final Position finalPos;

        public Move(Unit attacker, Position startPos, Unit defender, Position finalPos) {
            this.attacker = attacker;
            this.defender = defender;
            this.startPos = startPos;
            this.finalPos = finalPos;
        }
    }
}
