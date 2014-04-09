package com.tiberiuslabs.BattleChess.Types;

/**
 * Container for the Unit class
 *
 * @author Amandeep Gill
 */
public class Unit {
    private final transient int hash;
    private final String stringID;
    private final String typeString;

    /**
     * the unit's current position
     */
    public Position position;
    /**
     * the unit's enumerated type
     */
    public final UnitType unitType;
    /**
     * the color of the controlling player
     */
    public final Color color;
    /**
     * an ID to distinguish between similar units
     */
    public final int id;

    /**
     * Initialize the Unit.
     *
     * @param unitType the type of unit that this is
     * @param color    the color of the this unit's controlling player
     * @param id       an ID to distinguish this from the other units of the same type and color
     */
    public Unit(UnitType unitType, Color color, int id) {
        this.unitType = unitType;
        this.color = color;
        this.id = id;

        boolean isBlack = color == Color.BLACK;
        switch (unitType) {
            case PAWN:
                stringID = "Pawn #" + id;
                typeString = isBlack ? "bp" : "wp";
                break;
            case KNIGHT:
                stringID = "Knight #" + id;
                typeString = isBlack ? "bn" : "wn";
                break;
            case ROOK:
                stringID = "Rook #" + id;
                typeString = isBlack ? "br" : "wr";
                break;
            case BISHOP:
                stringID = "Bishop #" + id;
                typeString = isBlack ? "bb" : "wb";
                break;
            case QUEEN:
                stringID = "Queen #" + id;
                typeString = isBlack ? "bq" : "wq";
                break;
            case KING:
                stringID = "King #" + id;
                typeString = isBlack ? "bk" : "wk";
                break;
            default:
                stringID = "Unknown";
                typeString = " ";
        }

        hash = (unitType == null ? 0 : unitType.hashCode()) * 43 + (color == null ? 0 : color.hashCode()) * 29 + id;
    }

    public String getTypeString() {
        return typeString;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other == null || !(getClass().isInstance(other))) {
            return false;
        }

        Unit otherUnit = getClass().cast(other);
        return otherUnit.unitType == this.unitType && otherUnit.color == this.color && otherUnit.id == this.id;
    }

    @Override
    public String toString() {
        return stringID;
    }
}
