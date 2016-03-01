package pacman.controllers.yuhao_huang;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by hyh0221 on 2/22/16.
 */
public class Hill_Climbing_Controller extends Controller<MOVE> {
    public static StarterGhosts ghosts = new StarterGhosts();

    public Constants.MOVE getMove(Game game, long timeDue) {
        Random rnd = new Random();
        Constants.MOVE[] allMoves = Constants.MOVE.values();

        int highScore = -1;
        Constants.MOVE highMove = null;
        List<Constants.MOVE> sameScoreMove = new ArrayList<Constants.MOVE>();

        for (Constants.MOVE m : allMoves) {
            //System.out.println("Trying Move: " + m);
            Game gameCopy = game.copy();
            Game gameAtM = gameCopy;
            gameAtM.advanceGame(m, ghosts.getMove(gameAtM, timeDue));
            // Use hill climbing
            int tempHighScore = this.Hill_Climbing(new PacManNode(gameAtM, 0));

            if (gameAtM.getPacmanCurrentNodeIndex() == game.getPacmanCurrentNodeIndex()) {
                continue;

            } else {
                if (highScore < tempHighScore) {
                    highScore = tempHighScore;
                    highMove = m;
                } else if(highScore == tempHighScore) {

                    sameScoreMove.add(m);
                }
            }


            System.out.println("Trying Move: " + m + ", Score: " + tempHighScore);
        }

        if(!sameScoreMove.isEmpty()) {
            sameScoreMove.add(highMove);
            Game gm = game.copy();
            gm.advanceGame(highMove, ghosts.getMove(gm, timeDue));

            int size = sameScoreMove.size();
            Random rn = new Random();
            highMove = sameScoreMove.get(rn.nextInt(size));
        }

        System.out.println("High Score: " + highScore + ", High Move:" + highMove);
        return highMove;
    }


    public int Hill_Climbing(PacManNode rootGameState) {
        Constants.MOVE[] allMoves = Constants.MOVE.values();
//        int depth = 0;
        int highScore = -1;


        for(Constants.MOVE m : allMoves) {
            Game gameCopy = rootGameState.gameState.copy();
            gameCopy.advanceGame(m, ghosts.getMove(gameCopy, 0));

            if(gameCopy.getPacmanCurrentNodeIndex() == rootGameState.gameState.getPacmanCurrentNodeIndex()) {
                continue;
            }

            PacManNode node = new PacManNode(gameCopy, rootGameState.depth);
            int tempHighScore = node.gameState.getScore();

            if(tempHighScore > highScore) {
                highScore = tempHighScore;
            }
        }



        return highScore;
    }
}
