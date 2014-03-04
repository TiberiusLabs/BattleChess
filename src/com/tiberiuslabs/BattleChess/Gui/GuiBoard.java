package com.tiberiuslabs.BattleChess.Gui;

import com.sun.javafx.collections.ObservableMapWrapper;
import com.sun.javafx.geom.Vec2f;
import com.tiberiuslabs.BattleChess.GameState.BoardState;
import com.tiberiuslabs.BattleChess.Types.AIDifficulty;
import com.tiberiuslabs.BattleChess.Types.Color;
import com.tiberiuslabs.BattleChess.Types.Position;
import com.tiberiuslabs.BattleChess.Types.Unit;
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
    private int size = 10;
    private ObservableMap<Position, Unit>  board;
    private BoardState gameState;

    public Map<Position, Tile> tiles;

    public GuiBoard() {
        tiles = new HashMap<>();
        gameState = new BoardState(Color.BLACK, AIDifficulty.EXPERT);
        board = new ObservableMapWrapper<>(gameState.getBoard());

        for (Map.Entry<Position, Unit> entry : board.entrySet()) {
            Tile tile;
            switch (gameState.tileColor(entry.getKey())) {
                case BLACK:
                    tile = new Tile(
                            new Vec2f((float)(entry.getKey().x() * size * 1.5), entry.getKey().y() * size * 2 + entry.getKey().x() * size),
                            size, new Image("file:res/art/board/black.png"));
                    break;
                case WHITE:
                    tile = new Tile(
                            new Vec2f((float)(entry.getKey().x() * size * 1.5), entry.getKey().y() * size * 2 + entry.getKey().x() * size),
                            size, new Image("file:res/art/board/white.png"));
                    break;
                case GREY:
                    tile = new Tile(
                            new Vec2f((float)(entry.getKey().x() * size * 1.5), entry.getKey().y() * size * 2 + entry.getKey().x() * size),
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
        if (unit != null && unit.color == player) {
            return gameState.makeMove(unit, startPos, finalPos);
        }

        return false;
    }
}