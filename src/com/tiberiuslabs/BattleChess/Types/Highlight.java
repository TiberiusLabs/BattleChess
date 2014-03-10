package com.tiberiuslabs.BattleChess.Types;

/**
 * holds what type of highlight the gui should display<p/>
 *
 * @author Amandeep Gill
 */
public enum Highlight {
    /**
     * do not highlight this tile/unit
     */
    NONE,
    /**
     * this tile is empty and the selected unit can move here
     */
    MOVE,
    /**
     * this tile has a unit on it that is threatened by the selected unit
     */
    THRT,
    /**
     * this tile/unit is selected
     */
    SELD
}
