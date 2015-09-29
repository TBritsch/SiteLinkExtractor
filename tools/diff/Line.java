package com.tb.ba.analysis.diff;

/**
 * Repräsentiert eine Zeile einer Datei.
 * Created by T. Britsch on 01.09.2015.
 */
public class Line {
    private String line, file;

    public Line(String line, String file){
        this.line = line;
        this.file = file;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Line line1 = (Line) o;

        return !(cleanString(line) != null ? !cleanString(line).equals(line1.cleanString(line)) : line1.cleanString(line) != null);

    }

    @Override
    public int hashCode() {
        return cleanString(line) != null ? cleanString(line).hashCode() : 0;
    }

    public static String cleanString(String toClean){
        toClean = toClean.replaceAll("%3F", "?");
        toClean = toClean.toLowerCase();
        return toClean.replaceAll("[^a-zA-Z0-9]", "");
    }


    public String toString(){
        return this.file + ": " + this.line;

    }

}
