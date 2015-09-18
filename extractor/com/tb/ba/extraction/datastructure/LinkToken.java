package com.tb.ba.extraction.datastructure;

/**
 * Dient zur Realisation eines Tokens, welches einen Link enthält.
 * Created by T. Britsch on 10.08.2015.
 */
public class LinkToken {
    private String link;
    private int position;

    /**
     * Konstruktor der Klasse
     *
     * @param link     Der Linktext des Tokens
     * @param position Position des Tokens
     */
    public LinkToken(String link, int position) {
        this.link = link;
        this.position = position;
    }


    /**
     * Gibt Position des Tokens zurück
     *
     * @return Position des Tokens
     */
    public int getPosition() {
        return position;
    }

    /**
     * Speichert die Position des Tokens
     *
     * @param position Die Position des Tokens
     */
    public void setPosition(int position) {
        this.position = position;
    }


    /**
     * Gibt den Link zurück.
     *
     * @return Link des Tokens
     */
    public String getLink() {
        return link;
    }

    /**
     * Setzt den Link des Tokens.
     *
     * @param link Der Link des Tokens.
     */
    public void setLink(String link) {
        this.link = link;
    }
}
