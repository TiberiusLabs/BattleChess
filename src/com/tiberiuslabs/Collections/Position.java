package com.tiberiuslabs.Collections;

/**
 * Created by Amandeep Gill
 *
 * Specialized Pair wrapper specifically for integer cartesian coordinate
 */
public class Position {
    private Pair<Integer, Integer> xy;

    public Position() {
        xy = new Pair<>(0, 0);
    }

    public Position(int x, int y) {
        xy = new Pair<>(x, y);
    }

    public Position(Position other) {
        xy = new Pair<>(other.x(), other.y());
    }

    public Integer x() {
        return xy.fst;
    }

    public Integer y() {
        return xy.snd;
    }

    public Position add(Position other) {
        return new Position(this.x() + other.x(), this.y() + other.y());
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
