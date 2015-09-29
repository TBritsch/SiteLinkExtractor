package com.tb.ba.analysis.prSplitter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Splittet einen PR-Wert auf, und zeigt die Zusammensetzung von diesem an.
 * Created by T. Britsch on 11.09.2015.
 */
public class PRSplitter {
    public static void main(String[] args) {

        String pattern = args[2];
        ArrayList<Result> elements = getElements(args[0], pattern);

        for (Result element : elements) {
            String name = element.getName();
            name = name.replaceAll("<http://.*?dbpedia.org/resource/", "");
            name = name.replace(">", "");

            element.setName(name);
        }


        addPageRanks(args[1], elements);

        Collections.sort(elements);

        for (Result element : elements) {
            System.out.println(element);
        }


    }

    public static ArrayList<Result> getElements(String filename, String pattern) {
        ArrayList<Result> elements = new ArrayList<>();
        String thisLine;
        try {

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(filename)));
            while ((thisLine = br.readLine()) != null) {
                String[] splitted = thisLine.split(" ");
                if (splitted[splitted.length - 2].contains(pattern)) {
                    elements.add(new Result(splitted[0]));
                }

            }
        }
        catch (IOException e) {
            System.err.println("Error: " + e);
        }

        return elements;
    }

    public static ArrayList<Result> addPageRanks(String filename, ArrayList<Result> elements) {

        String thisLine;
        try {

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(filename)));
            while ((thisLine = br.readLine()) != null) {

                for (Result element : elements) {
                    if (thisLine.contains("/resource/" + element.getName() + "\t")) {
                        String[] splitted = thisLine.split("\t");
                        element.setPagerank(Double.parseDouble(splitted[splitted.length - 1]));

                    }
                }
            }
        }
        catch (IOException e) {
            System.err.println("Error: " + e);
        }

        return elements;
    }
}
