package pacman.controllers.yuhao_huang;

import pacman.game.Constants;

/**
 * Created by hyh0221 on 4/14/16.
 */

/**
 * This is a transition node between two StateNodes. It contains Q value and action we need.
 */
public class TransitionNode {
    public float QValue;
    public Constants.MOVE move;
    public StateNode fromState,toState;
    public TransitionNode(StateNode fromState, Constants.MOVE move, StateNode toState) {
        this.fromState = fromState;
        this.move = move;
        this.toState = toState;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TransitionNode other = (TransitionNode) obj;
        if (fromState == null) {
            if (other.fromState != null)
                return false;
        } else if (!fromState.equals(other.fromState))
            return false;
//		if (move != other.move)
//			return false;
        if (toState == null) {
            if (other.toState != null)
                return false;
        } else if (!toState.equals(other.toState))
            return false;
        return true;
    }
}

