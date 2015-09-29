package com.tb.ba.analysis.avgpage;

import com.tb.ba.analysis.diff.Line;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Berechnet das arithmetische Mittel bezüuglich der Anzahl der ausgehenden Seiten-Links pro
 Artikel
 * Created by T. Britsch on 09.09.2015.
 */
public class PageAvg {
    public static void main(String[] args) {

        String thisLine;
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(args[0])));
            String actUrl = "";
            ArrayList<Integer> arrayList = new ArrayList<>();
            int counter = 1;
            while ((thisLine = br.readLine()) != null) { // while loop begins her
                String thisUrl = thisLine.split("> <")[0];

                if(thisUrl.equals(actUrl)){
                    counter++;
                }else{
                    arrayList.add(counter);
                    counter = 1;
                    actUrl = thisUrl;
                }


            } // end while

            System.out.println("AVG:" + avg(arrayList));
            System.out.println("STD: " + std(arrayList));
        } // end try

        catch (IOException e) {
            System.err.println("Error: " + e);
        }
    }


    public static double avg(ArrayList<Integer> list)
    {
        double avg = 0.0;
        for (Integer count: list){
            avg += count;
        }
        avg /= list.size();
        return avg;
    }


    public static double std(ArrayList<Integer> list)
    {
        double sdev = 0.0;
        double avg = avg(list);

        for (Integer count: list){
            sdev += (count - avg) * (count - avg);
        }

        sdev = Math.sqrt(sdev / (list.size()));
        return sdev;
    }

}
