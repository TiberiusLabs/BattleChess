package com.tiberiuslabs.BattleChess.Types;

import com.tiberiuslabs.Collections.Pair;

/**
 * Specialized Pair wrapper specifically for integer cartesian coordinate
 * @author Amandeep Gill
 */
public class Position {
    private final Pair<Integer, Integer> xy;

    /**
     * Initializes the Position to (0,0)
     */
    public Position() {
        xy = new Pair<>(0, 0);
    }

    /**
     * Initializes the Position to (x,y)
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public Position(int x, int y) {
        xy = new Pair<>(x, y);
    }

    /**
     * Initializes the Position with a copy of the (x,y) of the other Position
     * @param other the position to copy the data from
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
     * @return  a new Position with (x,y) == (this.x + other.x, this.y + other.y), returns a copy of this if other == null
     */
    public Position add(Position other) {
        return other == null ? new Position(this) : new Position(this.x() + other.x(), this.y() + other.y());
    }

    public Position add(int x, int y) {
        return new Position(xy.fst + x, xy.snd + y);
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
