package pacman.controllers.yuhao_huang;

import java.util.*;
import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

/**
 *
 * @author Yuhao Huang
 */

public class Q_Learning_Controller extends Controller<MOVE> {

	public static StarterGhosts ghosts = new StarterGhosts();
	
	public StateNode curNode = null;
	
	//parameters of Q learning
	public static final float learningRate = 0.8f;
	public static final float discount = 0.5f;
	
	//method of updating state node
	public static void updateNode(StateNode curNode, MOVE m, StateNode nextNode){
		TransitionNode link = new TransitionNode(curNode, m, nextNode);
		
		int reward = (nextNode.curGameState.wasPillEaten() ? 30 : 0)
				+ (nextNode.curGameState.wasPowerPillEaten() ? 20 : 0)
				+ 50 * (nextNode.curGameState.getNumGhostsEaten() - curNode.curGameState.getNumGhostsEaten())
				- (curNode.curGameState.getPacmanLastMoveMade() == m.opposite() ? 7 : 5)
				+ 300 * (nextNode.curGameState.getPacmanNumberOfLivesRemaining() - curNode.curGameState.getPacmanNumberOfLivesRemaining());
				
		if(curNode.adjacent.isEmpty() || !(curNode.adjacent.contains(link)) ){
			link.QValue = reward;
			curNode.adjacent.add(link);
			System.out.println("new link added");
		}else{
			System.out.println("link updated");
			for(TransitionNode tranNode : curNode.adjacent){
				if(link.equals(tranNode)){
					
					float tempQ = 0;
					for(TransitionNode nextTNode : tranNode.toState.adjacent){
						tempQ = nextTNode.QValue > tempQ ? nextTNode.QValue : tempQ;
					}
					
					link.QValue = tranNode.QValue + learningRate * (reward + discount * tempQ - tranNode.QValue);
					curNode.adjacent.remove(tranNode);
					curNode.adjacent.add(link);
					break;
				}
			}
		}
	}

	public MOVE getMove(Game game, long timeDue) {
		
		int current = game.getPacmanCurrentNodeIndex();
		
		this.curNode = new StateNode(game);

		MOVE[] allMoves = game.getPossibleMoves(current);
		
		// Q learning algorithm starts here
		float highScore = Integer.MIN_VALUE;
		MOVE highMove = MOVE.NEUTRAL;
		
		for (MOVE m : allMoves) {
			
//			Game gameCopy = game.copy();
			Game gameAtM = game.copy();
			
			gameAtM.advanceGame(m, ghosts.getMove(gameAtM, timeDue));
			StateNode tempNode = new StateNode(gameAtM);
			
			updateNode(this.curNode, m, tempNode);
		}
		
		for (TransitionNode tranNode : this.curNode.adjacent){
//			System.out.println("Try to move " + tranNode.move.toString() + " with Q value " + tranNode.QValue);
			if (highScore < tranNode.QValue){
				highScore = tranNode.QValue;
				highMove = tranNode.move;
			}
		}
//		System.out.println("Go " + highMove.toString());
		return highMove;
	}
}