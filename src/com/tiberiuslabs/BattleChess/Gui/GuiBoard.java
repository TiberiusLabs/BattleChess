package com.tiberiuslabs.BattleChess.Gui;

import com.tiberiuslabs.BattleChess.Types.Color;
import com.tiberiuslabs.BattleChess.Types.Highlight;
import com.tiberiuslabs.BattleChess.Types.Position;
import com.tiberiuslabs.BattleChess.Types.Unit;
import com.tiberiuslabs.Collections.Triple;
import javafx.collections.MapChangeListener;
import javafx.scene.Node;

import java.util.*;

/**
 * Holds the individual tiles
 *
 * @author Amandeep Gill
 */
public class GuiBoard {

    private Map<Position, BoardTile> tiles;

    public GuiBoard(BoardCallback boardCallback) {
        tiles = new HashMap<>();

        for (Position position : boardCallback.getTilePositions()) {
            BoardTile tile;
            Triple<Highlight, Unit, Color> tileInfo = boardCallback.getTileInfo(position);
            int size = 25;
            switch (tileInfo.thd) {
                case BLACK:
                    tile = new BoardTile(position, tileInfo.snd, size, javafx.scene.paint.Color.DARKBLUE);
                    break;
                case GREY:
                    tile = new BoardTile(position, tileInfo.snd, size, javafx.scene.paint.Color.CORNFLOWERBLUE);
                    break;
                case WHITE:
                    tile = new BoardTile(position, tileInfo.snd, size, javafx.scene.paint.Color.DEEPSKYBLUE);
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
            BoardTile tile = tiles.get(position);
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