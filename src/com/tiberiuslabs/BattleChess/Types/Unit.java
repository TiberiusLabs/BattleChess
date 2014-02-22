package com.tiberiuslabs.BattleChess.Types;

/**
 * @author Amandeep Gill
 *
 * Container for the Unit class
 *  - the unit type
 *  - the player color
 *  - an id to distinguish between two units of the same type
 */
public class Unit {
    private final transient int hash;

    public final UnitType unitType;
    public final Color color;
    public final int id;

    public Unit(UnitType unitType, Color color, int id) {
        this.unitType = unitType;
        this.color = color;
        this.id = id;

        hash =  (unitType == null ? 0 : unitType.hashCode()) * 43 +  (color == null ? 0 : color.hashCode()) * 29 + id;
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
}
