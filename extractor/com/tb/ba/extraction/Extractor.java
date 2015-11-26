package com.tb.ba.extraction;

import com.tb.ba.extraction.extraction.SplitterThread;
import com.tb.ba.extraction.filter.Filter;
import com.tb.ba.extraction.methods.*;
import com.tb.ba.extraction.parsing.ParserThread;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Hauptklasse des Programms.
 */
public class Extractor {
    public static final boolean DEBUG = false;
    public static final ConcurrentLinkedQueue<String> parseQueue = new
            ConcurrentLinkedQueue<String>();
    public static final String EXTRACTION_FILENAMES =
            "extraction_#method#.nt";
    public static boolean splitting = true, extracting = true;
    public static ArrayList<ExtractionMethod> methods;
    public static SplitterThread splitterThread;

    public static String config_filename_input = "";
    public static String config_language = "";


    /**
     * Führt die Extraktion aus.
     * Das Programm endet mit der Extraktion des letzten Artikels.
     *
     * @param args [0] = Sprachversion des zu extrahierenden Wikis
     *             [1] = Pfad zur bz2-Datei
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Wrong Parameters!");
            System.exit(0);
        }

        config_language = args[0];
        config_filename_input = args[1];
        new Extractor();
    }


    /**
     * Führt die Extraktion aus, indem die verschiedenen Extraktionsmethoden gestartet werden und die verschiedenen
     * Threads gestartet werden.
     */
    public Extractor() {
        Filter.init();


        methods = new ArrayList<>();
        methods.add(new ExtractNormal());
        methods.add(new ExtractAbstract());
        methods.add(new ExtractNoTemplate());
        methods.add(new ExtractNoLinksInInfobox());
        methods.add(new ExtractInfobox());
        methods.add(new ExtractRelativePosition());


        splitterThread = new SplitterThread();
        new Thread(splitterThread).start();

        ParserThread p = new ParserThread();
        new Thread(p).start();

        WatchdogThread watchdogThread = new WatchdogThread();
        new Thread(watchdogThread).start();


    }


}
