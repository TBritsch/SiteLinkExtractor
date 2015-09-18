package com.tb.ba.extraction.datastructure;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Speichert das Vorkommen eines TemplateMarkers, also einer Stelle, an der ein Template begonnen oder beendet wird.
 * Created by T. Britsch on 20.08.2015.
 */
public class Occurrence implements Comparable<Occurrence>{

    public static final String OPEN = "o";
    public static final String CLOSE = "c";

    private int pos;
    private String type;

    /**
     * Erstellt neues Vorkommen
     * @param pos Stelle des Vorkommens im Text
     * @param type Typ des Vorkommens
     */
    public Occurrence(int pos, String type){
        this.pos = pos;
        this.type = type;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int compareTo(Occurrence o) {
        return Integer.compare(this.pos, o.pos);
    }

    public String toString(){
        return this.getPos() + "("+this.getType()+")";
    }


}
