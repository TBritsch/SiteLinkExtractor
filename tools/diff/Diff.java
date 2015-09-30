package com.tb.ba.analysis.diff;

import java.io.*;
import java.util.HashSet;

/**
 * Created by T. Britsch on 31.08.2015.
 */
public class Diff {

    public static void main(String[] args) {


        String thisLine;

        HashSet<Line> hashSet = new HashSet<>();

        //erste Datei einlesen
        try {

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(args[0])));
            while ((thisLine = br.readLine()) != null) {
                hashSet.add(new Line(thisLine, "f1"));
            }
        }
        catch (IOException e) {
            System.err.println("Error: " + e);
        }

        //zweite Datei einlesen
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(args[1])));
            while ((thisLine = br.readLine()) != null) {
                if(hashSet.contains(new Line(thisLine, "f2"))){
                    hashSet.remove(new Line(thisLine, "f2"));
                }else{
                    hashSet.add(new Line(thisLine, "f2"));

                }
            }
        }
        catch (IOException e) {
            System.err.println("Error: " + e);
        }


        //Gebe alle gesammelten Änderungen aus.
        for (Line line: hashSet){
            System.out.println(line);
        }

    }
}
