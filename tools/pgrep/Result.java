package com.tb.ba.analysis.grep;


/**
 * Repräsentiert ein Ergbnis einer FileSearch
 * Created by T. Britsch on 26.08.2015.
 */
public class Result {

    private String page;
    private String pr;

    public Result(String page, String pr) {
        this.page = page;
        this.pr = pr;
    }

    public String getPage() {
        return page;
    }


    public String getPr() {
        return pr;
    }


}
