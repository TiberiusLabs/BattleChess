package com.tiberiuslabs.BattleChess.GameEngine;

import com.tiberiuslabs.BattleChess.Types.*;

import java.util.*;

/**
 * Responsible for generating the initial game information
 * @author Amandeep Gill
 */
public class Init {
    /**
     * An immutable mapping from pos -> unit, used to setup the initial board state
     */
    public static final Map<Position, Unit> defaultPositions =
        Collections.unmodifiableMap(new HashMap<Position, Unit>() {
        {
            put(new Position(1, 9), new Unit(UnitType.Footman,  Color.BLACK, 1));
            put(new Position(2, 9), new Unit(UnitType.Footman,  Color.BLACK, 2));
            put(new Position(3, 9), new Unit(UnitType.Footman,  Color.BLACK, 3));
            put(new Position(4, 9), new Unit(UnitType.Footman,  Color.BLACK, 4));
            put(new Position(5, 9), new Unit(UnitType.Footman,  Color.BLACK, 5));
            put(new Position(6, 8), new Unit(UnitType.Footman,  Color.BLACK, 6));
            put(new Position(7, 7), new Unit(UnitType.Footman,  Color.BLACK, 7));
            put(new Position(8, 6), new Unit(UnitType.Footman,  Color.BLACK, 8));
            put(new Position(9, 5), new Unit(UnitType.Footman,  Color.BLACK, 9));
            put(new Position(2, 8), new Unit(UnitType.Charger,  Color.BLACK, 1));
            put(new Position(8, 0), new Unit(UnitType.Charger,  Color.BLACK, 2));
            put(new Position(3, 3), new Unit(UnitType.Calvary,  Color.BLACK, 1));
            put(new Position(7, 0), new Unit(UnitType.Calvary,  Color.BLACK, 2));
            put(new Position(4, 1), new Unit(UnitType.Monarch,  Color.BLACK, 1));
            put(new Position(1, 0), new Unit(UnitType.Champion, Color.BLACK, 0));
            put(new Position(5, 0), new Unit(UnitType.Assassin, Color.BLACK, 1));
            put(new Position(5, 1), new Unit(UnitType.Assassin, Color.BLACK, 2));
            put(new Position(5, 2), new Unit(UnitType.Assassin, Color.BLACK, 3));

            put(new Position(9, 9), new Unit(UnitType.Footman,  Color.WHITE, 1));
            put(new Position(8, 9), new Unit(UnitType.Footman,  Color.WHITE, 2));
            put(new Position(7, 9), new Unit(UnitType.Footman,  Color.WHITE, 3));
            put(new Position(6, 9), new Unit(UnitType.Footman,  Color.WHITE, 4));
            put(new Position(5, 9), new Unit(UnitType.Footman,  Color.WHITE, 5));
            put(new Position(4, 8), new Unit(UnitType.Footman,  Color.WHITE, 6));
            put(new Position(3, 7), new Unit(UnitType.Footman,  Color.WHITE, 7));
            put(new Position(2, 6), new Unit(UnitType.Footman,  Color.WHITE, 8));
            put(new Position(1, 5), new Unit(UnitType.Footman,  Color.WHITE, 9));
            put(new Position(8, 7), new Unit(UnitType.Charger,  Color.WHITE, 1));
            put(new Position(2,10), new Unit(UnitType.Charger,  Color.WHITE, 2));
            put(new Position(7, 8), new Unit(UnitType.Calvary,  Color.WHITE, 1));
            put(new Position(3,10), new Unit(UnitType.Calvary,  Color.WHITE, 2));
            put(new Position(6, 9), new Unit(UnitType.Monarch,  Color.WHITE, 1));
            put(new Position(4,10), new Unit(UnitType.Champion, Color.WHITE, 0));
            put(new Position(5,10), new Unit(UnitType.Assassin, Color.WHITE, 1));
            put(new Position(5, 9), new Unit(UnitType.Assassin, Color.WHITE, 2));
            put(new Position(5, 8), new Unit(UnitType.Assassin, Color.WHITE, 3));
        }
    });

    /**
     * An immutable mapping of current pos -> adjacent pos, used to bypass calculation of adjacencies at runtime
     */
    public static final Map<Position, List<Position>> moveAdjacencies =
        Collections.unmodifiableMap(new HashMap<Position, List<Position>>() {
        {
            for (int q = 0; q <= 10; q++) {
                for (int r = 0; r <= 10; r++) {
                    if (Rules.inBounds(q, r)) {
                        List<Position> adjacent = new ArrayList<>(24);
                        for (int i = 0; i < 24; i++)
                            adjacent.add(i, null);

                        Position pos = new Position(q, r);

                        // first level cardinal adjacencies
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
                        // first level vertical adjacencies
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
                        // all jump adjacencies
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

    /**
     * An immutable set of pos describing the location of all six cities
     */
    public static final List<Position> cities = Collections.unmodifiableList(new ArrayList<Position>() {
        {
            add(new Position(5, 0));
            add(new Position(5, 10));
            add(new Position(0, 5));
            add(new Position(10, 0));
            add(new Position(0, 10));
            add(new Position(10, 5));
        }
    });

    /**
     * An immutable mapping of pos -> tile color, used to verify that players do not have more than one Assassin
     * on each of the tile colors
     */
    public static Map<Position, Color> tileColors = Collections.unmodifiableMap(new HashMap<Position, Color>(){
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

    /**
     * Constructs a map that has all 91 tile positions mapped to the unit that is at that tile or null if the tile is empty
     * @return a pos -> unit map with unit == null representing empty tiles
     */
    public static Map<Position, Unit> initBoard() {
        Map<Position, Unit> board = new HashMap<>(91);

        for (int x = 0; x <= 10; x++) {
            for (int y = 0; y <= 10; y++) {
                Position pos = new Position(x,y);
                if (Rules.inBounds(pos)) {
                    board.put(pos, defaultPositions.get(pos));
                }
            }
        }

        return board;
    }

}
