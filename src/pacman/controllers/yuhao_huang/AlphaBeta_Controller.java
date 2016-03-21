package pacman.controllers.yuhao_huang;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Constants.*;
import pacman.game.Game;

import java.util.*;

/**
 * Created by hyh0221 on 3/20/16.
 */
public class AlphaBeta_Controller extends Controller{
    public static StarterGhosts ghosts = new StarterGhosts();
    private static boolean isAlpha = true;
    public Constants.MOVE getMove(Game game, long timeDue) {
        Random rnd=new Random();
        Constants.MOVE[] allMoves= Constants.MOVE.values();


        Constants.MOVE bestMove = Constants.MOVE.NEUTRAL;
//        int alphaScore = Integer.MIN_VALUE, betaScore = Integer.MAX_VALUE;

        Game gameAtM = game.copy();
        int tempScore = Alpha_Beta(gameAtM, 3, timeDue, isAlpha);
        for(MOVE m: allMoves) {
            //System.out.println("Trying Move: " + m);
            gameAtM.advanceGame(m, ghosts.getMove(gameAtM, timeDue));

            if(Alpha_Beta(gameAtM, 2, timeDue, !isAlpha) == tempScore) {
                if (gameAtM.getPacmanCurrentNodeIndex() == game.getPacmanCurrentNodeIndex()) {
                    MOVE tempMove = allMoves[rnd.nextInt(5)];
                    while(tempMove == m) {
                        tempMove = allMoves[rnd.nextInt(5)];
                    }
                    m = tempMove;
                }
                return m;
            }

        }

//        System.out.println("High Score: " + alphaScore + ", High Move:" + bestMove);
        bestMove = allMoves[rnd.nextInt(5)];
        return bestMove;

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
                // alpha, beta for current ghost
                int tempAlpha = alpha, tempBeta = beta;
                int bestScore = Integer.MAX_VALUE;

                for (MOVE m : moves) {
                    Game gameAtM = game.copy();
                    // Assign MOVE m to current ghost
                    ghostMoves.put(ghost, m);
                    // For ghosts other than current ghost, assign MOVE.NEUTRAL
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
                // change the final beta
                beta = Math.min(beta, tempBeta);

                if(bestScore > tempAlpha) {
                    bestScores.add(bestScore);
                }
            }
            return Collections.max(bestScores);
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
        if(value < 20) {
            value = score - value;
        } else {
            value = score;
        }
        return value;
    }

}
