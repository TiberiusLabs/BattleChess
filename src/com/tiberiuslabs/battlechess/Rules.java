package com.tiberiuslabs.battlechess;

import java.lang.Math;
import java.util.*;

class Rules {
    private static int boardSize = 5;
    private static int outOfBounds = (2 * boardSize + 1) * (101);
    private static Set<GamePiece> cities = Collections.unmodifiableSet(new HashSet<GamePiece>() {
        {
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.BLACK, -boardSize, 0, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.BLACK, 0, -boardSize, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.BLACK, boardSize, -boardSize, Types.Color.NEUTRAL));

            add(new GamePiece(Types.UnitType.PAWN, Types.Color.WHITE, -boardSize, boardSize, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.WHITE, 0, boardSize, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.WHITE, boardSize, 0, Types.Color.NEUTRAL));
        }
    });
    private static Set<GamePiece> defaultPositions = Collections.unmodifiableSet(new HashSet<GamePiece>() {
        {
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.BLACK, -4, 4 - boardSize, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.BLACK, -3, 4 - boardSize, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.BLACK, -2, 4 - boardSize, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.BLACK, -1, 4 - boardSize, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.BLACK, 0, 4 - boardSize, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.BLACK, 1, 3 - boardSize, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.BLACK, 2, 2 - boardSize, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.BLACK, 3, 1 - boardSize, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.BLACK, 4, -boardSize, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.ROOK, Types.Color.BLACK, -3, 3 - boardSize, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.ROOK, Types.Color.BLACK, 3, -boardSize, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.KNIGHT, Types.Color.BLACK, -2, 2 - boardSize, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.KNIGHT, Types.Color.BLACK, 2, -boardSize, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.KING, Types.Color.BLACK, -1, 1 - boardSize, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.QUEEN, Types.Color.BLACK, 1, -boardSize, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.BISHOP, Types.Color.BLACK, 0, -boardSize, Types.Color.BLACK));
            add(new GamePiece(Types.UnitType.BISHOP, Types.Color.BLACK, 0, 1 - boardSize, Types.Color.WHITE));
            add(new GamePiece(Types.UnitType.BISHOP, Types.Color.BLACK, 0, 2 - boardSize, Types.Color.GREY));

            add(new GamePiece(Types.UnitType.PAWN, Types.Color.WHITE, 4, boardSize - 4, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.WHITE, 3, boardSize - 4, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.WHITE, 2, boardSize - 4, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.WHITE, 1, boardSize - 4, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.WHITE, 0, boardSize - 4, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.WHITE, -1, boardSize - 3, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.WHITE, -2, boardSize - 2, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.WHITE, -3, boardSize - 1, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.WHITE, -4, boardSize, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.ROOK, Types.Color.WHITE, 3, boardSize - 3, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.ROOK, Types.Color.WHITE, -3, boardSize, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.KNIGHT, Types.Color.WHITE, 2, boardSize - 2, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.KNIGHT, Types.Color.WHITE, -2, boardSize, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.KING, Types.Color.WHITE, 1, boardSize - 1, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.QUEEN, Types.Color.WHITE, -1, boardSize, Types.Color.NEUTRAL));
            add(new GamePiece(Types.UnitType.BISHOP, Types.Color.WHITE, 0, boardSize, Types.Color.WHITE));
            add(new GamePiece(Types.UnitType.BISHOP, Types.Color.WHITE, 0, boardSize - 1, Types.Color.BLACK));
            add(new GamePiece(Types.UnitType.BISHOP, Types.Color.WHITE, 0, boardSize - 2, Types.Color.GREY));
        }
    });
    private static Map<Integer, List<Integer>> moveAdjacencies = Collections.unmodifiableMap(new HashMap<Integer, List<Integer>>() {
        {
            for (int q = -boardSize; q <= boardSize; q++) {
                for (int r = -boardSize; r <= boardSize; r++) {
                    if (Rules.inBounds(q, r)) {
                        List<Integer> adjacent = new ArrayList<Integer>(24);
                        for (int i = 0; i < 24; i++)
                            adjacent.add(i, outOfBounds);

                        int xy = tileHash(q, r);

                        // first level ROOK adjacencies
                        if (Rules.inBounds(q, r - 1))
                            adjacent.set(0, tileHash(q, r - 1));
                        if (Rules.inBounds(q + 1, r - 1))
                            adjacent.set(1, tileHash(q + 1, r - 1));
                        if (Rules.inBounds(q + 1, r))
                            adjacent.set(2, tileHash(q + 1, r));
                        if (Rules.inBounds(q, r + 1))
                            adjacent.set(3, tileHash(q, r + 1));
                        if (Rules.inBounds(q - 1, r + 1))
                            adjacent.set(4, tileHash(q - 1, r + 1));
                        if (Rules.inBounds(q - 1, r))
                            adjacent.set(5, tileHash(q - 1, r));
                        // first level BISHOP adjacencies
                        if (Rules.inBounds(q - 1, r - 1))
                            adjacent.set(6, tileHash(q - 1, r - 1));
                        if (Rules.inBounds(q + 1, r - 2))
                            adjacent.set(7, tileHash(q + 1, r - 2));
                        if (Rules.inBounds(q + 2, r - 1))
                            adjacent.set(8, tileHash(q + 2, r - 1));
                        if (Rules.inBounds(q + 1, r + 1))
                            adjacent.set(9, tileHash(q + 1, r + 1));
                        if (Rules.inBounds(q - 1, r + 2))
                            adjacent.set(10, tileHash(q - 1, r + 2));
                        if (Rules.inBounds(q - 2, r + 1))
                            adjacent.set(11, tileHash(q - 2, r + 1));
                        // all KNIGHT adjacencies
                        if (Rules.inBounds(q - 2, r - 1))
                            adjacent.set(12, tileHash(q - 2, r - 1));
                        if (Rules.inBounds(q - 1, r - 2))
                            adjacent.set(13, tileHash(q - 1, r - 2));
                        if (Rules.inBounds(q + 1, r - 3))
                            adjacent.set(14, tileHash(q + 1, r - 3));
                        if (Rules.inBounds(q + 2, r - 3))
                            adjacent.set(15, tileHash(q + 2, r - 3));
                        if (Rules.inBounds(q + 3, r - 2))
                            adjacent.set(16, tileHash(q + 3, r - 2));
                        if (Rules.inBounds(q + 3, r - 1))
                            adjacent.set(17, tileHash(q + 3, r - 1));
                        if (Rules.inBounds(q + 2, r + 1))
                            adjacent.set(18, tileHash(q + 2, r + 1));
                        if (Rules.inBounds(q + 1, r + 2))
                            adjacent.set(19, tileHash(q + 1, r + 2));
                        if (Rules.inBounds(q - 1, r + 3))
                            adjacent.set(20, tileHash(q - 1, r + 3));
                        if (Rules.inBounds(q - 2, r + 3))
                            adjacent.set(21, tileHash(q - 2, r + 3));
                        if (Rules.inBounds(q - 3, r + 2))
                            adjacent.set(22, tileHash(q - 3, r + 2));
                        if (Rules.inBounds(q - 3, r + 1))
                            adjacent.set(23, tileHash(q - 3, r + 1));

                        put(xy, Collections.unmodifiableList(adjacent));
                    }
                }
            }
        }
    });
    public static Map<Integer, Types.Color> tiles = Collections.unmodifiableMap(new TreeMap<Integer, Types.Color>(){
        {
            int color = 1;
            for (int x = -boardSize; x <= boardSize; x++) {
                for (int y = -boardSize; y <= boardSize; y++) {
                    if (inBounds(x, y)) {
                        switch (color) {
                            case 0:
                                put(tileHash(x, y), Types.Color.WHITE);
                                break;
                            case 1:
                                put(tileHash(x, y), Types.Color.GREY);
                                break;
                            case 2:
                                put(tileHash(x, y), Types.Color.BLACK);
                                break;
                            default:
                                put(tileHash(x, y), Types.Color.NEUTRAL);
                                break;
                        }
                    }

                    color = ++color % 3;
                }
            }
        }
    });

    private static int tileHash(int x, int y) {
        return (x + boardSize + 1) * 100 + y + boardSize + 1;
    }

    private static int tileHashX(int xy) {
        return (xy / 100) - boardSize - 1;
    }

    private static int tileHashY(int xy) {
        return (xy % 100) - boardSize - 1;
    }

    public static Set<GamePiece> initialSetup() {
        Set<GamePiece> pieces = new HashSet<GamePiece>();
        pieces.addAll(defaultPositions);
        return pieces;
    }
    
    public static boolean inBounds(int x, int y) {
        return (Math.abs(x + y) <= 5);
    }

    public static boolean validMove(GamePiece p, int x, int y) {
        switch (p.unitType) {
            case PAWN: {
                int dir = p.playerColor == Types.Color.BLACK ? 1 : -1;
                if (p.x == x && ((p.y + dir) == y || !p.hasMoved && (p.y + 2*dir) == y)) {
                    return true;
                }
                break;
            }
            case ROOK: {
                int startPos = tileHash(p.x, p.y);
                int finalPos = tileHash(x, y);

                for (int i = 0; i < 6; i++) {
                    int currPos = startPos;
                    while (inBounds(tileHashX(currPos), tileHashY(currPos))) {
                        currPos = moveAdjacencies.get(currPos).get(i);

                        if (currPos == finalPos) {
                            return true;
                        }
                    }
                }
                break;
            }
            case BISHOP: {
                int startPos = tileHash(p.x, p.y);
                int finalPos = tileHash(x, y);

                for (int i = 6; i < 12; i++) {
                    int currPos = startPos;
                    while (inBounds(tileHashX(currPos), tileHashY(currPos))) {
                        currPos = moveAdjacencies.get(currPos).get(i);
                        if (currPos == finalPos) {
                            return true;
                        }
                    }
                }
                break;
            }
            case KNIGHT: {
                int startPos = tileHash(p.x, p.y);
                int finalPos = tileHash(x, y);

                for (int i = 12; i < 24; i++) {
                    if (moveAdjacencies.get(startPos).get(i) == finalPos) {
                        return true;
                    }
                }
                break;
            }
            case QUEEN: {
                int startPos = tileHash(p.x, p.y);
                int finalPos = tileHash(x, y);

                for (int i = 0; i < 12; i++) {
                    int currPos = startPos;
                    while (inBounds(tileHashX(currPos), tileHashY(currPos))) {
                        currPos = moveAdjacencies.get(currPos).get(i);
                        if (currPos == finalPos) {
                            return true;
                        }
                    }
                }
                break;
            }
            case KING: {
                int startPos = tileHash(p.x, p.y);
                int finalPos = tileHash(x, y);

                for (int i = 0; i < 12; i++) {
                    if (moveAdjacencies.get(startPos).get(i) == finalPos) {
                        return true;
                    }
                }
                break;
            }
        }
        return false;
    }

    public static boolean validAttack(GamePiece attacker, GamePiece defender) {
        if (attacker.playerColor != defender.playerColor) {
            if (attacker.unitType == Types.UnitType.PAWN) {
                int dir = attacker.playerColor == Types.Color.BLACK ? 1 : -1;
                if (attacker.x + dir == defender.x && attacker.y       == defender.y ||
                    attacker.x - dir == defender.x && attacker.y + dir == defender.y) {
                    return true;
                }
            }
            else {
                return validMove(attacker, defender.x, defender.y);
            }
        }

        return false;
    }

    public static boolean canRecruit(Set<GamePiece> citiesHeld, Types.Color playerColor) {
        Set<GamePiece> subset = playerSubset(citiesHeld, playerColor);
        int x = playerColor == Types.Color.WHITE ? boardSize : -boardSize;

        if (subset.size() == 3 && hasKing(subset, playerColor)) {
            for (GamePiece p : subset) {
                if (p.x == x && p.y == 0) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean inCity(GamePiece p) {
        for (GamePiece city : cities) {
            if (p.x == city.x && p.y == city.y) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasKing(Set<GamePiece> units, Types.Color playerColor) {
        for (GamePiece p : units) {
            if (p.playerColor == playerColor) {
                return true;
            }
        }

        return false;
    }

    public static Set<GamePiece> playerSubset(Set<GamePiece> units, Types.Color playerColor) {
        Set<GamePiece> subset = new HashSet<GamePiece>();

        for (GamePiece p : units) {
            if (p.playerColor == playerColor) {
                subset.add(p);
            }
        }

        return subset;
    }

    public static int numCities() {
        return 6;
    }

    public static boolean validRecruitment(GamePiece p, int x, int y) {
        if (p.startColor == Types.Color.NEUTRAL || p.startColor == tileColor(x, y)) {
            Set<GamePiece> playerCities = playerSubset(cities, p.playerColor);
            int finalPos = tileHash(x, y);
            for (GamePiece city : playerCities) {
                int cityPos = tileHash(city.x, city.y);
                for (int i = 0; i < 6; i++) {
                    if (finalPos == moveAdjacencies.get(cityPos).get(i)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static Types.Color tileColor(int x, int y) {
        Types.Color color = Types.Color.NEUTRAL;

        if (inBounds(x, y)) {
            color = tiles.get(tileHash(x, y));
        }

        return color;
    }

    public static Types.Color holdsCapitols(Set<GamePiece> citiesHeld) {
        Types.Color blackCapitol = Types.Color.NEUTRAL;
        Types.Color whiteCapitol = Types.Color.NEUTRAL;

        for (GamePiece p : citiesHeld) {
            if (p.x == 0) {
                if (p.y == boardSize) {
                    whiteCapitol = p.playerColor;
                }

                else if (p.y == -boardSize) {
                    blackCapitol = p.playerColor;
                }
            }
        }

        if (blackCapitol == whiteCapitol) {
            return blackCapitol;
        }

        return Types.Color.NEUTRAL;
    }
}
