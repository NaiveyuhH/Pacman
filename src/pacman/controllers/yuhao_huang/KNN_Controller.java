package pacman.controllers.yuhao_huang;


import pacman.controllers.Controller;
import pacman.game.*;
import pacman.game.Constants.MOVE;

import java.util.*;

/**
 *  @Title Hmk4, CS5100/4100 Artificial Intelligence, K Nearest Neighbors
 *  @Author Yuhao Huang
 *  @Date 4/14/2016
 */
public class KNN_Controller extends Controller {

    int k = 5; // define k value

    // data sets path
    String path1 = "/Users/hyh0221/Documents/4th semester/Artificial Intelligence/Training data/data2.txt";

    // read data sets
    DataReader dataReader = new DataReader();

    // store data sets to the list
    public List<DataPoint> dataSet1 = dataReader.read(path1);

    // all moves
    MOVE left = MOVE.LEFT;
    MOVE right = MOVE.RIGHT;
    MOVE up = MOVE.UP;
    MOVE down = MOVE.DOWN;
    MOVE neutral = MOVE.NEUTRAL;


    /**
     * KNN Algorithm
     * @param game A copy of the current game
     * @param timeDue The time the next move is due
     * @return
     */
    @Override
    public MOVE getMove(Game game, long timeDue) {

        MOVE[] allMoves = MOVE.values();

        int size = dataSet1.size();
        double distance = 0;

        // get current index of pacman
        int currPacLocation = game.getPacmanCurrentNodeIndex();
        // get current index of ghost
        int ghostLocation = game.getGhostCurrentNodeIndex(Constants.GHOST.BLINKY);

        // convert data type to DataPoint
        DataPoint dp = new DataPoint();

        dp.setScore(game.getScore());
        dp.setTime(game.getTotalTime());
        dp.setDistance(game.getEuclideanDistance(currPacLocation, ghostLocation));
        dp.setIndex(game.getPacmanCurrentNodeIndex());


        Map<Double, Integer> map = new HashMap<>();

        // calculate euclidean distance for all data points
        for (int i = 0; i < size; i++) {

            DataPoint di = dataSet1.get(i);
            distance = computeDistance(di, dp);
           // System.out.println("distance:" + distance);
            map.put(distance, i);
        }

        // sort distance with ascending order and also keep the index
        SortedSet<Double> set = new TreeSet<>(map.keySet());

        int[] res = new int[k];
        int count = 0;

        // get k nearest index with the distance
        for (Double i : set) {
            res[count] = map.get(i);
//            System.out.println("res[count]: " + res[count]);
            count++;
            if (count == k) {
                break;
            }
        }

        // get index and moves
        String[] moves = new String[k];

        for (int i = 0; i < res.length; i++) {
             moves[i] = dataSet1.get(res[i]).getMove();
//             System.out.println("moves[i]:" + moves[i]);
        }

        return findMajority(moves, game);
    }



    /**
     * Find majority count decide next move
     * @param moves
     * @return
     */
    public MOVE findMajority(String[] moves, Game game) {
        Random random = new Random();
        // initialize count value
        int[] count = new int[5];
        int[] countOfMoves = new int[5];
        MOVE[] Moves = new MOVE[5];
        Moves[0] = MOVE.LEFT;
        Moves[1] = MOVE.RIGHT;
        Moves[2] = MOVE.UP;
        Moves[3] = MOVE.DOWN;
        Moves[4] = MOVE.NEUTRAL;

        for (int i = 0; i < moves.length; i++) {
            if (moves[i].equals("LEFT")) {
                count[0]++;
                countOfMoves[0]++;
            } else if (moves[i].equals("RIGHT")) {
                count[1]++;
                countOfMoves[1]++;
            } else if (moves[i].equals("UP")) {
                count[2]++;
                countOfMoves[2]++;
            } else if (moves[i].equals("DOWN")) {
                count[3]++;
                countOfMoves[3]++;
            } else if (moves[i].equals("NEUTRAL")) {
                count[4]++;
                countOfMoves[4]++;
            }
        }

        // find the maximum value of count
        Arrays.sort(count);


        // return next move
        for(int i = count.length - 1; i >= 0; i--) {
            for(int j = 0; j < countOfMoves.length; j++) {

                if(count[i] == countOfMoves[j]) {
//                      return Moves[j];
                    MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex());

                    for(MOVE m : possibleMoves) {
                        System.out.println(m);

                        if(m.equals(Moves[j])) {
                            if(game.getPacmanLastMoveMade() == m.opposite())
                                return possibleMoves[random.nextInt(possibleMoves.length)];
                            return Moves[j];
                        };
                    }

                }
            }
        }


        return null;
    }


    /**
     * Calculate euclidean distance
     * @param d1
     * @param d2
     * @return
     */
    public double computeDistance(DataPoint d1, DataPoint d2) {

        double dPow = Math.pow((d1.getDistance() - d2.getDistance()), 2); // distance
        double tPow = Math.pow((d1.getTime() - d2.getTime()), 2); // time
        double sPow = Math.pow((d1.getScore() - d2.getScore()), 2); // score
        double iPow = Math.pow((d1.getIndex() - d2.getIndex()), 2); // current index

        return Math.sqrt(dPow + tPow + sPow + iPow);
    }

}


