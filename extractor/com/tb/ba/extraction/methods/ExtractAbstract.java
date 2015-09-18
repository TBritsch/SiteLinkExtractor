package com.tb.ba.extraction.methods;

import com.tb.ba.extraction.datastructure.Article;
import com.tb.ba.extraction.datastructure.Link;
import com.tb.ba.extraction.writer.StdTupleElement;

import java.util.ArrayList;

/**
 * Extrahiert nur Daten aus dem Abstract einer Seite.
 * Created by T. Britsch on 13.07.2015.
 */
public class ExtractAbstract extends ExtractionMethod {
    @Override
    public void extract(Article article) {
        ArrayList<Link> links = Article.getLinks(article.getAbstract());

        for (Link link : links) {
            if (link.getParent() != Link.PARENT_FILE) {
                this.addElement(new StdTupleElement(article.getTitle(), link.toString()));
            }

        }

    }

    @Override
    public String getMethodName() {
        return "abstract";
    }
}
