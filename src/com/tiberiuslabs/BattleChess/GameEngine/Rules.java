package com.tiberiuslabs.BattleChess.GameEngine;

import com.tiberiuslabs.BattleChess.Types.*;
import com.tiberiuslabs.Collections.*;
import com.tiberiuslabs.BattleChess.GameState.*;

/**
 * Created by Amandeep Gill
 *
 * Responsible for verifying the legality of moves and recruitment, as well as determining whether a final state has
 * been reached
 */
public class Rules {

    public static boolean inBounds(int x, int y) {
        return Math.abs(x-5) + Math.abs(y-5) <= 5;
    }

    public static boolean inBounds(Position pos) {
        return inBounds(pos.x(), pos.y());
    }

    public static boolean validMove(Pair<Unit, Color> unit, Position startPos, Position finalPos, Pair<Unit, Color> board[][]) {
        switch (unit.fst) {
            case Footman: {
                int dir = unit.snd == Color.BLACK ? 1 : -1;
                if (startPos.x().equals(finalPos.x()) && ((Integer)(startPos.y() + dir)).equals(finalPos.y())) {
                    return true;
                }

                else if (unit.equals(Init.defaultPositions.get(startPos)) &&
                         board[startPos.x()][startPos.y() + dir] == null &&
                         startPos.x().equals(finalPos.x()) && ((Integer)(startPos.y() + 2 * dir)).equals(finalPos.y())) {
                    return true;
                }
                break;
            }
            case Charger: {
                for (int i = 0; i < 6; i++) {
                    Position currPos = new Position(startPos);
                    while (inBounds(currPos)) {
                        currPos = Init.moveAdjacencies.get(currPos).get(i);

                        if (finalPos.equals(currPos)) {
                            return true;
                        }

                        else if (board[currPos.x()][currPos.y()] != null) {
                            break;
                        }
                    }
                }
                break;
            }
            case Assassin: {
                for (int i = 6; i < 12; i++) {
                    Position currPos = new Position(startPos);
                    while (inBounds(currPos)) {
                        currPos = Init.moveAdjacencies.get(currPos).get(i);
                        if (finalPos.equals(currPos)) {
                            return true;
                        }

                        else if (board[currPos.x()][currPos.y()] != null) {
                            break;
                        }
                    }
                }
                break;
            }
            case Calvary: {
                for (int i = 12; i < 24; i++) {
                    if (finalPos.equals(Init.moveAdjacencies.get(startPos).get(i))) {
                        return true;
                    }
                }
                break;
            }
            case Champion: {
                for (int i = 0; i < 12; i++) {
                    Position currPos = new Position(startPos);
                    while (inBounds(currPos)) {
                        currPos = Init.moveAdjacencies.get(currPos).get(i);
                        if (finalPos.equals(currPos)) {
                            return true;
                        }

                        else if (board[currPos.x()][currPos.y()] != null) {
                            break;
                        }
                    }
                }
                break;
            }
            case Monarch: {
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

}
