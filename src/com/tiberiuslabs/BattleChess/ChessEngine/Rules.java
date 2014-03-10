package com.tiberiuslabs.BattleChess.ChessEngine;

import com.sun.istack.internal.NotNull;
import com.tiberiuslabs.BattleChess.Types.Color;
import com.tiberiuslabs.BattleChess.Types.Position;
import com.tiberiuslabs.BattleChess.Types.Unit;
import com.tiberiuslabs.BattleChess.Types.UnitType;
import com.tiberiuslabs.Collections.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Math.abs;

/**
 * Responsible for verifying the legality of moves and recruitment, as well as determining whether a final state has
 * been reached
 *
 * @author Amandeep Gill
 */
public final class Rules {

    /**
     * Checks if (x,y) is a valid tile location.
     *
     * @param x the column coordinate
     * @param y the row coordinate
     * @return true if the location is valid, false otherwise
     */
    public static boolean inBounds(int x, int y) {
        return abs(x) <= 5 && abs(y) <= 5 && abs(x + y) <= 5;
    }

    /**
     * Checks that pos(x,y) is a valid tile location. Performs null safety check
     *
     * @param pos the position to check
     * @return true if the location is valid, false otherwise
     */
    public static boolean inBounds(Position pos) {
        return pos != null && inBounds(pos.x(), pos.y());
    }

    /**
     * Checks that the attempted move is legal on a per-unit basis. Includes both attacking and normal moves
     *
     *
     * @param unit     the unit that the player is attempting to move, must not be null
     * @param startPos the starting position of the unit, must be a valid location (use inBounds to verify)
     * @param finalPos the desired final position of the unit, must be a valid location (use inBounds to verify)
     * @param board    the current state of the game board, must not be null
     * @return true if the move is valid, false otherwise
     */
    public static boolean isValidMove(Unit unit, Position startPos, Position finalPos, Board board) {
        return getValidMoves(unit, startPos, board).contains(finalPos);
    }

    /**
     * Get the set of all valid moves and attacks for the unit from the startPos
     *
     *
     * @param unit     the unit that the player is attempting to move, must not be null
     * @param startPos the starting position of the unit, must be a valid location (use inBounds to verify)
     * @param board    the current state of the game board, must not be null
     * @return the set containing all valid moves and attacks for the given unit at the given startPos
     */
    public static Set<Position> getValidMoves(@NotNull Unit unit, @NotNull Position startPos, @NotNull Board board) {
        if (!inBounds(startPos)) {
            return new HashSet<>();
        }

        switch (unit.unitType) {
            case PAWN: {
                return getValidPawnMoves(unit, startPos, board);
            }
            case ROOK: {
                return getValidRookMoves(unit, startPos, board);
            }
            case BISHOP: {
                return getValidBishopMoves(unit, startPos, board);
            }
            case KNIGHT: {
                return getValidKnightMoves(unit, startPos, board);
            }
            case QUEEN: {
                return getValidQueenMoves(unit, startPos, board);
            }
            case KING: {
                return getValidKingMoves(unit, startPos, board);
            }
            default: {
                return new HashSet<>();
            }
        }
    }

    private static Set<Position> getValidPawnMoves(Unit unit, Position startPos, Board board) {
        Set<Position> moves = new HashSet<>();
        // black may only move in the positive y direction, white in the negative direction
        int dir = unit.color == Color.BLACK ? 1 : -1;

        // check if the position in front of the footman is inBounds and empty
        Position front = new Position(startPos.x(), startPos.y() + dir);
        if (inBounds(front) && board.get(front) == null) {
            moves.add(front);
        }
        // check if the footman is at a default position (and is allowed a double move)
        // and that both the front and jump positions are not blocked
        Position jump = new Position(front.x(), front.y() + dir);
        if (unit.equals(Init.defaultPositions.get(startPos)) && board.get(front) == null && board.get(jump) == null) {
            moves.add(jump);
        }
        // special check for the PAWN since they do not attack along their move paths
        if (unit.unitType == UnitType.PAWN) {
            Position attack1 = new Position(startPos.x() + dir, startPos.y());
            Unit defender = board.get(attack1);
            if (inBounds(attack1) && defender != null && defender.color != unit.color) {
                moves.add(attack1);
            }

            Position attack2 = new Position(startPos.x() - dir, startPos.y() + dir);
            defender = board.get(attack2);
            if (inBounds(attack2) && defender != null && defender.color != unit.color) {
                moves.add(attack2);
            }
        }

        return moves;
    }

    private static Set<Position> getValidRookMoves(Unit unit, Position startPos, Board board) {
        Set<Position> moves = new HashSet<>();

        // depth-first search each of the six cardinal directions for open positions or enemy units
        for (int i = 0; i < 6; i++) {
            Position currPos = Init.moveAdjacencies.get(startPos).get(i);

            // only check until the edge of the board has been reached
            while (inBounds(currPos)) {
                Unit other = board.get(currPos);
                if (other == null) {
                    // this tile is empty
                    moves.add(new Position(currPos));
                } else if (unit.color != other.color) {
                    // this tile is occupied by an enemy unit. add the pos and move to the next direction
                    moves.add(new Position(currPos));
                    break;
                } else {
                    // a friendly unit is blocking this tile, move to the next direction
                    break;
                }
                currPos = Init.moveAdjacencies.get(currPos).get(i);
            }
        }

        return moves;
    }


