package com.tiberiuslabs.BattleChess.GameEngine;

import com.sun.javafx.collections.ObservableMapWrapper;
import com.tiberiuslabs.BattleChess.ChessEngine.Rules;
import com.tiberiuslabs.BattleChess.Gui.GuiBoard;
import com.tiberiuslabs.BattleChess.Gui.RecruitMenu;
import com.tiberiuslabs.BattleChess.Types.*;
import com.tiberiuslabs.Collections.Triple;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.scene.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GameEngineCallbacks implements GuiBoard.BoardCallback, RecruitMenu.RecruitCallback {

    private GameEngine gameEngine = new GameEngine();
    private GameEngineCallbacks.AddNodeCallback addNodeCallback;
    private ObservableMap<Position, Triple<Highlight, Unit, Color>> listenerBoard;
    private ObservableSet<Unit> availableRecruits;
    private Position selectedTile;
    private Unit recruit;
    private Color playerColor;
    private Set<Position> validMoves;

    public GameEngineCallbacks(AddNodeCallback addNodeCallback) {
        this.addNodeCallback = addNodeCallback;
    }

    public void setup(Color playerColor, AIDifficulty aiDifficulty) {
        this.playerColor = playerColor;
        gameEngine.reset(playerColor, aiDifficulty);

        Map<Position, Triple<Highlight, Unit, Color>> tempListenerMap = new HashMap<>();
        Map<Position, Unit> boardMap = gameEngine.getBoard();

        for (Position pos : boardMap.keySet()) {
            tempListenerMap.put(pos, new Triple<>(Highlight.NONE, boardMap.get(pos), gameEngine.tileColor(pos)));
        }

        listenerBoard = new ObservableMapWrapper<>(tempListenerMap);

        gameEngine.getBoard().addListener((MapChangeListener<? super Position,? super Unit>) change -> {
            Unit newUnit = (Unit) (change.wasAdded() ? change.getValueAdded() : change.getValueRemoved());
            Position position = (Position) change.getKey();
            listenerBoard.put(position, new Triple<>(Highlight.NONE, newUnit, gameEngine.tileColor(position)));
        });

        gameEngine.update();
    }

    private void setHighlights() {
        if (selectedTile != null) {
            listenerBoard.put(selectedTile, new Triple<>(Highlight.SELD, gameEngine.get(selectedTile), gameEngine.tileColor(selectedTile)));
            validMoves = gameEngine.getValidMoves(selectedTile);
            for (Position pos : validMoves) {
                Unit unit = gameEngine.get(pos);
                if (unit == null) {
                    listenerBoard.put(pos, new Triple<>(Highlight.MOVE, null, gameEngine.tileColor(pos)));
                } else if (unit.color != gameEngine.get(selectedTile).color) {
                    listenerBoard.put(pos, new Triple<>(Highlight.THRT, unit, gameEngine.tileColor(pos)));
                }
            }
        }
    }

    private void resetHighlights() {
        if (listenerBoard != null) {
            for (Position pos : listenerBoard.keySet()) {
                Triple<Highlight, Unit, Color> triple = listenerBoard.get(pos);
                if (triple.fst != Highlight.NONE) {
                    listenerBoard.put(pos, new Triple<>(Highlight.NONE, triple.snd, triple.thd));
                }
            }
        }
    }

    public boolean move(Position startPos, Position finalPos) {
        boolean successful = false;
        if (startPos != null && gameEngine.makeMove(startPos, finalPos)) {
            listenerBoard.put(startPos, new Triple<>(Highlight.NONE, gameEngine.get(startPos), gameEngine.tileColor(startPos)));
            listenerBoard.put(finalPos, new Triple<>(Highlight.NONE, gameEngine.get(finalPos), gameEngine.tileColor(finalPos)));
            successful = true;
        } else if (gameEngine.makeMove(recruit, null, finalPos)) {
            listenerBoard.put(finalPos, new Triple<>(Highlight.NONE, gameEngine.get(finalPos), gameEngine.tileColor(finalPos)));
        }

        return successful;
    }

    @Override
    public void addBoardListener(MapChangeListener<Position, Triple<Highlight, Unit, Color>> listener) {
        listenerBoard.addListener(listener);
    }

    @Override
    public void makeSelection(Position position) {
        if (recruit != null && Rules.inBounds(position)) {
            if (move(null, position)) {
                recruit = null;
                // TODO: cleanup as necessary?
            } else {
                // TODO: send 'invalid recruit' error message to the player
            }
        } else if (selectedTile != null) {
            if (selectedTile.equals(position)) {
                selectedTile = null;
                resetHighlights();
            } else if (position != null && validMoves.contains(position)) {
                if (move(selectedTile, position)) {
                    selectedTile = null;
                    resetHighlights();
                } else {
                    // TODO: send 'invalid move' error message to the player
                }
            }
        } else if (gameEngine.get(position) != null && gameEngine.get(position).color == playerColor) {
            selectedTile = position;
            setHighlights();
        }
    }

    @Override
    public Set<Position> getTilePositions() {
        return listenerBoard.keySet();
    }

    @Override
    public Triple<Highlight, Unit, Color> getTileInfo(Position position) {
        return listenerBoard.get(position);
    }

    @Override
    public void addNode(Node node) {
        addNodeCallback.addNode(node);
    }

    @Override
    public void setRecruitListener(SetChangeListener<Unit> recruitListener) {
        availableRecruits.addListener(recruitListener);
    }

    public interface AddNodeCallback {
        public void addNode(Node node);
    }
}
