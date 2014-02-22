package com.tiberiuslabs.Collections;

/**
 * @author Amandeep Gill
 *
 * Simple pair (tuple) class
 */
public class Pair<Fst, Snd> {
    public final Fst fst;
    public final Snd snd;
    private transient final int hash;

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

