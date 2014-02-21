package com.tiberiuslabs.BattleChess.GameEngine;

import com.tiberiuslabs.BattleChess.Types.*;
import com.tiberiuslabs.Collections.*;

import java.util.*;

/**
 * Created by Amandeep Gill
 *
 * Responsible for generating the initial game information
 */
public class Init {
    public static Map<Position, Pair<Unit, Color>> defaultPositions =
        Collections.unmodifiableMap(new HashMap<Position, Pair<Unit, Color>>() {
        {
            put(new Position(1, 9), new Pair<>(Unit.Footman,  Color.BLACK));
            put(new Position(2, 9), new Pair<>(Unit.Footman,  Color.BLACK));
            put(new Position(3, 9), new Pair<>(Unit.Footman,  Color.BLACK));
            put(new Position(4, 9), new Pair<>(Unit.Footman,  Color.BLACK));
            put(new Position(5, 9), new Pair<>(Unit.Footman,  Color.BLACK));
            put(new Position(6, 8), new Pair<>(Unit.Footman,  Color.BLACK));
            put(new Position(7, 7), new Pair<>(Unit.Footman,  Color.BLACK));
            put(new Position(8, 6), new Pair<>(Unit.Footman,  Color.BLACK));
            put(new Position(9, 5), new Pair<>(Unit.Footman,  Color.BLACK));
            put(new Position(2, 8), new Pair<>(Unit.Charger,  Color.BLACK));
            put(new Position(8, 0), new Pair<>(Unit.Charger,  Color.BLACK));
            put(new Position(3, 3), new Pair<>(Unit.Calvary,  Color.BLACK));
            put(new Position(7, 0), new Pair<>(Unit.Calvary,  Color.BLACK));
            put(new Position(4, 1), new Pair<>(Unit.Monarch,  Color.BLACK));
            put(new Position(1, 0), new Pair<>(Unit.Champion, Color.BLACK));
            put(new Position(5, 0), new Pair<>(Unit.Assassin, Color.BLACK));
            put(new Position(5, 1), new Pair<>(Unit.Assassin, Color.BLACK));
            put(new Position(5, 2), new Pair<>(Unit.Assassin, Color.BLACK));

            put(new Position(9, 9), new Pair<>(Unit.Footman,  Color.WHITE));
            put(new Position(8, 9), new Pair<>(Unit.Footman,  Color.WHITE));
            put(new Position(7, 9), new Pair<>(Unit.Footman,  Color.WHITE));
            put(new Position(6, 9), new Pair<>(Unit.Footman,  Color.WHITE));
            put(new Position(5, 9), new Pair<>(Unit.Footman,  Color.WHITE));
            put(new Position(4, 8), new Pair<>(Unit.Footman,  Color.WHITE));
            put(new Position(3, 7), new Pair<>(Unit.Footman,  Color.WHITE));
            put(new Position(2, 6), new Pair<>(Unit.Footman,  Color.WHITE));
            put(new Position(1, 5), new Pair<>(Unit.Footman,  Color.WHITE));
            put(new Position(8, 7), new Pair<>(Unit.Charger,  Color.WHITE));
            put(new Position(2,10), new Pair<>(Unit.Charger,  Color.WHITE));
            put(new Position(7, 8), new Pair<>(Unit.Calvary,  Color.WHITE));
            put(new Position(3,10), new Pair<>(Unit.Calvary,  Color.WHITE));
            put(new Position(6, 9), new Pair<>(Unit.Monarch,  Color.WHITE));
            put(new Position(4,10), new Pair<>(Unit.Champion, Color.WHITE));
            put(new Position(5,10), new Pair<>(Unit.Assassin, Color.WHITE));
            put(new Position(5, 9), new Pair<>(Unit.Assassin, Color.WHITE));
            put(new Position(5, 8), new Pair<>(Unit.Assassin, Color.WHITE));
        }
    });

