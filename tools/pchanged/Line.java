package com.tb.ba.analysis.changenew;

/**
 * Repäsentiert eine einzelne Zeile einer Datei
 * Created by T. Britsch on 06.09.2015.
 */
public class Line implements Comparable<Line>{

    private String url;
    private Double val1, val2;


    public Line(String url){
        this.url = url;
    }

    public Double getDiff(){
        if(this.val1 != null && this.val2 != null){
            return this.val1 - this.val2;
        }else{
            return null;
        }
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Line line = (Line) o;

        return !(url != null ? !url.equals(line.url) : line.url != null);

    }


    @Override
    public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Double getVal1() {
        return val1;
    }

    public void setVal1(Double val1) {
        this.val1 = val1;
    }

    public Double getVal2() {
        return val2;
    }

    public void setVal2(Double val2) {
        this.val2 = val2;
    }

    @Override
    public int compareTo(Line o) {
        if(PChanged.CONFIG_ORDER == PChanged.ORDER_ABS){
            return new Double(Math.abs(this.getDiff())).compareTo(Math.abs(o.getDiff()));
        }else if(PChanged.CONFIG_ORDER == PChanged.ORDER_REL){
            return new Double(Math.abs(this.getDiff())/Math.min(this.getVal1(), this.getVal2())).compareTo(Math.abs(o.getDiff
                    ())/Math.min(o.getVal1(), o.getVal2()));
        }else{
            return new Double(Math.abs(this.getDiff())).compareTo(Math.abs(o.getDiff()));
        }



    }
}
