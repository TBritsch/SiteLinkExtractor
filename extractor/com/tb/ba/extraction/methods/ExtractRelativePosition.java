package com.tb.ba.extraction.methods;

import com.tb.ba.extraction.datastructure.Article;
import com.tb.ba.extraction.datastructure.LinkToken;
import com.tb.ba.extraction.datastructure.TokenList;
import com.tb.ba.extraction.writer.RatedTupleElement;
import com.tb.ba.extraction.writer.StdTupleElement;
import com.tb.ba.extraction.writer.TupleElement;

import java.util.ArrayList;

/**
 * Extrahiert Daten mit der Methode "Relative Position" unter Berücksichtigung der Linkposition im Text.
 * Created by T. Britsch on 13.07.2015.
 */
public class ExtractRelativePosition extends ExtractionMethod{
    @Override
    public void extract(Article article) {
        TokenList linkTokens = article.getTokenList();
        for (LinkToken token : linkTokens.getTokens()){
            //System.out.println(token.getLink().toString() + ": " + linkTokens.getRating(token.getPosition()));
            this.addElement(new RatedTupleElement(article.getTitle(), token.getLink().toString(), linkTokens.getRating(token.getPosition())));
        }

    }

    @Override
    public String getMethodName() {
        return "relative_position";
    }
}
