package pacman.controllers.yuhao_huang;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants.*;
import pacman.game.Constants;

import pacman.game.Game;
import pacman.controllers.yuhao_huang.AlphaBeta_Controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Random;


/**
 * Created by hyh0221 on 3/21/16.
 */

/*
This class is for ghosts in Alpha-Beta pruning algorithm
 */
public class AlphaBetaGhosts extends Controller<EnumMap<Constants.GHOST,Constants.MOVE>> {
    private EnumMap<Constants.GHOST,Constants.MOVE> moves = new EnumMap<Constants.GHOST,Constants.MOVE>(Constants.GHOST.class);
    private Constants.MOVE[] allMoves = Constants.MOVE.values();
    private Random rnd=new Random();
    public static StarterGhosts ghosts = new StarterGhosts();
    private static boolean isBeta = true;

    public EnumMap<GHOST,MOVE> getMove(Game game, long timeDue) {
        moves.clear();

        Game gameAtM = game.copy();
        int tempScore = Alpha_Beta(gameAtM, 3, timeDue, isBeta);

        for(GHOST ghost : GHOST.values()) {
            EnumMap<GHOST,MOVE> ghostMoves=new EnumMap<GHOST,MOVE>(GHOST.class);
            for (MOVE m : allMoves) {
                //System.out.println("Trying Move: " + m);
                ghostMoves.put(ghost, m);

                for(GHOST gh : GHOST.values()) {
                    if(!ghostMoves.containsKey(gh)) {
                        ghostMoves.put(gh, MOVE.NEUTRAL);
                    }
                }
                gameAtM.advanceGame(MOVE.NEUTRAL, ghostMoves);

                if (Alpha_Beta(gameAtM, 2, timeDue, !isBeta) == tempScore) {
                    if (gameAtM.getPacmanCurrentNodeIndex() == game.getPacmanCurrentNodeIndex()) {
                        MOVE tempMove = allMoves[rnd.nextInt(5)];
                        while (tempMove == m) {
                            tempMove = allMoves[rnd.nextInt(5)];
                        }
                        m = tempMove;
                        ghostMoves.put(ghost, m);
                    }
                    return ghostMoves;
                }

            }
        }
        return moves;
    }

    public int Alpha_Beta(Game rootGameState, int maxdepth, long timeDue, boolean isAlpha) {
        if(isAlpha)
            return AlphaMove(rootGameState, maxdepth, Integer.MIN_VALUE, Integer.MAX_VALUE, timeDue);
        else
            return BetaMove(rootGameState, maxdepth, Integer.MIN_VALUE, Integer.MAX_VALUE, timeDue);
    }
    // Maximizer
    public int AlphaMove(Game game, int depth, int alpha, int beta, long timeDue) {
        if(game.gameOver() || depth == 0) {
            return evalFunc(game);
        } else {
            int bestScore = Integer.MIN_VALUE;

            MOVE bestMove = MOVE.NEUTRAL;
            MOVE[] moves = MOVE.values();
            for(MOVE m : moves) {
                Game gameAtM = game.copy();

                gameAtM.advanceGame(m, ghosts.getMove(gameAtM, timeDue));
                int tempScore = BetaMove(gameAtM, depth - 1, alpha, beta, timeDue);

                bestScore = Math.max(bestScore, tempScore);
                if(bestScore  >= beta) {
                    return bestScore;
                }
                alpha = Math.max(bestScore, alpha);
            }
            return bestScore;
        }


    }
    // Minimizer
    public int BetaMove(Game game, int depth, int alpha, int beta, long timeDue) {
        if(game.gameOver() || depth == 0) {
            return evalFunc(game);
        } else {

            ArrayList<Integer> bestScores = new ArrayList<Integer>();
//            MOVE bestMove = MOVE.NEUTRAL;
            MOVE[] moves = MOVE.values();


            for(GHOST ghost : GHOST.values()) {
                EnumMap<GHOST,MOVE> ghostMoves=new EnumMap<GHOST,MOVE>(GHOST.class);
                int tempAlpha = alpha, tempBeta = beta;
                int bestScore = Integer.MAX_VALUE;

                for (MOVE m : moves) {
                    Game gameAtM = game.copy();
                    ghostMoves.put(ghost, m);

                    for(GHOST gh : GHOST.values()) {
                        if(!ghostMoves.containsKey(gh)) {
                            ghostMoves.put(gh, MOVE.NEUTRAL);
                        }
                    }

                    /* Advancing game state using ghost's move */

                    gameAtM.advanceGame(MOVE.NEUTRAL, ghostMoves);

                    /*-------------------------------------------------------*/

                    int tempScore = AlphaMove(gameAtM, depth - 1, tempAlpha, tempBeta, timeDue);

                    bestScore = Math.min(bestScore, tempScore);
                    if (bestScore <= tempAlpha) {
                        bestScores.add(bestScore);
                        break;
                    }
                    tempBeta = Math.min(tempBeta, bestScore);
                }
                beta = Math.min(beta, tempBeta);
                if(bestScore > tempAlpha) {
                    bestScores.add(bestScore);
                }
            }
            return Collections.min(bestScores);
        }
    }

    //Evaluation function of game states
    public int evalFunc(Game game) {
        int score = game.getScore();
        int pacIndex = game.getPacmanCurrentNodeIndex();
        int value = Integer.MAX_VALUE;
        for(GHOST ghost : GHOST.values()) {
            int ghostIndex = game.getGhostCurrentNodeIndex(ghost);
            int pac2ghostDist = game.getShortestPathDistance(pacIndex, ghostIndex);
            value = Math.min(pac2ghostDist, value);
        }
        if(value < 100) {
            value = score + value;
        } else {
            value = score;
        }
        return value;
    }
}
