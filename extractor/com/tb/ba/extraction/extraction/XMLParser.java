package com.tb.ba.extraction.extraction;


import com.tb.ba.extraction.Extractor;
import com.tb.ba.extraction.datastructure.Article;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

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
        //this.article = new Article(eElement.getElementsByTagName("title").item(0).getTextContent(), eElement
        // .getElementsByTagName("text").item(0).getTextContent());


        try {
            org.jsoup.nodes.Document doc = Jsoup.parse(xml, "", Parser.xmlParser());

            if (doc.select("text").size() != 0 && doc.select("title").size() != 0) {
                String wikitext = doc.select("text").get(0).text();
                String wikititle = doc.select("title").get(0).text();
                int namespace = Integer.parseInt(doc.select("ns").get(0).text());
                this.article = new Article(wikititle, wikitext, namespace);
            }


        } catch (Exception e) {
            if (Extractor.DEBUG) {
                System.out.println("XMLParser");
                System.out.println(xml);
                e.printStackTrace();
            }
        }
        //return doc.toString();



        /*
        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new InputSource(new StringReader(xml)));

            doc.getDocumentElement().normalize();

            NodeList nPage = doc.getElementsByTagName("page");

            for (int temp = 0; temp < nPage.getLength(); temp++) {

                Node nNode = nPage.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;
                    this.article = new Article(eElement.getElementsByTagName("title").item(0).getTextContent(),
                    eElement.getElementsByTagName("text").item(0).getTextContent());
                }
            }


        } catch (SAXException e) {

            if(Extractor.DEBUG){
                System.out.println("XMLParser");
                e.printStackTrace();
            }
        } catch (IOException e) {
            if(Extractor.DEBUG){
                System.out.println("XMLParser");
                e.printStackTrace();
            }
        } catch (ParserConfigurationException e) {
            if(Extractor.DEBUG){
                System.out.println("XMLParser");
                e.printStackTrace();
            }
        }
        */
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
