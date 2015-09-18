package com.tb.ba.extraction.methods;

import com.tb.ba.extraction.datastructure.Article;
import com.tb.ba.extraction.datastructure.Link;
import com.tb.ba.extraction.writer.StdTupleElement;
import com.tb.ba.extraction.writer.TupleElement;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Extrahiert die Daten auf die "herkömmliche" Art und Weise.
 * Created by T. Britsch on 13.07.2015.
 */
public class ExtractNormal extends ExtractionMethod{
    @Override
    public void extract(Article article) {
        //System.out.println(article.wikitext);
        ArrayList<Link> linksList = Article.getLinks(article.getWikitext());
        HashSet<Link> links = new HashSet<>(linksList);

        for (Link link: links){
            //System.out.println(StdTupleElement.encodeTitle(link.toString()));
            this.addElement(new StdTupleElement(article.getTitle(), link.toString()));
        }

    }

    @Override
    public String getMethodName() {
        return "normal";
    }
}
