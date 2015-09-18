package com.tb.ba.extraction.methods;

import com.tb.ba.extraction.datastructure.Article;
import com.tb.ba.extraction.datastructure.Link;
import com.tb.ba.extraction.datastructure.Template;
import com.tb.ba.extraction.writer.StdTupleElement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Extrahiert ausschließlich Links aus Templates
 * Created by T. Britsch on 13.07.2015.
 */
public class ExtractInfobox extends ExtractionMethod {
    @Override
    public void extract(Article article) {
        ArrayList<Link> templateLinks = new ArrayList<>();

        Iterator it = article.getTemplates().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Template> entry = (Map.Entry<Integer, Template>) it.next();
            ArrayList<Link> actTemplateLinks = Article.getLinks(entry.getValue().getContent());

            for (Link link : actTemplateLinks) {
                if (!templateLinks.contains(link)) {
                    templateLinks.add(link);
                }
            }

        }


        for (Link link : templateLinks) {
            this.addElement(new StdTupleElement(article.getTitle(), link.toString()));
        }
    }

    @Override
    public String getMethodName() {
        return "exlusive_templates";
    }
}
