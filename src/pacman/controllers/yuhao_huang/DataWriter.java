package pacman.game;

import pacman.game.Constants.MOVE;
import java.*;
import java.io.*;


/**
 *
 * Store Game state data with .txt file
 * Created by Yuhao on 4/14/16.
 */
public class DataWriter {

    /**
     * Store game state to the specific directory
     * @param game
     * @param pacManMove
     */
    public void printGameInfo(Game game, MOVE pacManMove) {

        // System.out.println("Game State: " + game.getGameState());
        // System.out.println("Score: " + game.getScore());
        // System.out.println("Time: " + game.getTotalTime());

        int currPacLocation = game.getPacmanCurrentNodeIndex(); // get current pacMan
        int ghostLocation = game.getGhostCurrentNodeIndex(Constants.GHOST.BLINKY);

        // System.out.println("Distance: " + game.getEuclideanDistance(currPacLocation, ghostLocation));
        // System.out.println("Current Index: " + game.getPacmanCurrentNodeIndex());
        // System.out.println("Move: " + pacManMove);


        // directory to store training data
        String path = "/Users/hyh0221/Documents/4th semester/Artificial Intelligence/Training data/data8.txt";
        File file = new File(path);

        try {
            PrintWriter pw = new PrintWriter(new FileWriter(file, true));

            /*--------Write DataPoint to a text file-------*/
//            DataPoint dataPoint = new DataPoint(game);
//            pw.print(dataPoint.getDistToPill() + " "); // get distance to pill
//
//            pw.print(dataPoint.getDistToPowerPill() + " "); // get distance to power pill
//            pw.print(dataPoint.getDistToGhost() + " "); // get distance to ghost
//
//            pw.println(pacManMove); // move
//
//            pw.close();
             /*----------------------*/


            pw.print(game.getScore() + " "); // get current score

            pw.print(game.getTotalTime() + " "); // get time
            pw.print(game.getEuclideanDistance(currPacLocation, ghostLocation) + " "); // get distance
            pw.print(game.getPacmanCurrentNodeIndex() + " "); // get current index
            pw.println(pacManMove); // move

            pw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is for test
     * @param args
     */
    public static void main(String[] args) {

        return;
    }

}
