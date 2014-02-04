package com.tiberiuslabs.battlechess;

import com.sun.accessibility.internal.resources.accessibility_it;

import java.util.*;

class Board {
    private final Set<GamePiece> activePieces;
    private final Set<GamePiece> deadPieces;
    private final Set<GamePiece> citiesHeld = new HashSet<GamePiece>();
    private final int numCities;

    public Board() {
        activePieces = Rules.initialSetup();
        deadPieces = new HashSet<GamePiece>();
        numCities = Rules.numCities();
    }

    public void update() {
        citiesHeld.clear();

        for (GamePiece p : activePieces) {
            if (Rules.inCity(p)) {
                citiesHeld.add(p);
            }
        }
    }

    public GamePiece at(int x, int y) {
        if (Rules.inBounds(x, y)) {
            for (GamePiece p : activePieces) {
                if (p.x == x && p.y == y) {
                    return p;
                }
            }
        }

        return null;
    }

    public Types.Color tileColor(int x, int y) {
        return Rules.tileColor(x, y);
    }

    public boolean move(Types.Color playerColor, int startx, int starty, int finalx, int finaly) {
        if (Rules.inBounds(startx, starty) && Rules.inBounds(finalx, finaly)) {
            GamePiece attacker = at(startx, starty); 
            GamePiece defender = at(finalx, finaly);
            if (attacker != null && attacker.playerColor == playerColor) {
                if (defender != null && Rules.validAttack(attacker, defender)) {
                    attacker.x = finalx;
                    attacker.y = finaly;
                    attacker.hasMoved = true;

                    activePieces.remove(defender);
                    deadPieces.add(defender);
                    defender.hasMoved = true;

                    return true;
                }

                else if (defender == null && Rules.validMove(attacker, finalx, finaly)) {
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
