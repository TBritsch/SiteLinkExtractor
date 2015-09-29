package com.tb.ba.analysis.changenew;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * Gibt die größten "Gewinner" und "Verlierer" aus.
 * Created by T. Britsch on 06.09.2015.
 */
public class PChanged {

    public static final int ORDER_ABS = 0;
    public static final int ORDER_REL = 1;
    public static int CONFIG_ORDER = ORDER_REL;
    public static int CONFIG_LENGTH = 100;
    public static String CONFIG_FILTER;


    public static void main(String[] args) {

        LinesMap linesMap = new LinesMap();
        ArrayList<Line> ordered = new ArrayList<>();

        String str_order = (args.length > 2) ? (args[2]) : ("");
        CONFIG_LENGTH = Integer.parseInt((args.length > 3) ? (args[3]) : ("100"));
        CONFIG_FILTER = (args.length > 4) ? (args[4]) : ("");

        switch (str_order.toLowerCase()){//config
            case "abs": CONFIG_ORDER = ORDER_ABS;
                        break;
            case "rel": CONFIG_ORDER = ORDER_REL;
                        break;
            default:    CONFIG_ORDER = ORDER_REL;


        }





        String thisLine;
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(args[0])
                    )
            );

            while ((thisLine = br.readLine()) != null) {

                String [] line = thisLine.split("\t");

                Line l = new Line(line[line.length-2]);
                l = linesMap.put(l);
                if(l != null){
                    l.setVal1(Double.parseDouble(line[line.length - 1]));
                }



            }




            br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(args[1])
                    )
            );

            while ((thisLine = br.readLine()) != null) {

                String [] line = thisLine.split("\t");

                Line l = new Line(line[line.length-2]);
                l = linesMap.put(l);

                if(l != null){
                    l.setVal2(Double.parseDouble(line[line.length - 1]));
                }

            }


        }
        catch (IOException e) {
            System.err.println("Error: " + e);
        }

        for (Line line: linesMap.map.values()){
            if (line.getDiff() != null){
                ordered.add(line);
            }
        }

        Collections.sort(ordered);
        Collections.reverse(ordered);

        System.out.println("("+args[0]+"|"+args[1]+")");
        for (int i = Math.min(CONFIG_LENGTH, ordered.size()); i >= 0; i--){
            Line line = ordered.get(i);
            System.out.println(line.getUrl() + ": " + line.getDiff() + " ("+line.getVal1()+"|"+line.getVal2()+")");
        }



    }


}
