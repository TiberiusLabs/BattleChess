package com.tiberiuslabs.BattleChess.GameState;

import com.tiberiuslabs.BattleChess.Types.Color;
import com.tiberiuslabs.BattleChess.Types.Unit;
import com.tiberiuslabs.Collections.Pair;

/**
 * @author Amandeep Gill
 *
 * Holds the current state of the game
 *  - which units are where on the board
 *  - which units are in the graveyard
 *  - which player's turn it is currently
 */
public class GameBoard {
    private Pair<Unit, Color>[][] board;
}
