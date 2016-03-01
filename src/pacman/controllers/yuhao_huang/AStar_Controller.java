package pacman.controllers.yuhao_huang;

import java.util.*;

import pacman.controllers.Controller;
import pacman.controllers.examples.StarterGhosts;
import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.Constants.MOVE;
import pacman.game.internal.Node;
import pacman.game.internal.Maze;

/**
 *
 */
public class AStar_Controller extends Controller<MOVE> {

    public static StarterGhosts ghosts = new StarterGhosts();
    private N[] graph;
//    private static Constants.MOVE lastMove = MOVE.NEUTRAL;
    private static int pillIndex = 0;
    private static int ithMove = 0;
    private MOVE[] path = {MOVE.UP};

    public AStar_Controller(int pillInd, int ithMov) {
        pillIndex = pillInd;
        ithMove = ithMov;

    }
    public void createGraph(Node[] nodes)
    {
        graph=new N[nodes.length];

        //create graph
        for(int i=0;i<nodes.length;i++)
            graph[i]=new N(nodes[i].nodeIndex);

        //add neighbours
        for(int i=0;i<nodes.length;i++)
        {
            EnumMap<Constants.MOVE,Integer> neighbours=nodes[i].neighbourhood;
            Constants.MOVE[] moves= Constants.MOVE.values();

            for(int j=0;j<moves.length;j++)
                if(neighbours.containsKey(moves[j]))
                    graph[i].adj.add(new E(graph[neighbours.get(moves[j])],moves[j],1));
        }
    }

    public Constants.MOVE getMove(Game game, long timeDue) {
        Maze currentMaze = game.getCurrentMaze();
        Node[] mazeNode = currentMaze.graph;
        // Create the maze of the game
        createGraph(mazeNode);

        int[] pillIndexes = currentMaze.pillIndices;
        int lengthPill = pillIndexes.length;
        Random rnd = new Random();
        Constants.MOVE[] allMoves = Constants.MOVE.values();

//        int highScore = -1;
        Constants.MOVE highMove = null;


        //System.out.println("Trying Move: " + m);


        if(ithMove >= path.length) { // The path has been finished, now start to find a new path
            ithMove = 0;
            path = this.AStar_FindPath(game.getPacmanCurrentNodeIndex(), pillIndexes[pillIndex++], this.getMove(), game);
            highMove = path[ithMove];
        } else {
            highMove = path[ithMove];
            Game gameCopy = game.copy();
            Game gameAtM = gameCopy;
            gameAtM.advanceGame(highMove, ghosts.getMove(gameAtM, timeDue));

            if (gameAtM.getPacmanCurrentNodeIndex() == game.getPacmanCurrentNodeIndex()) {
                MOVE tempMove = allMoves[rnd.nextInt(5)];
                while(tempMove == highMove) {
                    tempMove = allMoves[rnd.nextInt(5)];
                }
                highMove = tempMove;
            }

            ithMove++;
        }


//        System.out.println("Trying Move: " + highMove + ", Score: " + path);

        lastMove = highMove;


//        System.out.println("High Score: " + highScore + ", High Move:" + highMove);

        return highMove;
    }

    // A* algorithm implementation
    public MOVE[] AStar_FindPath(int s, int t, Constants.MOVE lastMoveMade, Game game) {
        Constants.MOVE[] allMoves = Constants.MOVE.values();
        int highScore = -1;

        N start = graph[s];
        N target = graph[t];

        PriorityQueue<N> open = new PriorityQueue<N>();
        ArrayList<N> closed = new ArrayList<N>();

        start.g = 0;
        start.h = game.getShortestPathDistance(start.index, target.index);

        start.reached=lastMoveMade;

        open.add(start);

        while(!open.isEmpty())
        {
            N currentNode = open.poll();
            closed.add(currentNode);

            if(currentNode.isEqual(target))
                break;

            for(E next : currentNode.adj)
            {
                if(next.move != currentNode.reached.opposite())
                {
                    double currentDistance = next.cost;

                    if (!open.contains(next.node) && !closed.contains(next.node))
                    {
                        next.node.g = currentDistance + currentNode.g;
                        next.node.h = game.getShortestPathDistance(next.node.index, target.index);
                        next.node.parent = currentNode;

                        next.node.reached=next.move;

                        open.add(next.node);
                    }
                    else if (currentDistance + currentNode.g < next.node.g)
                    {
                        next.node.g = currentDistance + currentNode.g;
                        next.node.parent = currentNode;

                        next.node.reached = next.move;

                        if (open.contains(next.node))
                            open.remove(next.node);

                        if (closed.contains(next.node))
                            closed.remove(next.node);

                        open.add(next.node);
                    }
                }
            }
        }

        return extractPath(target);
    }
    private MOVE[] extractPath(N target)
    {
        ArrayList<MOVE> route = new ArrayList<MOVE>();
        N current = target;
        route.add(current.reached);

        while (current.parent != null)
        {
            route.add(current.parent.reached);
            current = current.parent;
        }

        Collections.reverse(route);

        MOVE[] routeArray=new MOVE[route.size()];

        for(int i=0;i<routeArray.length;i++)
            routeArray[i]=route.get(i);

        return routeArray;
    }


}

