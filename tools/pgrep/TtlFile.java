package com.tb.ba.analysis.grep;

/**
 * Repräsentiert eine Ttl-Datei.
 * Created by T. Britsch on 26.08.2015.
 */
public class TtlFile {

    private String name;
    private Result result = new Result("bla", "0.2");


    public TtlFile(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