    private static Set<Position> getValidBishopMoves(Unit unit, Position startPos, Board board) {
        Set<Position> moves = new HashSet<>();

        // depth-first search each of the six vertical directions for open positions or enemy units
        for (int i = 6; i < 12; i++) {
            Position currPos = Init.moveAdjacencies.get(startPos).get(i);

            // only check until the edge of the board has been reached
            while (inBounds(currPos)) {
                Unit other = board.get(currPos);
                if (other == null) {
                    // this tile is empty and inBounds
                    moves.add(new Position(currPos));
                } else if (other.color != unit.color) {
                    // this tile is occupied by an enemy unit, add the pos and move to the next direction
                    moves.add(new Position(currPos));
                    break;
                } else {
                    // this tile is blocked by a friendly unit, move to the next direction
                    break;
                }
                currPos = Init.moveAdjacencies.get(currPos).get(i);
            }
        }

        return moves;
    }


    private static Set<Position> getValidKnightMoves(Unit unit, Position startPos, Board board) {
        Set<Position> moves = new HashSet<>();

        // check each of the single-move 'jump' adjacencies for the final position
        List<Position> adjacencies = Init.moveAdjacencies.get(startPos);
        for (int i = 12; i < 24; i++) {
            Position pos = adjacencies.get(i);
            Unit other = board.get(pos);

            if (inBounds(pos)) {
                if (other == null) {
                    // this tile is empty and inBounds, add pos to moves
                    moves.add(pos);
                } else if (other.color != unit.color) {
                    // this tile is inBounds and occupied by an enemy unit
                    moves.add(pos);
                }
            }
        }

        return moves;
    }


    private static Set<Position> getValidQueenMoves(Unit unit, Position startPos, Board board) {
        Set<Position> moves = new HashSet<>();

        // depth-first search on the six cardinal and vertical directions for the final position
        for (int i = 0; i < 12; i++) {
            Position currPos = Init.moveAdjacencies.get(startPos).get(i);

            // only check until the edge of the board has been reached
            while (inBounds(currPos)) {
                Unit other = board.get(currPos);
                if (other == null) {
                    // this tile is empty and inBounds
                    moves.add(new Position(currPos));
                } else if (other.color != unit.color) {
                    // this tile is occupied by an enemy unit, add the pos and move to the next direction
                    moves.add(new Position(currPos));
                    break;
                } else {
                    // this tile is blocked by a friendly unit, move to the next direction
                    break;
                }
                currPos = Init.moveAdjacencies.get(currPos).get(i);

            }
        }

        return moves;
    }


    private static Set<Position> getValidKingMoves(Unit unit, Position startPos, Board board) {
        Set<Position> moves = new HashSet<>();

        // check each of the single-move cardinal and vertical adjacencies for the final position
        List<Position> adjacencies = Init.moveAdjacencies.get(startPos);
        for (int i = 0; i < 12; i++) {
            Position pos = adjacencies.get(i);
            Unit other = board.get(pos);

            if (inBounds(pos)) {
                if (other == null) {
                    // this tile is empty and inBounds, add pos to moves
                    moves.add(pos);
                } else if (other.color != unit.color) {
                    // this tile is inBounds and occupied by an enemy unit
                    moves.add(pos);
                }
            }
        }

        return moves;
    }

    /**
     * Checks whether the player can recruit the given unit to the given position. <p/>
     * Requirements for recruitment:
     * <ul>
     * <li>the recruit must be in the player's graveyard<p/>
     * <li>the player must have an active monarch on one of the cities<p/>
     * <li>the player must control their own capitol<p/>
     * <li>the player must control at least 2 other cities
     * </ul>
     *
     *
     * @param player   the player requesting recruitment
     * @param recruit  the unit that the player wishes to recruit, must not be null
     * @param position the position to place the unit on
     * @param board    the current state of the game board, must not be null
     * @return true if the player can recruit the unit, false otherwise
     */
    public static boolean canRecruitUnit(Color player, Unit recruit, Position position, Board board) {
        return getValidRecruitments(player, recruit, board).contains(position);
    }

    public static Set<Position> getValidRecruitments(Color player, Unit recruit, Board board) {
        Set<Position> positions = new HashSet<>();

        Unit capitol = board.get(Init.cities.get(player == Color.BLACK ? 0 : 1));
        if (board.hasKing(player) && board.getGraveyard(player).contains(recruit) &&
                capitol != null && capitol.color == player && board.numCitiesHeld(player) >= 3) {
            for (Position city : Init.cities) {
                for (Position adjacent : Init.moveAdjacencies.get(city).subList(0, 6)) {
                    if (adjacent != null && board.get(adjacent) == null) {
                        positions.add(adjacent);
                    }
                }
            }
        }

        return positions;
    }

    /**
     * Checks to see if either player has reached a win condition
     *
     * @param board the current state of the game board, must not be null
     * @return the color of the winning player, NEUTRAL if neither player has won
     */
    public static Color winner(Board board) {
        Pair<Unit, Unit> capitols = new Pair<>(board.get(Init.cities.get(0)), board.get(Init.cities.get(1)));
        if (capitols.fst != null && capitols.snd != null) {
            if (capitols.fst.color == capitols.snd.color) {
                return capitols.fst.color;
            }
        }
        if (board.numActiveUnits(Color.BLACK) == 0) {
            return Color.WHITE;
        }
        if (board.numActiveUnits(Color.WHITE) == 0) {
            return Color.BLACK;
        }
        return Color.NEUTRAL;
    }
}

