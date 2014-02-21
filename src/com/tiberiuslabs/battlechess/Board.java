package com.tiberiuslabs.battlechess;

import java.util.*;

class Board {
    private final Set<GamePiece> activePieces;
    private final Set<GamePiece> deadPieces;
    private final Set<GamePiece> citiesHeld = new HashSet<GamePiece>();
    private final GamePiece[][] gameBoard = new GamePiece[11][11];

    public Board() {
        for (int x = 0; x < 11; x++) {
            for (int y = 0; y < 11; y++) {
                gameBoard[x][y] = null;
            }
        }
        activePieces = Rules.initialSetup();
        deadPieces = new HashSet<GamePiece>();

        for (GamePiece piece : activePieces) {
            gameBoard[piece.y][piece.y] = piece;
        }
    }

    /**
     * Updates the cities that are being held by either player
     */
    public void update() {
        citiesHeld.clear();

        for (GamePiece p : activePieces) {
            if (Rules.inCity(p)) {
                citiesHeld.add(p);
            }
        }
    }

    /**
     * Finds the GamePiece associated with the x,y coordinate
     * @param x
     * @param y
     * @return      The GamePiece found at (x,y) or null if no piece was found
     */
    public GamePiece at(int x, int y) {
        if (Rules.inBounds(x, y)) {
            return gameBoard[x][y];
        }

        return null;
    }

    public GamePiece at(Position pos) {
        return at(pos.x, pos.y);
    }

    /**
     * Returns the color of the board tile at (x,y)
     * @param x
     * @param y
     * @return      BLACK, WHITE, or GREY if the coordinate is in-bounds, NEUTRAL otherwise
     */
    public Types.Color tileColor(int x, int y) {
        return Rules.tileColor(x, y);
    }

    /**
     *
     * @param playerColor
     * @param startx
     * @param starty
     * @param finalx
     * @param finaly
     * @return
     */
    public boolean move(Types.Color playerColor, int startx, int starty, int finalx, int finaly) {
        if (Rules.inBounds(startx, starty) && Rules.inBounds(finalx, finaly)) {
            GamePiece attacker = at(startx, starty); 
            GamePiece defender = at(finalx, finaly);
            if (attacker != null && attacker.playerColor == playerColor) {
                if (defender != null && Rules.validAttack(attacker, defender, this)) {
                    gameBoard[attacker.x][attacker.y] = null;
                    gameBoard[finalx][finaly] = attacker;
                    attacker.x = finalx;
                    attacker.y = finaly;
                    attacker.hasMoved = true;

                    activePieces.remove(defender);
                    deadPieces.add(defender);
                    defender.hasMoved = true;

                    return true;
                }

                else if (defender == null && Rules.validMove(attacker, finalx, finaly, this)) {
                    gameBoard[attacker.x][attacker.y] = null;
                    gameBoard[finalx][finaly] = attacker;
                    attacker.x = finalx;
                    attacker.y = finaly;
                    attacker.hasMoved = true;

                    return true;
                }
            }
        }

        return false;
    }

    public boolean recruit(Types.UnitType unitType, Types.Color playerColor, int x, int y) {
        if (Rules.inBounds(x, y) && at(x, y) == null && Rules.canRecruit(citiesHeld, playerColor)) {
            for (GamePiece p : deadPieces) {
                if (p.playerColor == playerColor && p.unitType == unitType && Rules.validRecruitment(p, x, y)) {
                    deadPieces.remove(p);
                    activePieces.add(p);
                    p.x = x;
                    p.y = y;
                    gameBoard[x][y] = p;

                    return true;
                }
            }
        }

        return false;
    }

    public int citiesHeld(Types.Color playerColor) {
        return Rules.playerSubset(citiesHeld, playerColor).size();
    }

    public List<String> availableRecruits(Types.Color playerColor) {
        List<String> graveyard = new ArrayList<String>();

        if (Rules.canRecruit(citiesHeld, playerColor)) {
            Set<GamePiece> subset = Rules.playerSubset(deadPieces, playerColor);
            for (GamePiece p : subset) {
                graveyard.add(p.toString());
            }
        }

        return graveyard;
    }

    public Types.Color winner() {
        if (Rules.playerSubset(activePieces, Types.Color.WHITE).size() == 0) {
            return Types.Color.BLACK;
        }

        if (Rules.playerSubset(activePieces, Types.Color.BLACK).size() == 0) {
            return Types.Color.WHITE;
        }

        return Rules.holdsCapitols(citiesHeld);
    }
}
