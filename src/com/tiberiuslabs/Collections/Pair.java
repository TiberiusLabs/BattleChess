package com.tiberiuslabs.Collections;

/**
 * Simple pair (tuple) class with immutable references to the first and second object
 * @author Amandeep Gill
 */
public class Pair<Fst, Snd> {
    /**
     * the first object of the Pair
     */
    public final Fst fst;
    /**
     * the second object of the Pair
     */
    public final Snd snd;
    private transient final int hash;

    /**
     * Initialize the Pair with the given first and second objects
     * @param fst   the first object of the Pair, the reference to first can't be changed once set
     * @param snd   the second object of the Pair, the reference to second can't be changed once set
     */
    public Pair(Fst fst, Snd snd) {
        this.fst = fst;
        this.snd = snd;
        hash = (this.fst == null ? 0 : this.fst.hashCode() * 31) + (this.snd == null ? 0 : this.snd.hashCode());
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
        @SuppressWarnings("unchecked") Pair<Fst, Snd> otherPair = getClass().cast(other);
        return (fst == null ? otherPair.fst == null : fst.equals(otherPair.fst))
                && (snd == null ? otherPair.snd == null : snd.equals(otherPair.snd));
    }

}

