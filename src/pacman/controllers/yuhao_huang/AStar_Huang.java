package pacman.controllers.yuhao_huang;

import pacman.game.Constants;
import pacman.game.Game;
import pacman.game.internal.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.PriorityQueue;

/**
 * AStar class that has the information needed for AStar_Controller
 */
public class AStar_Huang {
    private N[] graph;


    public void resetGraph()
    {
        for(N node : graph)
        {
            node.g=0;
            node.h=0;
            node.parent=null;
            node.reached=null;
        }
    }
}


class N implements Comparable<N>
{
    public N parent;
    public double g, h;
    public boolean visited = false;
    public ArrayList<E> adj;
    public int index;
    public Constants.MOVE reached=null;

    public N(int index)
    {
        adj = new ArrayList<E>();
        this.index=index;
    }

    public N(double g, double h)
    {
        this.g = g;
        this.h = h;
    }

    public boolean isEqual(N another)
    {
        return index == another.index;
    }

    public String toString()
    {
        return ""+index;
    }

    public int compareTo(N another)
    {
        if ((g + h) < (another.g + another.h))
            return -1;
        else  if ((g + h) > (another.g + another.h))
            return 1;

        return 0;
    }
}

class E
{
    public N node;
    public Constants.MOVE move;
    public double cost;

    public E(N node, Constants.MOVE move, double cost)
    {
        this.node=node;
        this.move=move;
        this.cost=cost;
    }
}

