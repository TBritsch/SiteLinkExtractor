package com.tb.ba.extraction.extraction;



import com.tb.ba.extraction.Extractor;
import com.tb.ba.extraction.datastructure.Article;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;

/**
 * Bearbeitet einen einzelnen Wikipedia-Artikel und erstellt ein Article-Object davon.
 * Created by T. Britsch on 02.07.2015.
 */
public class XMLParser {
    Article article = null;

    /**
     * Extrahiert den Wikititel, Wikitext und den Namespace eines XML-Strings und speichert diese in einem
     * Article-Objekt.
     *
     * @param xml der zu extrahierende XML-String.
     */
    public XMLParser(String xml) {

        try {
            Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
            doc.outputSettings(new Document.OutputSettings().prettyPrint(false));
            if (doc.select("text").size() != 0 &&
            		doc.select("title").size() != 0 &&
            		doc.select("ns").size() != 0) {
            	
            	String wikitext = doc.select("text").get(0).html();
                String wikititle = doc.select("title").get(0).text();
                int namespace = Integer.parseInt(doc.select("ns").get(0).text());
                this.article = new Article(wikititle, wikitext, namespace);
            }


        } catch (Exception e) {
            if (Extractor.DEBUG) {
                System.err.println("XMLParser");
                System.err.println(xml);
                e.printStackTrace();
            }
        }
    }

    /**
     * Gibt den bearbeiteten Artikel zurück.
     *
     * @return
     */
    public Article getArticle() {
        return this.article;
    }
}
