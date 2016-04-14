package pacman.game;

/**
 * Data for the game state
 * Created by shengchen on 4/13/16.
 */
public class DataPoint {

    private int distToPill;
    private int distToPowerPill;
    private int distToGhost;

    public DataPoint() {}
    public DataPoint(Game game) {
        int current = game.getPacmanCurrentNodeIndex();
        int[] pills=game.getActivePillsIndices();
        int[] powerPills=game.getActivePowerPillsIndices();

        this.distToPill = Integer.MAX_VALUE;

        for(int i=0;i<pills.length;i++){				//find the nearest pill
            int tempDistance = game.getManhattanDistance(current, pills[i]);
            this.distToPill = tempDistance < this.distToPill ? tempDistance : this.distToPill;
        }

        this.distToPowerPill = Integer.MAX_VALUE;

        for(int i=0;i<powerPills.length;i++){				//find the nearest power pill
            int tempDistance = game.getManhattanDistance(current, powerPills[i]);
            this.distToPowerPill = tempDistance < this.distToPowerPill ? tempDistance : this.distToPowerPill;
        }

        Constants.GHOST[] ghosts = Constants.GHOST.values();

        this.distToGhost = Integer.MAX_VALUE;

        for(Constants.GHOST ghost : ghosts){						//find the nearest ghost and whether it is edible
            int tempDistance = game.getShortestPathDistance(current, game.getGhostCurrentNodeIndex(ghost));
            if(tempDistance < this.distToGhost){
                this.distToGhost = tempDistance;
//                this.islair = game.getGhostEdibleTime(ghost)>0;
            }
        }
    }


    public int getDistToPill() {
        return distToPill;
    }

    public void setDistToPill(int distToPill) {
        this.distToPill = distToPill;
    }

    public int getDistToPowerPill() {
        return distToPowerPill;
    }

    public void setDistToPowerPill(int distToPowerPill) {
        this.distToPowerPill = distToPowerPill;
    }

    public int getDistToGhost() {
        return distToGhost;
    }

    public void setDistToGhost(int distToGhost) {
        this.distToGhost = distToGhost;
    }

    int score;
    int time;
    double distance;
    int index;
    String move;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }
}
