package com.tiberiuslabs.BattleChess.AI.Score;

import com.tiberiuslabs.BattleChess.ChessEngine.Rules;
import com.tiberiuslabs.BattleChess.Types.Color;
import com.tiberiuslabs.BattleChess.Types.Position;
import com.tiberiuslabs.BattleChess.Types.Unit;

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
        ScoreFunc[] scoreFuncs = new ScoreFunc[10];

        scoreFuncs[0] = playerActiveUnitsScore();
        scoreFuncs[1] = opponentActiveUnitScore();
        scoreFuncs[2] = playerHasKingScore();
        scoreFuncs[3] = opponentHasKingScore();
        scoreFuncs[4] = playerThreatenedUnits();
        scoreFuncs[5] = opponentThreatenedUnits();
        scoreFuncs[6] = playerMovementFreedom();
        scoreFuncs[7] = opponentMovementFreedom();
        scoreFuncs[8] = playerCitiesHeld();
        scoreFuncs[9] = opponentCitiesHeld();


        return scoreFuncs;
    }

    /**
     * Construct a ScoreFunc lambda to score the player on their current active units
     *
     * @return a score from 0 to 100 depending on the player's active units
     */
    public static ScoreFunc playerActiveUnitsScore() {
        return (board, player) -> {
            int score = 0;
            for (Unit unit : board.getActiveUnits(player)) {
                switch (unit.unitType) {
                    case PAWN:
                        score += 1;
                        break;
                    case KNIGHT:
                        score += 5;
                        break;
                    case ROOK:
                        score += 7;
                        break;
                    case BISHOP:
                        score += 9;
                        break;
                    case QUEEN:
                        score += 20;
                        break;
                    case KING:
                        score += 20;
                        break;
                }
            }
            return score;
        };
    }

    /**
     * Construct a ScoreFunc lambda to penalize the player on their opponent's active units
     *
     * @return a number from -100 to 0 depending on the opponents active units
     */
    public static ScoreFunc opponentActiveUnitScore() {
        return (board, player) -> {
            Color opponent = player == Color.BLACK ? Color.WHITE : Color.BLACK;
            int score = 0;
            for (Unit unit : board.getActiveUnits(opponent)) {
                switch (unit.unitType) {
                    case PAWN:
                        score -= 1;
                        break;
                    case KNIGHT:
                        score -= 5;
                        break;
                    case ROOK:
                        score -= 7;
                        break;
                    case BISHOP:
                        score -= 9;
                        break;
                    case QUEEN:
                        score -= 20;
                        break;
                    case KING:
                        score -= 20;
                        break;
                }
            }
            return score;
        };
    }

    /**
     * Construct a ScoreFunc lambda to score the player on whether they have a king in play
     *
     * @return 100 if the player has a king, 0 otherwise
     */
    public static ScoreFunc playerHasKingScore() {
        return (board, player) -> (board.hasKing(player) ? 100 : 0);
    }

    /**
     * Construct a ScoreFunc lambda to penalize the player if the opponent still has their king
     *
     * @return -100 if the opponent has a king, 0 otherwise
     */
    public static ScoreFunc opponentHasKingScore() {
        return (board, player) -> {
            Color opponent = player == Color.BLACK ? Color.WHITE : Color.BLACK;
            return (board.hasKing(opponent) ? -100 : 0);
        };
    }

    /**
     * Construct a ScoreFunc lambda to score the player on how many opposing units they currently threaten
     *
     * @return a score from 0 to 100 depending on the number and value of the threatened unit
     */
    public static ScoreFunc playerThreatenedUnits() {
        return (board, player) -> {
            int score = 0;

            for (Unit unit : board.getActiveUnits(player)) {
                Set<Position> validMoves = Rules.getValidMoves(unit, board.getPosition(unit), board);
                for (Position position : validMoves) {
                    Unit defender = board.get(position);
                    if (defender != null) {
                        switch (defender.unitType) {
                            case PAWN:
                                score += 1;
                                break;
                            case KNIGHT:
                                score += 5;
                                break;
                            case ROOK:
                                score += 7;
                                break;
                            case BISHOP:
                                score += 9;
                                break;
                            case QUEEN:
                                score += 20;
                                break;
                            case KING:
                                score += 20;
                                break;
                        }
                    }
                }
            }

            return score;
        };
    }

    /**
     * Construct a ScoreFunc lambda to penalize the player for the number of their own units that are threatened
     *
     * @return a score from -100 to 0 depending on the number and value of the player's units that are threatened
     */
    public static ScoreFunc opponentThreatenedUnits() {
        return (board, player) -> {
            Color opponent = player == Color.BLACK ? Color.WHITE : Color.BLACK;
            int score = 0;

            for (Unit unit : board.getActiveUnits(opponent)) {
                Set<Position> validMoves = Rules.getValidMoves(unit, board.getPosition(unit), board);
                for (Position position : validMoves) {
                    Unit defender = board.get(position);
                    if (defender != null) {
                        switch (defender.unitType) {
                            case PAWN:
                                score -= 1;
                                break;
                            case KNIGHT:
                                score -= 5;
                                break;
                            case ROOK:
                                score -= 7;
                                break;
                            case BISHOP:
                                score -= 9;
                                break;
                            case QUEEN:
                                score -= 20;
                                break;
                            case KING:
                                score -= 20;
                                break;
                        }
                    }
                }
            }

            return score;
        };
    }

    /**
     * Construct a ScoreFunc lambda to score the player on how freely their units can move
     *
     * @return a sum of the number of tiles each unit can move or attack to
     */
    public static ScoreFunc playerMovementFreedom() {
        return (board, player) -> {
            int score = 0;

            for (Unit unit : board.getActiveUnits(player)) {
                Set<Position> validMoves = Rules.getValidMoves(unit, board.getPosition(unit), board);
                for (Position position : validMoves) {
                    if (board.get(position) == null) {
                        score += 5;
                    }
                }
            }

            return score;
        };
    }

    /**
     * Construct a ScoreFunc lambda to penalize the player on how freely their opponent's units can move
     *
     * @return a sum of the number of tiles each opposing unit can move or attack to
     */
    public static ScoreFunc opponentMovementFreedom() {
        return (board, player) -> {
            Color opponent = player == Color.BLACK ? Color.WHITE : Color.BLACK;
            int score = 0;

            for (Unit unit : board.getActiveUnits(opponent)) {
                Set<Position> validMoves = Rules.getValidMoves(unit, board.getPosition(unit), board);
                for (Position position : validMoves) {
                    if (board.get(position) == null) {
                        score += 1;
                    }
                }
            }

            return score;
        };
    }

    /**
     * Construct a ScoreFunc to score the player on the number of cities that they hold
     *
     * @return a score from 0 to 100 depending on how many of the cities the player holds
     */
    public static ScoreFunc playerCitiesHeld() {
        return (board, player) -> (100 / 6) * board.numCitiesHeld(player);
    }

    /**
     * Construct a ScoreFunc to penalize the player on the number of cities their opponent holds
     *
     * @return a score from -100 to 0 depending on how many of the cities the opponent holds
     */
    public static ScoreFunc opponentCitiesHeld() {
        return (board, player) -> {
            Color opponent = player == Color.BLACK ? Color.WHITE : Color.BLACK;
            return (-100 / 6) * board.numCitiesHeld(opponent);
        };
    }
}
