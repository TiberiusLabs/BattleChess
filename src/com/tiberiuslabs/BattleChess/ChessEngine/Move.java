package com.tiberiuslabs.BattleChess.ChessEngine;

import com.sun.istack.internal.NotNull;
import com.tiberiuslabs.BattleChess.Types.MoveType;
import com.tiberiuslabs.BattleChess.Types.Position;
import com.tiberiuslabs.BattleChess.Types.Unit;

/**
 * encapsulates a move for AI move generation and board move stack
*/
public class Move {
    public final Unit attacker;
    public final Unit defender;
    public final Position startPos;
    public final Position finalPos;
    public final MoveType moveType;

    public Move(Unit attacker, Position startPos, Unit defender, Position finalPos, MoveType moveType) {
        this.attacker = attacker;
        this.defender = defender;
        this.startPos = startPos;
        this.finalPos = finalPos;
        this.moveType = moveType;
    }
}
