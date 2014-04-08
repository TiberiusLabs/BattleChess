package com.tiberiuslabs.BattleChess.AI;

import com.tiberiuslabs.BattleChess.AI.Score.ScoreFunc;
import com.tiberiuslabs.BattleChess.ChessEngine.Board;
import com.tiberiuslabs.BattleChess.ChessEngine.GameBoard;
import com.tiberiuslabs.BattleChess.ChessEngine.Rules;
import com.tiberiuslabs.BattleChess.Types.AIDifficulty;
import com.tiberiuslabs.BattleChess.Types.Color;
import com.tiberiuslabs.Collections.Pair;

import java.util.*;

import static com.tiberiuslabs.BattleChess.AI.Score.ScoreFuncFactory.buildScoreFuncs;
import static com.tiberiuslabs.BattleChess.ChessEngine.GameBoard.Move;

/**
 * Sorts through the possible move options and selects the best one
 *
 * @author Amandeep Gill
 */
public class AIEngine {
    private AI ai;

    /**
     * Initialize the AI with the given difficulty setting and player color
     *
     * @param level the difficulty setting for the AI
     * @param color the player color for the AI
     */
    public AIEngine(AIDifficulty level, Color color) {
        // currently only generates a random AI
        this.ai = new AI(buildScoreFuncs(), color);
    }

    /**
     * Get the player color of the AI
     *
     * @return the AI's player color
     */
    public Color getAIColor() {
        return ai.getColor();
    }

    /**
     * Gets the AI's move.
     *
     * @param board the current game state, must not be null. Makes a copy to ensure that the game state is not
     *              changed while the AI calculates the best move to make
     * @return a Move instance representing what the AI sees as the best move for it to make
     * @see com.tiberiuslabs.BattleChess.ChessEngine.GameBoard.Move
     */
    public Move getAIMove(Board board) throws AI.NoMoveException {
        return ai.getMove(board);
    }

    public static void main(String[] args) {
        List<AI> ais = new ArrayList<>(100);
        ScoreFunc[] scoreFuncs = buildScoreFuncs();

        for (int i = 0; i < 100; i += 1) {
            ais.add(i, new AI(scoreFuncs, Color.BLACK));
        }

        for (int gen = 0; gen < 5; gen += 1) {
            SortedSet<Pair<AI, Integer>> aiSet = new TreeSet<>((Pair<AI, Integer> o1, Pair<AI, Integer> o2) -> o1.snd.compareTo(o2.snd));
            for (AI white : ais) {
                int wins = 0;
                for (AI black : ais) {
                    System.out.println(white + " vs " + black);
                    if (playAIGame(white, black)) {
                        wins += 1;
                        System.out.println(white + " wins");
                    } else {
                        System.out.println(black + " wins");
                    }
                }
                aiSet.add(new Pair<>(white, wins));
            }
            Set<AI> topTen = new HashSet<>();
            for (Pair<AI, Integer> aiPair : aiSet) {
                topTen.add(aiPair.fst);
                if (topTen.size() > 10) {
                    break;
                }
            }

            System.out.println("Generation " + gen + " winners: ");
            topTen.forEach(System.out::println);

            List<AI> nextGen = new ArrayList<>();
            for (AI white : topTen) {
                for (AI black : topTen) {
                    nextGen.add(white.generateChild(black));
                }
            }

            ais = nextGen;
        }
    }

    public static boolean playAIGame(AI white, AI black) {
        GameBoard board = new GameBoard();
        white.setColor(Color.WHITE);
        black.setColor(Color.BLACK);

        for (int moves = 0; moves < 100; moves += 1) {
            Move wMove;
            try {
                wMove = white.getMove(new GameBoard(board));
            } catch (AI.NoMoveException e) {
                return false;
            }
            if (wMove != null) {
                if (wMove.startPos == null) {
                    board.set(wMove.attacker, wMove.finalPos);
                } else {
                    board.move(wMove.startPos, wMove.finalPos);
                }

                if (Rules.winner(board) == Color.WHITE) {
                    return true;
                }
            } else {
                return false;
            }

            Move bMove;
            try {
                bMove = black.getMove(new GameBoard(board));
            } catch (AI.NoMoveException e) {
                return true;
            }
            if (bMove != null) {
                if (bMove.startPos == null) {
                    board.set(bMove.attacker, bMove.finalPos);
                } else {
                    board.move(bMove.startPos, bMove.finalPos);
                }

                if (Rules.winner(board) == Color.BLACK) {
                    return false;
                }
            } else {
                return true;
            }
        }

        System.out.println("max moves exceeded");
        return false;
    }
}
