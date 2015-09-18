package com.tb.ba.extraction.filter;

import com.tb.ba.extraction.Extractor;

import java.util.HashMap;

/**
 * Created by T. Britsch on 19.08.2015.
 */
public class Filter {

    public static HashMap<String, String> translateFiles = new HashMap<>();
    public static HashMap<String, String> translateCategories = new HashMap<>();


    /**
     * Prüft, ob es sich bei dem als Parameter "from" und "to" angegebenen Link-Paar um eine erlaubte Verbindung
     * handelt. Zum Beispiel sind Links von Kategorie-Seiten auf Kategorie-Seiten nicht gestattet.
     *
     * @param from Pfad der Seite, welche den Link enthält
     * @param to   Pfad der Seite, auf welche der Link gesetzt ist
     * @return Ja/Nein, ob es sich um eine erlaubte Verbindung handelt.
     */
    public static boolean isAllowedEntitiyName(String from, String to) {

        if (from.toLowerCase().contains(translateCategorie(Extractor.config_language).toLowerCase() + ":")) {
            return false;
        } else {
            return true;
        }

    }


    /**
     * Gibt den in der Srache $Lang gebräuchigen Substring wieder, der eine Kategorie identifiziert. Im Falle, dass es
     * $Lang keine bekannt Sprache ist, wir das Ergebnis der englischen Sprache zurückgegeben.
     *
     * @param lang gesuchte Sprache
     * @return Ergebnis der Übersetzung
     */
    public static String translateCategorie(String lang) {
        /*
        Übersetzt den Substring Kategorie: im Link, da die Namespaces der Artikel nicht zur Verfügung stehen
         */

        String ret = Filter.translateCategories.get(lang);
        if (ret == null) {
            ret = Filter.translateCategories.get("en");
        }

        return ret;


    }


    /**
     * Prüft, ob es sich bei der Pfad um eine Datei handelt.
     *
     * @param path Pfad zur Entität, die überprüft werden soll
     * @return Ja/Nein, ob es sich um Datei handelt.
     */
    public static boolean isFile(String path) {
        if (path.contains(getTranslatedFileName() + ":")) {
            return true;
        } else if (path.contains(getTranslatedFileName("en") + ":")) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Gibt den in der in Extractor.config_language gespeicherten Sprache gebräuchigen Substring wieder, der eine Datei
     * identifiziert.
     *
     * @return String, der eine Datei identifiziert.
     */
    public static String getTranslatedFileName() {
        return getTranslatedFileName(Extractor.config_language);
    }


    /**
     * Gibt den in der Srache $Lang gebräuchigen Substring wieder, der eine Datei identifiziert. Im Falle, dass es
     * $Lang keine bekannt Sprache ist, wir das Ergebnis der englischen Sprache zurückgegeben.
     *
     * @param lang
     * @return String, der eine Datei identifiziert.
     */
    public static String getTranslatedFileName(String lang) {
        String ret = translateFiles.get(lang);
        if (ret == null) {
            ret = translateFiles.get("en");
        }

        return ret;
    }


    /**
     * Initialisiert die Hashmaps, die für die Filter bnötigt werden.
     */
    public static void init() {
        Filter.translateFiles.put("de", "Datei");
        Filter.translateFiles.put("als", "Datei");
        Filter.translateFiles.put("en", "File");
        Filter.translateFiles.put("es", "File");
        Filter.translateFiles.put("it", "File");
        Filter.translateFiles.put("fr", "File");
        Filter.translateFiles.put("ru", "File");
        Filter.translateFiles.put("zh", "File");


        Filter.translateCategories.put("de", "Kategorie");
        Filter.translateCategories.put("en", "Category");
        Filter.translateCategories.put("es", "Categoría");
        Filter.translateCategories.put("it", "Categoria");
        Filter.translateCategories.put("fr", "Catégorie");
        Filter.translateCategories.put("ru", "Категория");
        Filter.translateCategories.put("zh", "Category");

    }
}
