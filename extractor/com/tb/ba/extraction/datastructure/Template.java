package com.tb.ba.extraction.datastructure;

/**
 * Repräsentiert ein Template eines Artikels
 * Created by T. Britsch on 20.08.2015.
 */
public class Template implements Comparable<Template> {

    private int id;
    private int start, end;
    private String content;


    /**
     * Konstruktor der Klasse
     *
     * @param id      Eindeutige ID des Templates
     * @param start   Position des Startes des Templates im Text
     * @param end     Position des Endes des Templates im Text
     * @param content Inhalt des Templates
     */
    public Template(int id, int start, int end, String content) {
        this.content = content;
        this.end = end;
        this.id = id;
        this.start = start;
    }


    /**
     * Gibt den Content des Templates zurück.
     *
     * @return der Content
     */
    public String toString() {
        return this.content;
    }


    /**
     * Gibt die Länge des Templates zurück.
     *
     * @return Die Länge des Templates.
     */
    public int getLength() {
        return this.end - this.start;
    }

    @Override
    public int compareTo(Template o) {
        return Integer.compare(this.start, o.start);
    }

    /**
     * Gibt die Endposition des Templates zurück.
     *
     * @return Die Endposition des Templates.
     */
    public int getEnd() {
        return end;
    }

    /**
     * Setzt die Endposition des Templates.
     *
     * @param end Die Endposition des Templates.
     */
    public void setEnd(int end) {
        this.end = end;
    }

    /**
     * Gibt die Startposition des Templates zurück.
     *
     * @return Die Startposition des Templates.
     */
    public int getStart() {
        return start;
    }


    /**
     * Setzt die Startposition des Templates.
     *
     * @return Die Startposition des Templates.
     */
    public void setStart(int start) {
        this.start = start;
    }


    /**
     * Gibt den Content des Templates zurück.
     *
     * @return Der Content de Templates.
     */
    public String getContent() {
        return content;
    }


    /**
     * Speichert den Content des Templates.
     *
     * @param content Der Content des Templates.
     */
    public void setContent(String content) {
        this.content = content;
    }
}
