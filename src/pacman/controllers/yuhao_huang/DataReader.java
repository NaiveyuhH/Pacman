package pacman.game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import pacman.game.Constants.MOVE;
import java.util.*;

/**
 * Read data (Game state) from directory
 * Created by Yuhao on 4/14/16.
 */
public class DataReader {

    /**
     * Read game state data set, .txt file
     * @param path
     * @return
     */
    public List<DataPoint> read (String path) {

        List<DataPoint> dataList = new ArrayList<DataPoint>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));

            StringBuffer sb = new StringBuffer();
            String line = br.readLine();

            while (line != null) {

                // convert data to corresponding data type
                DataPoint dataPoint = new DataPoint();
                String[] data = line.split(" ");

                dataPoint.setScore(Integer.parseInt(data[0]));
                dataPoint.setTime(Integer.parseInt(data[1]));
                dataPoint.setDistance(Double.parseDouble(data[2]));
                dataPoint.setIndex(Integer.parseInt(data[3]));
                dataPoint.setMove(data[4]);

                dataList.add(dataPoint);

                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
//            String fileData = sb.toString();
            // System.out.print(fileData);

            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataList;
    }



}

