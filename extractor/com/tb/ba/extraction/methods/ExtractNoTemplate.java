package com.tb.ba.extraction.methods;

import com.tb.ba.extraction.datastructure.Article;
import com.tb.ba.extraction.datastructure.Link;
import com.tb.ba.extraction.writer.StdTupleElement;

import java.util.ArrayList;

/**
 * Exportiert Daten, nachdem sämtliche Templates entfernt wurden.
 * Created by T. Britsch on 13.07.2015.
 */
public class ExtractNoTemplate extends ExtractionMethod {
    @Override
    public void extract(Article article) {
        ArrayList<Link> links = Article.getLinks(article.getWikitextExcludedTemplates());

        for (Link link : links) {
            this.addElement(new StdTupleElement(article.getTitle(), link.toString()));
        }
    }

    @Override
    public String getMethodName() {
        return "removed_templates";
    }
}
