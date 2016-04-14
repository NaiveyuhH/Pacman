package pacman.controllers.yuhao_huang;

import pacman.game.Constants;
import pacman.game.Game;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by hyh0221 on 4/14/16.
 */
public class StateNode {
    public Game curGameState;
    private int distToPill;
    private int distToPowerPill;
    private int distToGhost;
    private boolean islair = false;

    public Set<TransitionNode> adjacent = new HashSet<TransitionNode>();

    public StateNode(Game curGame) {
        this.curGameState = curGame;

        int current = curGame.getPacmanCurrentNodeIndex();
        int[] pills=curGame.getActivePillsIndices();
        int[] powerPills=curGame.getActivePowerPillsIndices();

        this.distToPill = Integer.MAX_VALUE;

        for(int i=0;i<pills.length;i++){				//find the nearest pill
            int tempDistance = curGame.getManhattanDistance(current, pills[i]);
            this.distToPill = tempDistance < this.distToPill ? tempDistance : this.distToPill;
        }

        this.distToPowerPill = Integer.MAX_VALUE;

        for(int i=0;i<powerPills.length;i++){				//find the nearest power pill
            int tempDistance = curGame.getManhattanDistance(current, powerPills[i]);
            this.distToPowerPill = tempDistance < this.distToPowerPill ? tempDistance : this.distToPowerPill;
        }

        Constants.GHOST[] ghosts = Constants.GHOST.values();

        this.distToGhost = Integer.MAX_VALUE;

        for(Constants.GHOST ghost : ghosts){						//find the nearest ghost and whether it is edible
            int tempDistance = curGame.getShortestPathDistance(current, curGame.getGhostCurrentNodeIndex(ghost));
            if(tempDistance < this.distToGhost){
                this.distToGhost = tempDistance;
                this.islair = curGame.getGhostEdibleTime(ghost)>0;
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        StateNode other = (StateNode) obj;
        return this.islair == other.islair && this.distToGhost == other.distToGhost && this.distToPill == other.distToPill && this.distToPowerPill == other.distToPowerPill;
    }
}
