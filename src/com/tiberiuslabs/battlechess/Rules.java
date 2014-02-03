package com.tiberiuslabs.battlechess;

import java.lang.Math;
import java.util.*;

class Rules {
    private static int boardSize = 5;
    private static int outOfBounds = (2 * boardSize + 1) * (101);
    private static Set<GamePiece> cities = Collections.unmodifiableSet(new HashSet<GamePiece>() {
        {
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.BLACK, -boardSize, 0, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.BLACK, 0, -boardSize, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.BLACK, boardSize, -boardSize, Types.Color.NUETRAL));

            add(new GamePiece(Types.UnitType.PAWN, Types.Color.WHITE, -boardSize, boardSize, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.WHITE, 0, boardSize, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.WHITE, boardSize, 0, Types.Color.NUETRAL));
        }
    });
    private static Set<GamePiece> defaultPositions = Collections.unmodifiableSet(new HashSet<GamePiece>() {
        {
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.BLACK, -4, 4 - boardSize, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.BLACK, -3, 4 - boardSize, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.BLACK, -2, 4 - boardSize, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.BLACK, -1, 4 - boardSize, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.BLACK, 0, 4 - boardSize, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.BLACK, 1, 3 - boardSize, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.BLACK, 2, 2 - boardSize, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.BLACK, 3, 1 - boardSize, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.BLACK, 4, -boardSize, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.ROOK, Types.Color.BLACK, -3, 3 - boardSize, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.ROOK, Types.Color.BLACK, 3, -boardSize, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.KNIGHT, Types.Color.BLACK, -2, 2 - boardSize, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.KNIGHT, Types.Color.BLACK, 2, -boardSize, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.KING, Types.Color.BLACK, -1, 1 - boardSize, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.QUEEN, Types.Color.BLACK, 1, -boardSize, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.BISHOP, Types.Color.BLACK, 0, -boardSize, Types.Color.BLACK));
            add(new GamePiece(Types.UnitType.BISHOP, Types.Color.BLACK, 0, 1 - boardSize, Types.Color.WHITE));
            add(new GamePiece(Types.UnitType.BISHOP, Types.Color.BLACK, 0, 2 - boardSize, Types.Color.GREY));

            add(new GamePiece(Types.UnitType.PAWN, Types.Color.WHITE, 4, boardSize - 4, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.WHITE, 3, boardSize - 4, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.WHITE, 2, boardSize - 4, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.WHITE, 1, boardSize - 4, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.WHITE, 0, boardSize - 4, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.WHITE, -1, boardSize - 3, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.WHITE, -2, boardSize - 2, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.WHITE, -3, boardSize - 1, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.PAWN, Types.Color.WHITE, -4, boardSize, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.ROOK, Types.Color.WHITE, -3, boardSize - 3, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.ROOK, Types.Color.WHITE, -3, boardSize, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.KNIGHT, Types.Color.WHITE, 2, boardSize - 2, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.KNIGHT, Types.Color.WHITE, -2, boardSize, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.KING, Types.Color.WHITE, 1, boardSize - 1, Types.Color.NUETRAL));
            add(new GamePiece(Types.UnitType.QUEEN, Types.Color.WHITE, -1, boardSize, Types.Color.NUETRAL));
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

                        int x = q + boardSize + 1;
                        int y = r + boardSize + 1;
                        int xy = x * 100 + y;

                        // first level ROOK adjacencies
                        if (Rules.inBounds(q, r - 1))
                            adjacent.set(0, xy - 1);
                        if (Rules.inBounds(q + 1, r - 1))
                            adjacent.set(1, xy + 99);
                        if (Rules.inBounds(q + 1, r))
                            adjacent.set(2, xy + 100);
                        if (Rules.inBounds(q, r + 1))
                            adjacent.set(3, xy + 1);
                        if (Rules.inBounds(q - 1, r + 1))
                            adjacent.set(4, xy - 99);
                        if (Rules.inBounds(q - 1, r))
                            adjacent.set(5, xy - 100);
                        // first level BISHOP adjacencies
                        if (Rules.inBounds(q - 1, r - 1))
                            adjacent.set(6, xy - 101);
                        if (Rules.inBounds(q + 1, r - 2))
                            adjacent.set(7, xy + 98);
                        if (Rules.inBounds(q + 2, r - 1))
                            adjacent.set(8, xy + 199);
                        if (Rules.inBounds(q + 1, r + 1))
                            adjacent.set(9, xy + 101);
                        if (Rules.inBounds(q - 1, r + 2))
                            adjacent.set(10, xy - 98);
                        if (Rules.inBounds(q - 2, r + 1))
                            adjacent.set(11, xy - 199);
                        // all KNIGHT adjacencies
                        if (Rules.inBounds(q - 2, r - 1))
                            adjacent.set(12, xy - 201);
                        if (Rules.inBounds(q - 1, r - 2))
                            adjacent.set(13, xy - 102);
                        if (Rules.inBounds(q + 1, r - 3))
                            adjacent.set(14, xy + 97);
                        if (Rules.inBounds(q + 2, r - 3))
                            adjacent.set(15, xy + 197);
                        if (Rules.inBounds(q + 3, r - 2))
                            adjacent.set(16, xy + 298);
                        if (Rules.inBounds(q + 3, r - 1))
                            adjacent.set(17, xy + 299);
                        if (Rules.inBounds(q + 2, r + 1))
                            adjacent.set(18, xy + 201);
                        if (Rules.inBounds(q + 1, r + 2))
                            adjacent.set(19, xy + 102);
                        if (Rules.inBounds(q - 1, r + 3))
                            adjacent.set(20, xy - 97);
                        if (Rules.inBounds(q - 2, r + 3))
                            adjacent.set(21, xy - 197);
                        if (Rules.inBounds(q - 3, r + 2))
                            adjacent.set(22, xy - 298);
                        if (Rules.inBounds(q - 3, r + 1))
                            adjacent.set(23, xy - 299);

                        put(xy, Collections.unmodifiableList(adjacent));
                    }
                }
            }
        }
    });

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
                int startPos = p.x * 100 + p.y;
                int finalPos = x * 100 + y;

                for (int i = 0; i < 6; i++) {
                    int currPos = startPos;
                    while (inBounds(currPos/100, currPos%100)) {
                        currPos = moveAdjacencies.get(currPos).get(i);
                        if (currPos == finalPos) {
                            return true;
                        }
                    }
                }
                break;
            }
            case BISHOP: {
                int startPos = p.x * 100 + p.y;
                int finalPos = x * 100 + y;

                for (int i = 6; i < 12; i++) {
                    int currPos = startPos;
                    while (inBounds(currPos/100, currPos%100)) {
                        currPos = moveAdjacencies.get(currPos).get(i);
                        if (currPos == finalPos) {
                            return true;
                        }
                    }
                }
                break;
            }
            case KNIGHT: {
                int startPos = p.x * 100 + p.y;
                int finalPos = x * 100 + y;

                for (int i = 12; i < 24; i++) {
                    if (moveAdjacencies.get(startPos).get(i) == finalPos) {
                        return true;
                    }
                }
                break;
            }
            case QUEEN: {
                int startPos = p.x * 100 + p.y;
                int finalPos = x * 100 + y;

                for (int i = 0; i < 12; i++) {
                    int currPos = startPos;
                    while (inBounds(currPos/100, currPos%100)) {
                        currPos = moveAdjacencies.get(currPos).get(i);
                        if (currPos == finalPos) {
                            return true;
                        }
                    }
                }
                break;
            }
            case KING: {
                int startPos = p.x * 100 + p.y;
                int finalPos = x * 100 + y;

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
        if (subset.size() == 3 && hasKing(subset, playerColor)) {
            return true;
        }

        return false;
    }

    public static boolean inCity(GamePiece p) {
        for (GamePiece city : cities) {
            if (p.playerColor == city.playerColor && p.x == city.x && p.y == city.y) {
                return true;
            }
        }

        return true;
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
        if (p.startColor == Types.Color.NUETRAL || p.startColor == tileColor(x, y)) {
            Set<GamePiece> playerCities = playerSubset(cities, p.playerColor);
            int finalPos = x * 100 + y;
            for (GamePiece city : playerCities) {
                int cityPos = city.x * 100 + city.y;
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
        Types.Color color = Types.Color.NUETRAL;

        int dist = Math.abs(boardSize - x) + Math.abs(y);
        switch (dist % 3) {
            case 0:
                color = Types.Color.WHITE;
                break;
            case 1:
                color = Types.Color.GREY;
                break;
            case 2:
                color = Types.Color.BLACK;
                break;
        }
        return color;
    }
}
