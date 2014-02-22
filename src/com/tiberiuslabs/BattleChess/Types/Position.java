package com.tiberiuslabs.BattleChess.Types;

import com.tiberiuslabs.Collections.Pair;

/**
 * @author Amandeep Gill
 *
 * Specialized Pair wrapper specifically for integer cartesian coordinate
 */
public class Position {
    private Pair<Integer, Integer> xy;

    /**
     * Initializes the Position to (0,0)
     */
    public Position() {
        xy = new Pair<>(0, 0);
    }

    /**
     * Initializes the Position to (x,y)
     * @param x
     * @param y
     */
    public Position(int x, int y) {
        xy = new Pair<>(x, y);
    }

    /**
     * Initializes the Position with a copy of the (x,y) of the other Position
     * @param other
     */
    public Position(Position other) {
        xy = new Pair<>(other.x(), other.y());
    }

    /**
     * Get the x coordinate of the Position
     * @return  a reference to the Integer of x
     */
    public Integer x() {
        return xy.fst;
    }

    /**
     * Get the y coordinate of the Position
     * @return  a reference to the Integer of y
     */
    public Integer y() {
        return xy.snd;
    }

    /**
     * Construct a new Position through addition of this and other, performs check for null safety
     * @param other the Position to add to this Position
     * @return  a new Position with (x,y) == (this.x + other.x, this.y + other.y), returns this if other == null
     */
    public Position add(Position other) {
        return other == null ? new Position(this) : new Position(this.x() + other.x(), this.y() + other.y());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || !(getClass().isInstance(other))) {
            return false;
        }
        Position otherPos = getClass().cast(other);
        return this.xy.equals(otherPos.xy);
    }

    @Override
    public int hashCode() {
        return xy.hashCode();
    }
}
