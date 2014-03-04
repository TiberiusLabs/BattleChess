package com.tiberiuslabs.BattleChess.GameEngine;

import com.sun.javafx.collections.ObservableMapWrapper;
import com.tiberiuslabs.BattleChess.ChessEngine.Rules;
import com.tiberiuslabs.BattleChess.Gui.GuiBoard;
import com.tiberiuslabs.BattleChess.Gui.RecruitMenu;
import com.tiberiuslabs.BattleChess.Types.*;
import com.tiberiuslabs.Collections.Pair;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

public class GameEngineCallbacks implements GuiBoard.BoardCallback, RecruitMenu.RecruitCallback {

    private static GameEngine gameEngine = new GameEngine();
    private ObservableMap<Position, Pair<Highlight, Unit>> listenerBoard;
    private ObservableSet<Unit> availableRecruits;
    private Position selectedTile;
    private Unit recruit;

    public GameEngineCallbacks(Color playerColor, AIDifficulty aiDifficulty) {
        gameEngine.reset(playerColor, aiDifficulty);

        Map<Position, Pair<Highlight, Unit>> tempListenerMap = new HashMap<>();
        Map<Position, Unit> boardMap = gameEngine.getBoard();

        for (Position pos : boardMap.keySet()) {
            tempListenerMap.put(pos, new Pair<>(Highlight.NONE, boardMap.get(pos)));
        }

        listenerBoard = new ObservableMapWrapper<>(tempListenerMap);
    }

    private void resetHighlights() {
        if (listenerBoard != null) {
            for (Position pos : listenerBoard.keySet()) {
                if (listenerBoard.get(pos).fst != Highlight.NONE) {
                    listenerBoard.put(pos, new Pair<>(Highlight.NONE, listenerBoard.get(pos).snd));
                }
            }
        }
    }

    public boolean move(Position startPos, Position finalPos) {
        boolean successful = false;
        if (startPos != null && gameEngine.makeMove(startPos, finalPos)) {
            listenerBoard.put(startPos, new Pair<>(Highlight.NONE, gameEngine.getBoard().get(startPos)));
            listenerBoard.put(finalPos, new Pair<>(Highlight.NONE, gameEngine.getBoard().get(finalPos)));
            successful = true;
        } else if (gameEngine.makeMove(recruit, null, finalPos)) {
            listenerBoard.put(finalPos, new Pair<>(Highlight.NONE, gameEngine.getBoard().get(finalPos)));
        }

        return successful;
    }

    @Override
    public void setBoardListener(MapChangeListener<Position, Pair<Highlight, Unit>> listener) {
        listenerBoard.addListener(listener);
    }

    @Override
    public void makeSelection(Position position) {
        if (selectedTile != null) {
            if (selectedTile.equals(position)) {
                selectedTile = null;
                resetHighlights();
            } else if (position != null && Rules.inBounds(position)) {
                if (move(selectedTile, position)) {
                    resetHighlights();
                } else {
                    // TODO: send 'invalid move' error message to the player
                }
            }
        } else if (recruit != null && Rules.inBounds(position)) {
            if (move(null, position)) {
                recruit = null;
                // TODO: cleanup as necessary?
            } else {
                // TODO: send 'invalid recruit' error message to the player
            }
        }
    }

    @Override
    public void setRecruitListener(EventListener recruitListener) {

    }
}
