package com.tb.ba.extraction.extraction;

import com.tb.ba.extraction.Extractor;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import java.io.*;

/**
 * Liest eine bzip2-komprimierte XML-Datei ein und Splittet einzelne Artikel aus dieser auseinander und speichert
 * diese dann in einer Queue.
 * Created by T. Britsch on 02.07.2015.
 */
public class SplitterThread implements Runnable {

    private volatile boolean running = true;

    @Override
    public void run() {
        FileInputStream in = null;
        try {
            in = new FileInputStream(Extractor.config_filename_input);

            BZip2CompressorInputStream bzIn = new BZip2CompressorInputStream(in);
            InputStreamReader inputStreamReader = new InputStreamReader(bzIn, "UTF-8");
            BufferedReader br = new BufferedReader(inputStreamReader);
            String thisLine;
            StringBuffer actArticle = new StringBuffer();
            boolean recording = false;
            int i = 0;
            while ((thisLine = br.readLine()) != null) { // while loop begins here
                if (this.running) {

                    if (thisLine.contains("<page>")) {//neuer artikel beginnt
                        recording = true;
                    }

                    i++;

                    if (i % 100000 == 0) {
                        //System.out.println("XMLSPlitter: (" + "Line " + i + "): " + thisLine);
                    }

                    if (recording) {
                        actArticle.append(thisLine);
                        actArticle.append("\n");
                    }

                    if (thisLine.contains("</page>")) {
                        recording = false;

                        if (!actArticle.equals("")) {
                            Extractor.parseQueue.add(actArticle.toString());
                        }


                        actArticle = new StringBuffer();
                    }

                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        if (Extractor.DEBUG) {
                            System.out.println("SplitterThread");
                            e.printStackTrace();
                        }
                    }
                }
            }

            br.close();
            inputStreamReader.close();
            bzIn.close();

            Extractor.splitting = false;
        } catch (FileNotFoundException e) {
            if (Extractor.DEBUG) {
                System.out.println("SplitterThread");
                e.printStackTrace();
            }
        } catch (IOException e) {
            if (Extractor.DEBUG) {
                System.out.println("SplitterThread");
                e.printStackTrace();
            }
        }


    }

    /**
     * pausiert die Extraktion.
     *
     * @throws InterruptedException
     */
    public void pauseThread() throws InterruptedException {
        running = false;
    }


    /**
     * Führt die Extraktion fort.
     */
    public void resumeThread() {
        running = true;
    }

    /*
    Aktueller Zustand der Extraktion.
     */
    public boolean isRunning() {
        return running;
    }


}
