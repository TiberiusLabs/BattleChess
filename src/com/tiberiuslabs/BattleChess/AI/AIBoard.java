package com.tiberiuslabs.BattleChess.AI;

import com.tiberiuslabs.BattleChess.ChessEngine.Board;
import com.tiberiuslabs.BattleChess.ChessEngine.Init;
import com.tiberiuslabs.BattleChess.ChessEngine.Move;
import com.tiberiuslabs.BattleChess.ChessEngine.Rules;
import com.tiberiuslabs.BattleChess.Types.Color;
import com.tiberiuslabs.BattleChess.Types.Position;
import com.tiberiuslabs.BattleChess.Types.Unit;
import com.tiberiuslabs.BattleChess.Types.UnitType;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

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
                this.board[r][c] = board.get(new Position(r - 5, c - 5));
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

    /**
     * does nothing since the AI uses the Move class for move generation
     *
     * @param startPos ignored
     * @param finalPos ignored
     */
    @Override
    public void move(Position startPos, Position finalPos) {
    }

    @Override
    public void makeMove(Move move) {
        board[move.finalPos.x() + 5][move.finalPos.y() + 5] = move.attacker;
        switch (move.moveType) {
            case MOV:
                board[move.startPos.x() + 5][move.startPos.y() + 5] = null;
                break;
            case ATK:
                board[move.startPos.x() + 5][move.startPos.y() + 5] = null;
                activeUnits.get(move.defender.color).remove(move.defender);
                deadUnits.get(move.defender.color).add(move.defender);
                break;
            case REC:
                deadUnits.get(move.attacker.color).remove(move.attacker);
                activeUnits.get(move.attacker.color).add(move.attacker);
                break;
        }

        moves.push(move);
    }

    /**
     * does nothing since the AI uses the Move class for recruitment
     *
     * @param unit     ignored
     * @param startPos ignored
     * @return false
     */
    @Override
    public boolean set(Unit unit, Position startPos) {
        return false;
    }

    @Override
    public void undoMove() {
        Move lastMove = moves.empty() ? null : moves.pop();
        if (lastMove != null) {
            switch (lastMove.moveType) {
                case MOV:
                    board[lastMove.finalPos.x() + 5][lastMove.finalPos.y() + 5] = null;
                    activeUnits.get(lastMove.attacker.color).remove(lastMove.attacker);
                    deadUnits.get(lastMove.attacker.color).add(lastMove.attacker);
                    break;
                case ATK:
                    board[lastMove.startPos.x() + 5][lastMove.startPos.y() + 5] = lastMove.attacker;
                    board[lastMove.finalPos.x() + 5][lastMove.finalPos.y() + 5] = lastMove.defender;
                    deadUnits.get(lastMove.defender.color).remove(lastMove.defender);
                    activeUnits.get(lastMove.defender.color).add(lastMove.defender);
                    if (lastMove.defender.unitType == UnitType.KING) {
                        kingAlive.put(lastMove.defender.color, true);
                    }
                    break;
                case REC:
                    board[lastMove.finalPos.x() + 5][lastMove.finalPos.y() + 5] = null;
                    deadUnits.get(lastMove.attacker.color).add(lastMove.attacker);
                    activeUnits.get(lastMove.attacker.color).add(lastMove.attacker);
                    break;
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
    public Set<Unit> getGraveyard(Color player) {
        return deadUnits.get(player);
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


