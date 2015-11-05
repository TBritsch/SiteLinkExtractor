package com.tb.ba.extraction;

import com.tb.ba.extraction.datastructure.Article;
import com.tb.ba.extraction.datastructure.Link;
import com.tb.ba.extraction.extraction.XMLParser;
import com.tb.ba.extraction.methods.ExtractionMethod;
import com.tb.ba.extraction.writer.StdTupleElement;

import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Prüft, ob alle Prozesse so laufen, wie sie es sollen und ob der speicher des Rechners nicht zu sehr beansprucht
 * wird. In diesem Fall wird die Extraktion pausiert und die zu extrahierenden Artikel werden abgearbeitet.
 * Created by T. Britsch on 02.07.2015.
 */
public class WatchdogThread implements Runnable {

    int lastQueueSize = 0; //Anzahl der Jobs bei der letzten Iteration

    @Override
    public void run() {
        int i = 0;
        while (true) {
            i++;
            if (i % 50 == 0) {

                lastQueueSize = Extractor.parseQueue.size();
                //System.out.println("WatchdogThread: (" + i + ") (Splitting: " + Extractor.splitting + "): " +
                // Extractor.parseQueue.size());
            }


            //Cache zu voll
            if (Extractor.parseQueue.size() > 1000 && Extractor.splitterThread.isRunning()) {
                try {
                    Extractor.splitterThread.pauseThread();
                    //System.out.println("Pausing Splitter: " + i);
                } catch (InterruptedException e) {
                    if (Extractor.DEBUG) {
                        System.out.println("WatchdogThread");
                        e.printStackTrace();
                    }

                }
            }


            //Wieder leeren
            if (!Extractor.splitterThread.isRunning() && Extractor.parseQueue.size() < 100) {
                Extractor.splitterThread.resumeThread();
                //System.out.println("Resumeing Splitter: " + i);
            }
            try {
                if (!Extractor.extracting && !Extractor.splitting && Extractor.parseQueue.isEmpty()) {
                    //Alles gemacht

                    Thread.sleep(30000);
                    for (ExtractionMethod method : Extractor.methods) {
                        method.closeWriter();
                    }
                    System.exit(-1);
                }
                Thread.sleep(400);
            } catch (InterruptedException e) {
                if (Extractor.DEBUG) {
                    System.out.println("WatchdogThread");
                    e.printStackTrace();
                }
            }
        }

    }

}
