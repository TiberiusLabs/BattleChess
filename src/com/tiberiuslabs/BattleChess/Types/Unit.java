package com.tiberiuslabs.BattleChess.Types;

/**
 * Container for the Unit class
 * @author Amandeep Gill
 */
public class Unit {
    private final transient int hash;

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
     * @param unitType  the type of unit that this is
     * @param color     the color of the this unit's controlling player
     * @param id        an ID to distinguish this from the other units of the same type and color
     */
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
