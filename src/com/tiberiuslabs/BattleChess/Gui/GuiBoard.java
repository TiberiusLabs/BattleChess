package com.tiberiuslabs.BattleChess.Gui;

import com.sun.javafx.collections.ObservableMapWrapper;
import com.sun.javafx.geom.Vec2f;

import com.tiberiuslabs.BattleChess.GameEngine.GameEngine;
import com.tiberiuslabs.BattleChess.Types.*;

import com.tiberiuslabs.Collections.Pair;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds the individual tiles
 * @author Amandeep Gill
 */
public class GuiBoard {
    private Vec2f center = new Vec2f(400,300);
    private int size = 10;
    private ObservableMap<Position, Unit>  board;
    private GameEngine gameEngine;

    public Map<Position, Tile> tiles;

    public GuiBoard() {
        tiles = new HashMap<>();
        gameEngine = new GameEngine(Color.BLACK, AIDifficulty.EXPERT);
        board = new ObservableMapWrapper<>(gameEngine.getBoard());

        for (Map.Entry<Position, Unit> entry : board.entrySet()) {
            Tile tile;
            switch (gameEngine.tileColor(entry.getKey())) {
                case BLACK:
                    tile = new Tile(
                            new Vec2f((float)(entry.getKey().x() * size * 1.5 + center.x), entry.getKey().y() * size * 2 + entry.getKey().x() * size + center.y),
                            size, new Image("file:res/art/board/black.png"));
                    break;
                case WHITE:
                    tile = new Tile(
                            new Vec2f((float)(entry.getKey().x() * size * 1.5 + center.x), entry.getKey().y() * size * 2 + entry.getKey().x() * size + center.y),
                            size, new Image("file:res/art/board/white.png"));
                    break;
                case GREY:
                    tile = new Tile(
                            new Vec2f((float)(entry.getKey().x() * size * 1.5 + center.y), entry.getKey().y() * size * 2 + entry.getKey().x() * size + center.y),
                            size, new Image("file:res/art/board/grey.png"));
                    break;
                default:
                    continue;
            }
            tiles.put(entry.getKey(), tile);
        }

        board.addListener((MapChangeListener<Position, Unit>) change -> {
            tiles.get(change.getKey()).setUnitPortrait(null);
        });
    }

    public boolean makeMove(Position startPos, Position finalPos, Color player) {
        Unit unit = board.get(startPos);
        return unit != null && unit.color == player && gameEngine.makeMove(unit, startPos, finalPos);
    }

    public interface BoardCallback {
        public void setBoardListener(MapChangeListener<Position, Pair<Highlight, Unit>> listener);
        public void makeSelection(Position position);
    }
}