    public static Map<Position, List<Position>> moveAdjacencies =
        Collections.unmodifiableMap(new HashMap<Position, List<Position>>() {
        {
            for (int q = 0; q <= 10; q++) {
                for (int r = 0; r <= 10; r++) {
                    if (Rules.inBounds(q, r)) {
                        List<Position> adjacent = new ArrayList<>(24);
                        for (int i = 0; i < 24; i++)
                            adjacent.add(i, null);

                        Position pos = new Position(q, r);

                        // first level ROOK adjacencies
                        if (Rules.inBounds(q, r - 1))
                            adjacent.set(0, new Position(q, r-1));
                        if (Rules.inBounds(q + 1, r - 1))
                            adjacent.set(1, new Position(q + 1, r - 1));
                        if (Rules.inBounds(q + 1, r))
                            adjacent.set(2, new Position(q + 1, r));
                        if (Rules.inBounds(q, r + 1))
                            adjacent.set(3, new Position(q, r + 1));
                        if (Rules.inBounds(q - 1, r + 1))
                            adjacent.set(4, new Position(q - 1, r + 1));
                        if (Rules.inBounds(q - 1, r))
                            adjacent.set(5, new Position(q - 1, r));
                        // first level BISHOP adjacencies
                        if (Rules.inBounds(q - 1, r - 1))
                            adjacent.set(6, new Position(q - 1, r - 1));
                        if (Rules.inBounds(q + 1, r - 2))
                            adjacent.set(7, new Position(q + 1, r - 2));
                        if (Rules.inBounds(q + 2, r - 1))
                            adjacent.set(8, new Position(q + 2, r - 1));
                        if (Rules.inBounds(q + 1, r + 1))
                            adjacent.set(9, new Position(q + 1, r + 1));
                        if (Rules.inBounds(q - 1, r + 2))
                            adjacent.set(10, new Position(q - 1, r + 2));
                        if (Rules.inBounds(q - 2, r + 1))
                            adjacent.set(11, new Position(q - 2, r + 1));
                        // all KNIGHT adjacencies
                        if (Rules.inBounds(q - 2, r - 1))
                            adjacent.set(12, new Position(q - 2, r - 1));
                        if (Rules.inBounds(q - 1, r - 2))
                            adjacent.set(13, new Position(q - 1, r - 2));
                        if (Rules.inBounds(q + 1, r - 3))
                            adjacent.set(14, new Position(q + 1, r - 3));
                        if (Rules.inBounds(q + 2, r - 3))
                            adjacent.set(15, new Position(q + 2, r - 3));
                        if (Rules.inBounds(q + 3, r - 2))
                            adjacent.set(16, new Position(q + 3, r - 2));
                        if (Rules.inBounds(q + 3, r - 1))
                            adjacent.set(17, new Position(q + 3, r - 1));
                        if (Rules.inBounds(q + 2, r + 1))
                            adjacent.set(18, new Position(q + 2, r + 1));
                        if (Rules.inBounds(q + 1, r + 2))
                            adjacent.set(19, new Position(q + 1, r + 2));
                        if (Rules.inBounds(q - 1, r + 3))
                            adjacent.set(20, new Position(q - 1, r + 3));
                        if (Rules.inBounds(q - 2, r + 3))
                            adjacent.set(21, new Position(q - 2, r + 3));
                        if (Rules.inBounds(q - 3, r + 2))
                            adjacent.set(22, new Position(q - 3, r + 2));
                        if (Rules.inBounds(q - 3, r + 1))
                            adjacent.set(23, new Position(q - 3, r + 1));

                        put(pos, Collections.unmodifiableList(adjacent));
                    }
                }
            }
        }
    });

    public static Map<Position, Color> tileColors = Collections.unmodifiableMap(new TreeMap<Position, Color>(){
        {
            int color = 1;
            for (int x = 0; x <= 10; x++) {
                for (int y = 0; y <= 10; y++) {
                    if (Rules.inBounds(x, y)) {
                        switch (color) {
                            case 0:
                                put(new Position(x, y), Color.WHITE);
                                break;
                            case 1:
                                put(new Position(x, y), Color.GREY);
                                break;
                            case 2:
                                put(new Position(x, y), Color.BLACK);
                                break;
                            default:
                                put(new Position(x, y), Color.NEUTRAL);
                                break;
                        }
                    }

                    color = ++color % 3;
                }
            }
        }
    });

    public static Pair<Unit, Color>[][] initBoard() {
        Pair<Unit, Color>[][] board = new Pair[11][11];

        for (int x = 0; x <= 10; x++) {
            for (int y = 0; y <= 10; y++) {
                board[x][y] = null;
            }
        }

        for (Position pos : defaultPositions.keySet()) {
            board[pos.x()][pos.y()] = defaultPositions.get(pos);
        }

        return board;
    }
}
