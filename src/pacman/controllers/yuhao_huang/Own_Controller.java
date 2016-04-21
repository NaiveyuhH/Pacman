package pacman.controllers.yuhao_huang;

import pacman.controllers.Controller;
import pacman.game.Constants.*;
import pacman.game.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map.*;

/**
 * Created by hyh0221 on 4/20/16.
 */
public class Own_Controller extends Controller<MOVE> {
    private static final int MIN_DISTANCE = 20;	//if a ghost is this close, run away

    public MOVE getMove(Game game, long timeDue)
    {
        int current=game.getPacmanCurrentNodeIndex();

        //Strategy 1: if any ghost is too close (less than MIN_DISTANCE), run away from the nearest ghost
        HashSet<MOVE> movesAwayGhosts = new HashSet<>();


        MOVE bestMove = null;
        int shortestDis = Integer.MAX_VALUE;
        for(GHOST ghost : GHOST.values()) {

            if (game.getGhostEdibleTime(ghost) == 0 && game.getGhostLairTime(ghost) == 0) {
                int dist2Ghost = game.getShortestPathDistance(current, game.getGhostCurrentNodeIndex(ghost));
//                distances2Ghosts[ithMove++] = dist2Ghost;
                if (dist2Ghost < MIN_DISTANCE) {
                    MOVE tempMove = game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), Constants.DM.PATH);
                    if(shortestDis > dist2Ghost) {
                        shortestDis = dist2Ghost;
                        bestMove = tempMove;
                    }
                    movesAwayGhosts.add(tempMove);
//                    return tempMove;
                }


            }
        }
        if(shortestDis < MIN_DISTANCE) return bestMove;

//        Arrays.sort(distances2Ghosts);
        for(MOVE m : game.getPossibleMoves(current)) {
            if(movesAwayGhosts.contains(m)) return m;
        }
        //Strategy 2: find the nearest edible ghost. If it's closer than the MIN_DISTANCE, go after it
        int minDistance=Integer.MAX_VALUE;
        GHOST minGhost=null;

        for(GHOST ghost : GHOST.values())
            if(game.getGhostEdibleTime(ghost)>0)
            {
                int distance=game.getShortestPathDistance(current,game.getGhostCurrentNodeIndex(ghost));

                if(distance<minDistance)
                {
                    minDistance=distance;
                    minGhost=ghost;
                }
            }

        if(minGhost!=null && minDistance < MIN_DISTANCE)	//we found an edible ghost
            return game.getNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(),game.getGhostCurrentNodeIndex(minGhost), Constants.DM.PATH);

        //Strategy 3:use A* to let PacMan move towards pills or power pills
        int[] pills=game.getPillIndices();
        int[] powerPills=game.getPowerPillIndices();

        ArrayList<Integer> targets=new ArrayList<Integer>();

        for(int i=0;i<pills.length;i++)					//check which pills are available
            if(game.isPillStillAvailable(i))
                targets.add(pills[i]);

        for(int i=0;i<powerPills.length;i++)			//check with power pills are available
            if(game.isPowerPillStillAvailable(i))
                targets.add(powerPills[i]);

        int[] targetsArray=new int[targets.size()];		//convert from ArrayList to array

        for(int i=0;i<targetsArray.length;i++)
            targetsArray[i]=targets.get(i);

        int pillIndex = game.getClosestNodeIndexFromNodeIndex(current,targetsArray,DM.PATH);
        AStar_Controller astar = new AStar_Controller(pillIndex, 0);
        return astar.getMove(game, timeDue);
    }
}
