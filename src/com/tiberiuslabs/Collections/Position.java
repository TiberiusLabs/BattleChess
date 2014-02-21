package com.tiberiuslabs.Collections;

/**
 * Created by Amandeep Gill
 *
 * Specialized Pair wrapper specifically for integer cartesian coordinate
 */
public class Position {
    private Pair<Integer, Integer> xy;

    Position() {
        xy = new Pair<Integer, Integer>(0, 0);
    }

    Position(int x, int y) {
        xy = new Pair<Integer, Integer>(x, y);
    }

    public Integer x() {
        return xy.fst;
    }

    public Integer y() {
        return xy.snd;
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
