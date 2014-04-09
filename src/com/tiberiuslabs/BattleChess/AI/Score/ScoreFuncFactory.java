package com.tiberiuslabs.BattleChess.AI.Score;

import com.tiberiuslabs.BattleChess.ChessEngine.Rules;
import com.tiberiuslabs.BattleChess.Types.Color;
import com.tiberiuslabs.BattleChess.Types.Position;
import com.tiberiuslabs.BattleChess.Types.Unit;

import java.util.HashSet;
import java.util.Set;

/**
 * Static generator class to build an array of ScoreFunc lambdas
 *
 * @author Amandeep Gill
 * @see com.tiberiuslabs.BattleChess.AI.Score.ScoreFunc
 */
public class ScoreFuncFactory {
    /**
     * Constructs a list of ScoreFunc lambdas for the AI to use to generate the best move
     *
     * @return an array of 10 ScoreFunc lambdas
     */
    public static ScoreFunc[] buildScoreFuncs() {
        ScoreFunc[] scoreFuncs = new ScoreFunc[5];

        scoreFuncs[0] = playerMaterialScore();
        scoreFuncs[1] = opponentMaterialScore();
        scoreFuncs[2] = playerMovementFreedom();
        scoreFuncs[3] = playerHoldsCapitol();
        scoreFuncs[4] = opponentCanRecruit();

        return scoreFuncs;
    }

    private static ScoreFunc playerMaterialScore() {
        return (board, player) -> {
            int score = 0;
            for (Unit unit : board.getActiveUnits(player)) {
                switch (unit.unitType) {
                    case PAWN:
                        score += 200;
                        break;
                    case KNIGHT:
                        score += 300;
                        break;
                    case ROOK:
                        score += 500;
                        break;
                    case BISHOP:
                        score += 500;
                        break;
                    case QUEEN:
                        score += 900;
                        break;
                    case KING:
                        score += 10000;
                        break;
                }
            }

            return score - 500;
        };
    }

    private static ScoreFunc opponentMaterialScore() {
        return (board, player) -> {
            int score = 0;
            for (Unit unit : board.getActiveUnits(player == Color.BLACK ? Color.WHITE : Color.BLACK)) {
                switch (unit.unitType) {
                    case PAWN:
                        score -= 200;
                        break;
                    case KNIGHT:
                        score -= 300;
                        break;
                    case ROOK:
                        score -= 500;
                        break;
                    case BISHOP:
                        score -= 500;
                        break;
                    case QUEEN:
                        score -= 900;
                        break;
                    case KING:
                        score -= 10000;
                        break;
                }
            }

            return score;
        };
    }

    public static ScoreFunc playerMovementFreedom() {
        return (board, player) -> {
            Set<Position> validMoves = new HashSet<>();
            for (Unit unit : board.getActiveUnits(player)) {
                validMoves.addAll(Rules.getValidMoves(unit, unit.position, board));
            }

            return validMoves.size() > 50 ? 50 : validMoves.size();
        };
    }

    public static ScoreFunc playerHoldsCapitol() {
        return (board, player) -> {
            Unit capitol = board.get(Rules.getCapitol(player));
            return capitol != null && capitol.color == player ? 500 : 0;
        };
    }

    public static ScoreFunc opponentCanRecruit() {
        return (board, player) -> {
            Color opponent = player == Color.BLACK ? Color.WHITE : Color.BLACK;
            Unit capitol = board.get(Rules.getCapitol(opponent));
            int numCities = board.numCitiesHeld(opponent);

            return capitol != null && capitol.color == opponent && numCities >= 3 ? -200 : 0;
        };
    }
}
