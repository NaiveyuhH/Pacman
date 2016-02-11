package pacman.controllers.yuhao_huang;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.controllers.yuhao_huang.PacManNode;
import pacman.game.Constants;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Node;


public class DFS_Controller extends Controller<MOVE> {

    public static StarterGhosts ghosts = new StarterGhosts();

    public MOVE getMove(Game game, long timeDue) {
        Random rnd = new Random();
        MOVE[] allMoves = MOVE.values();

        int highScore = -1;
        MOVE highMove = null;
        List<MOVE> sameScoreMove = new ArrayList<MOVE>();

        for (MOVE m : allMoves) {
            //System.out.println("Trying Move: " + m);
            Game gameCopy = game.copy();
            Game gameAtM = gameCopy;
            gameAtM.advanceGame(m, ghosts.getMove(gameAtM, timeDue));
            int tempHighScore = this.DFS(new PacManNode(gameAtM, 0), 7);

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


    public int DFS(PacManNode rootGameState, int maxdepth) {
        MOVE[] allMoves = Constants.MOVE.values();
        int depth = 0;
        int highScore = -1;

        if(rootGameState.depth >= maxdepth) {
            int score = rootGameState.gameState.getScore();
           return score;
        }



        for(MOVE m : allMoves) {
            Game gameCopy = rootGameState.gameState.copy();
            gameCopy.advanceGame(m, ghosts.getMove(gameCopy, 0));
            if(gameCopy.getPacmanCurrentNodeIndex() == rootGameState.gameState.getPacmanCurrentNodeIndex()) {
                continue;
            }
            PacManNode node = new PacManNode(gameCopy, rootGameState.depth + 1);
            int tempHighScore = DFS(node, maxdepth);

            if(tempHighScore > highScore) {
                highScore = tempHighScore;
            }
        }



        return highScore;
    }

}

