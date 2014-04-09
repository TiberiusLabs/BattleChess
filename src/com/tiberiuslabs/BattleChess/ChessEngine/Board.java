package com.tiberiuslabs.BattleChess.ChessEngine;

import com.tiberiuslabs.BattleChess.Types.Color;
import com.tiberiuslabs.BattleChess.Types.Position;
import com.tiberiuslabs.BattleChess.Types.Unit;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.util.Set;

/**
 * Abstracts the GameBoard functions so that the internal representation can change depending on whether the AI needs
 * the board to generate the next move, or the GUI needs the board to keep the state of the game
 *
 * @author Amandeep Gill
 */
public interface Board {
    public Unit get(Position position);

    public void move(Position startPos, Position finalPos);

    public void makeMove(Move move);

    public boolean set(Unit unit, Position startPos);

    public void undoMove();

    public int numActiveUnits(Color player);

    public Set<Unit> getActiveUnits(Color player);

    public boolean hasKing(Color player);

    public Set<Unit> getGraveyard(Color player);

    public int numCitiesHeld(Color player);

    public ObservableMap<Position, Unit> getBoard();

    public Unit[][] getArrayBoard();

}
