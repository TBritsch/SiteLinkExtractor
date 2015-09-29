package com.tb.ba.analysis.prSplitter;

/**
 * Repräsentiert ein Ergebnis der Aufteilung
 * Created by T. Britsch on 11.09.2015.
 */
public class Result implements Comparable<Result> {

    private String name;
    private double pagerank;

    public Result(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPagerank() {
        return pagerank;
    }

    public void setPagerank(double pagerank) {
        this.pagerank = pagerank;
    }


    @Override
    public int compareTo(Result o) {
        return Double.compare(this.getPagerank(), o.getPagerank());
    }


    public String toString() {
        return this.getName() + ": " + this.getPagerank();
    }
}
