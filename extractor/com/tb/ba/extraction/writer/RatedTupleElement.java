package com.tb.ba.extraction.writer;

import com.tb.ba.extraction.Extractor;
import com.tb.ba.extraction.datastructure.Link;
import com.tb.ba.extraction.filter.Filter;

/**
 * Ein Quadrupel-Element einer erweiterten tsv-Datei.
 * <p/>
 * Created by T. Britsch on 09.07.2015.
 */
public class RatedTupleElement extends TupleElement {


    /**
     * Konstruktor der Klasse
     *
     * @param articleNameFrom Link der ausgehenden Seite
     * @param articleNameTo   Link der eingehenden Seite
     * @param rating          Rating des Links
     */

    public RatedTupleElement(String articleNameFrom, String articleNameTo, double rating) {
        this.map.put("articleNameFrom", articleNameFrom);
        this.map.put("articleNameTo", articleNameTo);
        this.map.put("rating", rating);
    }


    /**
     * Gibt den Triple in der Form "<http://$lang.dbpedia.org/resource/$article> <http://dbpedia
     * .org/ontology/wikiPageWikiLink> <http://$lang.dbpedia.org/resource/$article> <$rating>." aus.
     *
     * @return Der String
     */
    public String toString() {
        if (Filter.isAllowedEntitiyName(this.map.get("articleNameFrom").toString(), encodeTitle(this.map.get
                ("articleNameTo").toString()))) {//checking, if namespaces are ok
            StringBuilder sB = new StringBuilder();
            sB.append("<http://");
            if (!Extractor.config_language.equals("en") && Extractor.config_language != "") {
                sB.append(Extractor.config_language);
                sB.append(".");
            }
            sB.append("dbpedia.org/resource/");

            sB.append(encodeTitle(this.map.get("articleNameFrom").toString()));
            sB.append("> <http://dbpedia.org/ontology/wikiPageWikiLink> <http://");
            if (!Extractor.config_language.equals("en") && Extractor.config_language != "") {
                sB.append(Extractor.config_language);
                sB.append(".");
            }
            sB.append("dbpedia.org/resource/");
            sB.append(encodeTitle(this.map.get("articleNameTo").toString()));
            sB.append(">");

            sB.append(" <");
            sB.append(this.map.get("rating").toString());
            sB.append("> .");
            return sB.toString();
        } else {
            return null;
        }
        //return this.map.get("articleNameFrom").toString() + "\t" + this.map.get("articleNameTo").toString();
    }


    /**
     * Säubert einen URL-Pfad, um diesen korrekt zu speichern.
     *
     * @param title der zu säubernde Titel
     * @return der gesäuberte Titel
     */
    private static String encodeTitle(String title) {
        title = title.replace("\\\\", "\\");
        title = title.replace(" ", "_");
        title = title.replace("&nbsp;", "_");
        title = Link.cleanURL(title);
        return title.replace("  ", "_");
    }
}
