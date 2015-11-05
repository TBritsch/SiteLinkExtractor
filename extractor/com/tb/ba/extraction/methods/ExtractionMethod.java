package com.tb.ba.extraction.methods;

import com.tb.ba.extraction.Extractor;
import com.tb.ba.extraction.datastructure.Article;
import com.tb.ba.extraction.datastructure.Link;
import com.tb.ba.extraction.writer.TupleElement;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Abstrakte Klasse einer Extraktionsmethode.
 * Created by T. Britsch on 13.07.2015.
 */
public abstract class ExtractionMethod {

    private PrintWriter writer;
    private HashSet<TupleElement> hashSet = new HashSet<>();

    public ExtractionMethod(){
        try {
            this.writer = new PrintWriter(Extractor.EXTRACTION_FILENAMES.replace("#method#", this.getMethodName()), "UTF-8");

        } catch (FileNotFoundException e) {
            if(Extractor.DEBUG){
                System.out.println("ExtractionMethode");
                e.printStackTrace();
            }
        } catch (UnsupportedEncodingException e) {
            if(Extractor.DEBUG){
                System.out.println("ExtractionMethode");
                e.printStackTrace();
            }
        }
    }

    /**
     * Extrahiere die Links aus einem Artikel.
     * @param article der Artikel, aus dem die Links zu extrahieren sind.
     */
    public abstract void extract(Article article);


    /**
     * Gibt den Namen der jeweiligen Methode zurück. Dieser wird im Dateinamen verwendet, um die Dateinamen für die
     * Ergebnisse zu erzeugen.
     * @return Der Name der Methode.
     */
    public abstract String getMethodName();


    /**
     * Schreibt sämtliche Elemente in die Datei.
     */
    public void writeElements(){
        for (TupleElement tupleElement: hashSet){
            //System.out.println(tupleElement.toString());
            writer.println(tupleElement.toString());
            //if(tupleElement.toString().contains("\\\\")){
            //    System.out.println(tupleElement.toString());
            //}
        }

        hashSet = new HashSet<>();
    }

    /**
     * Fügt ein TupleElement hinzu, welches im Anschluss gespeichert wird.
     * @param element Hinzuzufügendes TupleElement.
     */
    protected void addElement(TupleElement element){
        if(element.toString() !=  null){
            hashSet.add(element);
        }
    }


    /**
     * Beendet den Schreibprozess in die Datei.
     */
    public void closeWriter(){
        writer.close();
    }


}
