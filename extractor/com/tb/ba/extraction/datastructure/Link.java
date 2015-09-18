package com.tb.ba.extraction.datastructure;

import com.sun.xml.internal.ws.util.StringUtils;
import com.tb.ba.scala.TurtleEscaper;

/**
 * Repräsentiert einen Link in einem Wiki-Text
 * Created by T. Britsch on 12.07.2015.
 */
public class Link implements Comparable<Link> {


    /**
     * Das Elternelement ist unbekannt/kann nicht ermittelt werden.
     */
    public static final int PARENT_UNDEFINED = 0;

    /**
     * Das Elternelement ist der Artikeltext.
     */
    public static final int PARENT_ARTICLE = 1;

    /**
     * Das Elternelement ist ein Template.
     */
    public static final int PARENT_TEMPLATE = 2;

    /**
     * Das Elternelement ist eine Datei-Linkumgebung.
     */
    public static final int PARENT_FILE = 3;

    public int parent, start, end;
    private String fieldtext, url;


    /**
     * Erstellt einen Link.
     *
     * @param url    die URL des Links
     * @param parent Typ des Eltern-Elements
     */
    public Link(String url, int parent) {
        this.parent = parent;
        this.url = cleanURL(url);
    }

    /**
     * Erstellt einen Link.
     *
     * @param url    die URL des LINKs
     * @param parent Typ des Eltern-Elements
     * @param start  Start des Links im Text
     * @param end    Ende des Links im Text
     */
    public Link(String url, int parent, int start, int end) {
        this.parent = parent;
        this.url = cleanURL(url);
        this.start = start;
        this.end = end;
    }

    /**
     * Erstellt einen Link.
     *
     * @param fieldtext Der Beschreibungstext des Links.
     * @param url       Die URL des Links.
     * @param parent    Typ des Eltern-Elements.
     */
    public Link(String fieldtext, String url, int parent) {
        this.fieldtext = fieldtext;
        this.parent = parent;
        this.url = cleanURL(url);
    }

    /**
     * Säubert eine URL und formatiert bzw. filtert diese so, dass sie Kompatibel ist.
     *
     * @param url Die zu säubernde URL.
     * @return die gesäuberte URL.
     */
    public static String cleanURL(String url) {
        if (url.contains("#")) {
            url = url.substring(0, url.indexOf('#'));
        }
        if (url.length() > 1) {
            if (url.charAt(0) == ':') {
                url = (url.substring(1, url.length()));
            }
        }
        url = url.replaceAll(": ", ":");


        StringBuilder stringBuilder = new StringBuilder();
        TurtleEscaper turtleEscaper = new TurtleEscaper(stringBuilder, false);
        turtleEscaper.escapeTurtle(url);

        url = stringBuilder.toString();
        //System.out.println("|||" + url);
        //System.out.println("newLink: " + url);
        url = url.trim();
        url = StringUtils.capitalize(url);
        return url;
    }

    public String getFieldtext() {
        return fieldtext;
    }

    public void setFieldtext(String fieldtext) {
        this.fieldtext = fieldtext;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String toString() {
        return this.getUrl();
    }


    @Override
    public int compareTo(Link o) {
        return Integer.compare(this.start, o.start);
    }
    /*
    @Override
    public boolean equals(Object object) {
        boolean equalBool = false;

        if (object != null && object instanceof Link) {
            equalBool = this.url.equals(((Link) object).url);
        }
        return equalBool;
    }
    */

    @Override
    public boolean equals(Object o) {
        Link link = (Link) o;
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        // Link link = (Link) o;

        boolean eq = !(url != null ? !url.toLowerCase().equals(link.url.toLowerCase()) : link.url != null);


        return eq;

    }

    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
