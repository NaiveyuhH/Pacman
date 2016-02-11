package pacman.controllers.yuhao_huang;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Arrays;
import java.util.Collections;
import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import pacman.game.internal.Node;


public class BFS_Controller extends Controller<MOVE> {


        
        /*public enum MOVE {
		UP 		{ public MOVE opposite(){return MOVE.DOWN;		};},	
		RIGHT 	{ public MOVE opposite(){return MOVE.LEFT;		};}, 	
		DOWN 	{ public MOVE opposite(){return MOVE.UP;		};},		
		LEFT 	{ public MOVE opposite(){return MOVE.RIGHT;		};}, 	
		NEUTRAL	{ public MOVE opposite(){return MOVE.NEUTRAL;	};};	
		
		public abstract MOVE opposite();
		private static final List<MOVE> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();
		public static MOVE randomMove()  {
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	};*/



    public static StarterGhosts ghosts = new StarterGhosts();
	public MOVE getMove(Game game,long timeDue) {
            Random rnd=new Random();
            MOVE[] allMoves=MOVE.values();
        
            int highScore = -1;
            MOVE highMove = null;
            List<MOVE> sameScoreMove = new ArrayList<MOVE>();
           
            for(MOVE m: allMoves) {
                //System.out.println("Trying Move: " + m);
                Game gameCopy = game.copy();
                Game gameAtM = gameCopy;
                gameAtM.advanceGame(m, ghosts.getMove(gameAtM, timeDue));
                int tempHighScore = this.BFS(new PacManNode(gameAtM, 0), 7);
                
                if(gameAtM.getPacmanCurrentNodeIndex() == game.getPacmanCurrentNodeIndex()) {

                    continue;

                } else {
                    if(highScore < tempHighScore) {
                        highScore = tempHighScore;
                        highMove = m;
                    } else if(highScore == tempHighScore){

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
        
        public int BFS(PacManNode rootGameState, int maxdepth) {
            MOVE[] allMoves=Constants.MOVE.values();
            int depth = 0;
            int highScore = -1;
		
            Queue<PacManNode> queue = new LinkedList<PacManNode>();
            queue.add(rootGameState);

		//System.out.println("Adding Node at Depth: " + rootGameState.depth);
                
  
            while(!queue.isEmpty()) {
                    PacManNode pmnode = queue.remove();
//                    System.out.println("Removing Node at Depth: " + pmnode.depth);
                    
                    if(pmnode.depth >= maxdepth) {
                        int score = pmnode.gameState.getScore();
                         if (highScore < score)
                                  highScore = score;
                    } else {
                        //GET CHILDREN
                        for (MOVE m : allMoves) {
                            Game gameCopy = pmnode.gameState.copy();
                            gameCopy.advanceGame(m, ghosts.getMove(gameCopy, 0));
                            if(gameCopy.getPacmanCurrentNodeIndex() != pmnode.gameState.getPacmanCurrentNodeIndex()) {
                                PacManNode node = new PacManNode(gameCopy, pmnode.depth + 1);
                                queue.add(node);
                            } else {}
                        }
                    }

		}
                
                return highScore;
	}
        
    
}
