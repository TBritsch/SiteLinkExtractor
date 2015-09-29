package com.tb.ba.analysis.grep;

import com.bethecoder.ascii_table.ASCIITable;

import java.util.ArrayList;



/**
 * Durchsucht mehrere Dateien und gibt die verschiedenen PageRank-Ergebnisse in einer Tabelle aus.
 * Created by T. Britsch on 26.08.2015.
 */
public class Pgrep {
    static String searchterm;
    static ArrayList<TtlFile> files = new ArrayList<>();


    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-h")) {
                System.out.println("################################");
                System.out.println("################################");
                System.out.println("Pcompare lists a number of lines from the PageRank-Files combining both files " +
                        "while using the url as a key.");
                System.out.println("################################");
                System.out.println("################################");
                System.out.println("How to use:");
                System.out.println("");
                System.out.println("PGrep searchterm /path/to/first/file /path/to/other/file ...");
                System.out.println("################################");
                System.out.println("################################");


                System.exit(0);
            }

        }

        searchterm = args[0];

        System.out.println("Suche nach: " + searchterm);


        for (int i = 1; i < args.length; i++) {
            files.add(new TtlFile(args[i]));
        }

        for (TtlFile file : files) {
            file.setResult(FileSearch.search(file.getName(), searchterm));
        }


        ArrayList<String> header = new ArrayList<String>();
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        ArrayList<String> dataLine = new ArrayList<String>();
        ArrayList<String> textLine = new ArrayList<String>();

        for (TtlFile file : files) {
            header.add(file.getName());
            dataLine.add(file.getResult().getPr());
            textLine.add(file.getResult().getPage());
        }

        data.add(dataLine);
        data.add(textLine);

        String[][] data_ar = new String[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            ArrayList<String> row = data.get(i);
            data_ar[i] = row.toArray(new String[row.size()]);
        }


        //https://code.google.com/p/java-ascii-table/
        ASCIITable.getInstance().printTable(header.toArray(new String[header.size()]), data_ar);

    }

}
