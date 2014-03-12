package com.tiberiuslabs.BattleChess.Gui;

import com.tiberiuslabs.BattleChess.Types.Color;
import com.tiberiuslabs.BattleChess.Types.Highlight;
import com.tiberiuslabs.BattleChess.Types.Position;
import com.tiberiuslabs.BattleChess.Types.Unit;
import com.tiberiuslabs.Collections.Triple;
import javafx.collections.MapChangeListener;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;

import java.util.*;

/**
 * Holds the individual tiles
 *
 * @author Amandeep Gill
 */
public class GuiBoard {

    private Map<Position, Tile> tiles;

    public GuiBoard(BoardCallback boardCallback) {
        tiles = new HashMap<>();

        for (Position position : boardCallback.getTilePositions()) {
            Tile tile;
            Triple<Highlight, Unit, Color> tileInfo = boardCallback.getTileInfo(position);
            int size = 25;
            switch (tileInfo.thd) {
                case BLACK:
                    tile = new Tile(position, tileInfo.snd, size, javafx.scene.paint.Color.BLACK);
                    break;
                case WHITE:
                    tile = new Tile(position, tileInfo.snd, size, javafx.scene.paint.Color.WHITE);
                    break;
                case GREY:
                    tile = new Tile(position, tileInfo.snd, size, javafx.scene.paint.Color.GRAY);
                    break;
                default:
                    continue;
            }
            tile.addSelectionListener(boardCallback::makeSelection);
            boardCallback.addNode(tile.getGroup());
            tile.repaint();
            tiles.put(position, tile);
        }

        boardCallback.addBoardListener(change -> {
            Position position = change.getKey();
            Triple<Highlight, Unit, Color> newTileInfo = change.getValueAdded();
            Tile tile = tiles.get(position);
            tile.setHighlight(newTileInfo.fst);
            tile.setUnit(newTileInfo.snd);
            tile.repaint();
        });
    }

    public interface BoardCallback {
        public void addBoardListener(MapChangeListener<Position, Triple<Highlight, Unit, Color>> listener);

        public void makeSelection(Position position);

        public Set<Position> getTilePositions();

        public Triple<Highlight, Unit, Color> getTileInfo(Position position);

        void addNode(Node node);
    }
}