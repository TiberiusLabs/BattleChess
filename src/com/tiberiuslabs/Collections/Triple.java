package com.tiberiuslabs.Collections;

/**
 * Simple triple (tuple) class with immutable references to the first, second, and third objects
 * @author Amandeep Gill
 */
public class Triple<Fst, Snd, Thd> {
    /**
     * the first object of the Triple
     */
    public final Fst fst;
    /**
     * the second object of the Triple
     */
    public final Snd snd;
    /**
     * the third object of the Triple
     */
    public final Thd thd;

    private transient final int hash;

    /**
     * Initialize the Triple with the given first and second objects
     * @param fst   the first object of the Triple, the reference to first can't be changed once set
     * @param snd   the second object of the Triple, the reference to second can't be changed once set
     * @param thd   the third object of the Triple, the reference to third can't be changed once set
     */
    public Triple(Fst fst, Snd snd, Thd thd) {
        this.fst = fst;
        this.snd = snd;
        this.thd = thd;
        hash = (this.fst == null ? 0 : this.fst.hashCode() * 31) + (this.snd == null ? 0 : this.snd.hashCode() * 59) + (this.thd == null ? 0 : this.thd.hashCode());
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
        @SuppressWarnings("unchecked") Triple<Fst, Snd, Thd> otherTriple = getClass().cast(other);
        return (fst == null ? otherTriple.fst == null : fst.equals(otherTriple.fst))
                && (snd == null ? otherTriple.snd == null : snd.equals(otherTriple.snd));
    }

}


