package com.tb.ba.extraction.datastructure;

/**
 * Speichert die Positionen, also den Beginn und das Ende eines Templates.
 * Created by T. Britsch on 12.09.2015.
 */
public class PositionMarker {

    private int start, end;

    /**
     * Erstelle neue Positionen eines Templates
     * @param start Beginn des Templates.
     * @param end Ende eines Templates.
     */
    public PositionMarker(int start, int end) {
        this.end = end;
        this.start = start;
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
