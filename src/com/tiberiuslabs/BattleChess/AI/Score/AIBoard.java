package com.tiberiuslabs.BattleChess.AI.Score;

import com.tiberiuslabs.BattleChess.ChessEngine.Board;
import com.tiberiuslabs.BattleChess.Types.Color;
import com.tiberiuslabs.BattleChess.Types.Position;
import com.tiberiuslabs.BattleChess.Types.Unit;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;

import java.util.Set;

/**
 * Implement the Board interface using arrays instead of maps to better optimize generating AI moves
 */
public class AIBoard implements Board {

    private final Unit[][] board = new Unit[11][11];
    private final Set<Unit> blackUnits;
    private final Set<Unit> whiteUnits;
    private final Set<Unit> blackGraveyard;
    private final

    @Override
    public Unit get(Position position) {
        return null;
    }

    @Override
    public void move(Position startPos, Position finalPos) {

    }

    @Override
    public boolean set(Unit unit, Position startPos) {
        return false;
    }

    @Override
    public void undoMove() {

    }

    @Override
    public int numActiveUnits(Color player) {
        return 0;
    }

    @Override
    public Set<Unit> getActiveUnits(Color player) {
        return null;
    }

    @Override
    public boolean hasKing(Color player) {
        return false;
    }

    @Override
    public ObservableSet<Unit> getGraveyard(Color player) {
        return null;
    }

    @Override
    public int numCitiesHeld(Color player) {
        return 0;
    }

    @Override
    public ObservableMap<Position, Unit> getBoard() {
        return null;
    }
}


