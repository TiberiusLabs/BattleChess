package com.tiberiuslabs.BattleChess.GameEngine;

import com.tiberiuslabs.BattleChess.Types.*;
import com.tiberiuslabs.Collections.*;

import java.util.*;
/**
 * @author Amandeep Gill
 * <p/>
 * Responsible for verifying the legality of moves and recruitment, as well as determining whether a final state has
 * been reached
 */
public class Rules {

    /**
     * Checks if (x,y) is a valid tile location.
     * @param x
     * @param y
     * @return true if the location is valid, false otherwise
     */
    public static boolean inBounds(int x, int y) {
        return Math.abs(x - 5) + Math.abs(y - 5) <= 5;
    }

    /**
     * Checks that pos(x,y) is a valid tile location. Performs null safety check
     * @param pos   the position to check
     * @return true if the location is valid, false otherwise
     */
    public static boolean inBounds(Position pos) {
        return pos != null && inBounds(pos.x(), pos.y());
    }

    /**
     * Checks that the attempted move is legal on a per-unit basis. If called externally, check that the final
     * position is empty.
     * @param unit      the unit that the player is attempting to move, must not be null
     * @param startPos  the starting position of the unit, must be a valid location (use inBounds to verify)
     * @param finalPos  the desired final position of the unit, must be a valid location (use inBounds to verify)
     * @param board     the current state of the game board, must be a 11x11 array of units (UnitType/Color Pairs)
     * @return          true if the move is valid, false otherwise
     */
    public static boolean validMove(Unit unit, Position startPos, Position finalPos, Map<Position, Unit> board) {
        switch (unit.unitType) {
            case Footman: {
                // black may only move in the positive y direction, white in the negative direction
                int dir = unit.color == Color.BLACK ? 1 : -1;

                // check if the final position is 'in front' of the footman
                if (startPos.x().equals(finalPos.x()) && ((Integer) (startPos.y() + dir)).equals(finalPos.y())) {
                    return true;
                }
                // check if the footman is at a default position (and is allowed a double move)
                // that the final position is not blocked by another unit
                // and check if the final position is two moves 'in front' of the footman
                else if (unit.equals(Init.defaultPositions.get(startPos)) &&
                        board.get(new Position(startPos.x(), startPos.y() + dir)) == null &&
                        startPos.x().equals(finalPos.x()) && ((Integer) (startPos.y() + 2 * dir)).equals(finalPos.y())) {
                    return true;
                }
                break;
            }
            case Charger: {
                // depth-first search each of the six cardinal directions for the final position
                for (int i = 0; i < 6; i++) {
                    Position currPos = new Position(startPos);

                    // only check until the edge of the board has been reached
                    while (inBounds(currPos)) {
                        currPos = Init.moveAdjacencies.get(currPos).get(i);

                        if (finalPos.equals(currPos)) {
                            return true;
                        }
                        // this direction is blocked, move to the next direction
                        else if (board.get(currPos) != null) {
                            break;
                        }
                    }
                }
                break;
            }
            case Assassin: {
                // depth-first search each of the six vertical directions for the final position
                for (int i = 6; i < 12; i++) {
                    Position currPos = new Position(startPos);

                    // only check unit the edge of the board has been reached
                    while (inBounds(currPos)) {
                        currPos = Init.moveAdjacencies.get(currPos).get(i);

                        if (finalPos.equals(currPos)) {
                            return true;
                        }
                        // this direction is blocked, move to the next direction
                        else if (board.get(currPos) != null) {
                            break;
                        }
                    }
                }
                break;
            }
            case Calvary: {
                // check each of the single-move 'jump' adjacencies for the final position
                List<Position> adjacencies = Init.moveAdjacencies.get(startPos);
                for (int i = 12; i < 24; i++) {
                    if (finalPos.equals(adjacencies.get(i))) {
                        return true;
                    }
                }
                break;
            }
            case Champion: {
                // depth-first search on the six cardinal and vertical directions for the final position
                for (int i = 0; i < 12; i++) {
                    Position currPos = new Position(startPos);

                    // only check until the edge of the board has been reached
                    while (inBounds(currPos)) {
                        currPos = Init.moveAdjacencies.get(currPos).get(i);

                        if (finalPos.equals(currPos)) {
                            return true;
                        }
                        // this direction is blocked, move to the next direction
                        else if (board.get(currPos) != null) {
                            break;
                        }
                    }
                }
                break;
            }
            case Monarch: {
                // check each of the single-move cardinal and vertical adjacencies for the final position
                List<Position> adjacencies = Init.moveAdjacencies.get(startPos);
                for (int i = 0; i < 12; i++) {
                    if (finalPos.equals(Init.moveAdjacencies.get(startPos).get(i))) {
                        return true;
                    }
                }
                break;
            }
        }
        return false;
    }

    /**
     * Checks that the attack is valid. This is calculated directly for the Footman, while attacks for other units are
     * valid if the defender's position is a valid move.
     * @param attacker      the unit that the player is using to capture the defending unit, must not be null
     * @param attackerPos   the position of the attacking unit, must be a valid location (use inBounds to verify)
     * @param defender      the unit that the player is attempting to capture, must not be null
     * @param defenderPos   the position of the defending unit, must be a valid location (use inBounds to verify)
     * @param board         the current state of the game board, must be a 11x11 array of units (UnitType/Color Pairs)
     * @return              returns true if the attack is valid, false otherwise
     */
    public static boolean validAttack(Unit attacker, Position attackerPos, Unit defender, Position defenderPos, Map<Position, Unit> board) {
        if (attacker.color != defender.color) {
            // a player may not attack their own units
            if (attacker.unitType == UnitType.Footman) {
                int dir = attacker.color == Color.BLACK ? 1 : -1;
                // special check for the Footman since they do not attack along their move paths
                if (((Integer) (attackerPos.x() + dir)).equals(defenderPos.x()) &&
                        attackerPos.y().equals(defenderPos.y())) {
                    return true;
                } else if (((Integer) (attackerPos.x() - dir)).equals(defenderPos.x()) &&
                        ((Integer) (attackerPos.y() + dir)).equals(defenderPos.y())) {
                    return true;
                }
            }
            else {
                // for all other units, the attack is valid if the move is valid
                return validMove(attacker, attackerPos, defenderPos, board);
            }
        }

        return false;
    }
}
