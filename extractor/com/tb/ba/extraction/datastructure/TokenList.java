package com.tb.ba.extraction.datastructure;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by T. Britsch on 10.08.2015.
 */
public class TokenList {

    private Integer totalLength;
    private ArrayList<LinkToken> tokens = new ArrayList<>();

    public TokenList(Integer totalLength){
        this.totalLength = totalLength;
    }

    public void add(LinkToken linkToken){
        tokens.add(linkToken);
    }


    public ArrayList<LinkToken> getTokens() {
        return tokens;
    }

    public int getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(int totalLength) {
        this.totalLength = totalLength;
    }

    public double getRating(int position){
        return 1- ((double) position / (double) this.getTotalLength());
    }

    public void setTokens(ArrayList<LinkToken> tokens) {
        this.tokens = tokens;
    }
}
