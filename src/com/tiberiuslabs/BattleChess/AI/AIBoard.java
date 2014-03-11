package com.tiberiuslabs.BattleChess.AI;

import com.sun.istack.internal.NotNull;
import com.sun.javafx.collections.ObservableSetWrapper;
import com.tiberiuslabs.BattleChess.ChessEngine.Board;
import com.tiberiuslabs.BattleChess.ChessEngine.Init;
import com.tiberiuslabs.BattleChess.ChessEngine.Rules;
import com.tiberiuslabs.BattleChess.Types.Color;
import com.tiberiuslabs.BattleChess.Types.Position;
import com.tiberiuslabs.BattleChess.Types.Unit;
import com.tiberiuslabs.BattleChess.Types.UnitType;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;

import java.util.*;

/**
 * Implement the Board interface using arrays instead of maps to better optimize generating AI moves
 */
public class AIBoard implements Board {

    private final Unit[][] board = new Unit[11][11];
    private final Map<Color, Set<Unit>> activeUnits = new HashMap<>();
    private final Map<Color, Set<Unit>> deadUnits = new HashMap<>();
    private final Map<Color, Boolean> kingAlive = new HashMap<>();
    private final Stack<Move> moves = new Stack<>();

    public AIBoard(Board board) {
        for (int r = 0; r < 11; r += 1) {
            for (int c = 0; c < 11; c += 1) {
                this.board[r][c] = board.get(new Position(r + 5, c + 5));
            }
        }
        activeUnits.put(Color.BLACK, new HashSet<>((board.getActiveUnits(Color.BLACK))));
        activeUnits.put(Color.WHITE, new HashSet<>((board.getActiveUnits(Color.WHITE))));
        deadUnits.put(Color.BLACK, new HashSet<>(board.getGraveyard(Color.BLACK)));
        deadUnits.put(Color.WHITE, new HashSet<>(board.getGraveyard(Color.WHITE)));
        kingAlive.put(Color.BLACK, board.hasKing(Color.BLACK));
        kingAlive.put(Color.WHITE, board.hasKing(Color.WHITE));
    }

    @Override
    public Unit get(Position position) {
        return position != null && Rules.inBounds(position) ? board[position.x() + 5][position.y() + 5] : null;
    }

    @Override
    public void move(Position startPos, Position finalPos) {
        Unit attacker = get(startPos);
        Unit defender = get(finalPos);
        if (attacker != null) {
            if (defender != null) {
                if (defender.unitType == UnitType.KING) {
                    kingAlive.put(defender.color, false);
                }
                activeUnits.get(defender.color).remove(defender);
                deadUnits.get(defender.color).add(defender);
            }

            board[finalPos.x() + 5][finalPos.y() + 5] = attacker;
            board[startPos.x() + 5][startPos.y() + 5] = null;
            attacker.position = finalPos;

            moves.add(new Move(attacker, startPos, defender, finalPos));
        }
    }

    @Override
    public boolean set(@NotNull Unit unit, @NotNull Position startPos) {
        if (deadUnits.get(unit.color).contains(unit) && get(startPos) == null) {
            board[startPos.x() + 5][startPos.y() + 5] = unit;
            deadUnits.get(unit.color).remove(unit);
            activeUnits.get(unit.color).add(unit);

            moves.add(new Move(unit, null, null, startPos));
            return true;
        }
        return false;
    }

    @Override
    public void undoMove() {
        Move lastMove = moves.empty() ? null : moves.pop();
        if (lastMove != null) {
            if (lastMove.startPos == null) {
                board[lastMove.finalPos.x() + 5][lastMove.finalPos.y() + 5] = null;
                activeUnits.get(lastMove.attacker.color).remove(lastMove.attacker);
                deadUnits.get(lastMove.attacker.color).add(lastMove.attacker);
            } else {
                board[lastMove.startPos.x() + 5][lastMove.startPos.y() + 5] = lastMove.attacker;
                board[lastMove.finalPos.x() + 5][lastMove.finalPos.y() + 5] = lastMove.defender;
                if (lastMove.defender != null) {
                    deadUnits.get(lastMove.defender.color).remove(lastMove.defender);
                    activeUnits.get(lastMove.defender.color).add(lastMove.defender);
                    if (lastMove.defender.unitType == UnitType.KING) {
                        kingAlive.put(lastMove.defender.color, true);
                    }
                }
            }
        }
    }

    @Override
    public int numActiveUnits(Color player) {
        return activeUnits.get(player).size();
    }

    @Override
    public Set<Unit> getActiveUnits(Color player) {
        return activeUnits.get(player);
    }

    @Override
    public boolean hasKing(Color player) {
        return kingAlive.get(player);
    }

    @Override
    public ObservableSet<Unit> getGraveyard(Color player) {
        return new ObservableSetWrapper<>(deadUnits.get(player));
    }

    @Override
    public int numCitiesHeld(Color player) {
        int cities = 0;

        for (Position cityPos : Init.cities) {
            Unit city = board[cityPos.x() + 5][cityPos.y() + 5];
            if (city != null && city.color == player) {
                cities += 1;
            }
        }

        return cities;
    }

    @Override
    public Unit[][] getArrayBoard() {
        return board;
    }

    @Override
    public ObservableMap<Position, Unit> getBoard() {
        return null;
    }
}


