package com.tb.ba.extraction.filter;

import com.tb.ba.extraction.Extractor;
import com.tb.ba.scala.TurtleEscaper;
import com.tb.ba.scala.UriDecoder;
import com.tb.ba.scala.WikiUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by T. Britsch on 19.08.2015.
 */
public class Filter {

    public static HashMap<String, String[]> translateFiles = new HashMap<>();
    public static HashMap<String, String> translateCategories = new HashMap<>();


    /**
     * Erstellt eine valide URL ausgehend vom Input.
     *
     * @param raw Der zu verarbeitende Link
     * @return Entity-Name
     */
    public static String buildEntityName(String raw) {


        if(raw.length() > 0){
            StringBuffer rawbuf = new StringBuffer(raw);
            rawbuf.setCharAt(0, Character.toUpperCase(rawbuf.charAt(0)));
            raw = rawbuf.toString();
        }



        if (raw.contains("#")) {
            raw = raw.substring(0, raw.indexOf('#'));
        }

        if (raw.length() > 1) {
            if (raw.charAt(0) == ':') {
                raw = (raw.substring(1, raw.length()));
            }
        }

        //raw = raw.replace(translateCategorie(Extractor.config_language) + ": ", translateCategorie(Extractor
        //        .config_language) + ":");

        raw = replaceInsensitive(raw, translateCategorie(Extractor.config_language) + ": ", translateCategorie
                (Extractor.config_language) + ":");


        raw = replaceInsensitive(raw, translateCategorie(Extractor.config_language), translateCategorie(Extractor
                .config_language));


        for (String prefix : getTranslatedFileNames()) {
            raw = raw.replace(prefix + ": ", prefix + ":");
        }


        //raw = raw.replaceAll(": ", ":");

        raw = UriDecoder.decode(raw);
        raw = WikiUtil.cleanSpace(raw);




        if(!(Extractor.config_language.equals("en") || Extractor.config_language.equals("als"))){
            raw = WikiUtil.wikiEncode(raw);
            StringBuilder stringBuilder = new StringBuilder();
            TurtleEscaper turtleEscaper = new TurtleEscaper(stringBuilder, false);
            turtleEscaper.escapeTurtle(raw);

            raw = stringBuilder.toString();
        }

        raw = raw.replace("\\\\", "\\");
        raw = raw.replace(" ", "_");
        raw = raw.replace("&nbsp;", "_");
        raw = raw.replace("  ", "_");
        raw = raw.replace("&amp;", "&");
        raw = raw.replace("%5B", "");


        raw = raw.trim();
        //raw = StringUtils.capitalize(raw);
        
        // replaces the above function
        if (raw.length() > 0) {
        	char first = raw.charAt(0);
        	raw = raw.substring(1);
            first = Character.toUpperCase(first);
            raw = first + raw;
        }

        return raw;
    }

    private static String replaceInsensitive(String string, String search, String replace) {
        return string.replaceAll("(?i)" + search, replace);
    }


    /**
     * Prüft, ob es sich bei dem als Parameter "from" und "to" angegebenen Link-Paar um eine erlaubte Verbindung
     * handelt. Zum Beispiel sind Links von Kategorie-Seiten auf Kategorie-Seiten nicht gestattet.
     *
     * @param from Pfad der Seite, welche den Link enthält
     * @param to   Pfad der Seite, auf welche der Link gesetzt ist
     * @return Ja/Nein, ob es sich um eine erlaubte Verbindung handelt.
     */
    public static boolean isAllowedEntitiyName(String from, String to) {
        boolean allowed = true;

        if (from.toLowerCase().contains(translateCategorie(Extractor.config_language).toLowerCase() + ":")) {
            allowed = false;
        }

        if (to.equals("")) {
            allowed = false;
        }

        if (isFile(from) || isFile(to)) {
            allowed = false;
        }

        if (from.toLowerCase().contains("commons:") || to.toLowerCase().contains("commons:")) {
            allowed = false;
        }

        //Simple English
        if (from.toLowerCase().contains("simple:") || to.toLowerCase().contains("simple:")) {
            allowed = false;
        }

        // Remove links to other language versions
        if (Pattern.matches("[a-zA-Z]{2,3}:.*?", from) || Pattern.matches("[a-zA-Z]{2,3}:.*?", to)) {

        	int langLength = Extractor.config_language.length();
        	String fromLang = null;
        	if (from.length() >= langLength) {
        		fromLang = from.substring(0, Extractor.config_language.length()).toLowerCase();	
        		if (!fromLang.equals(Extractor.config_language.toLowerCase())) {
        			allowed = false;
        		}
        	}
        	
        	String toLang = null;
        	if (to.length() >= langLength) {
        		toLang = to.substring(0, Extractor.config_language.length()).toLowerCase();
        		if (!toLang.equals(Extractor.config_language.toLowerCase())) {
        			allowed = false;
        		}
        	}
        }

        return allowed;

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
        boolean isFile = false;

        for (String prefix : getTranslatedFileNames()) {
            if (path.toLowerCase().contains(prefix.toLowerCase() + ":")) {
                isFile = true;
                break;
            }
        }

        if (!isFile) {
            for (String prefix : getTranslatedFileNames("en")) {
                if (path.toLowerCase().contains(prefix.toLowerCase() + ":")) {
                    isFile = true;
                    break;
                }
            }
        }

        return isFile;
    }


    public static String encodeURL(String host, String path){

        String ret = "";
        if(Extractor.config_language.equals("en") || Extractor.config_language.equals("als")){
            try {
                URI uri = new URI(
                        "http",
                        host,
                        path,
                        null);

                ret = uri.toASCIIString();
            } catch (URISyntaxException e) {
                if(Extractor.DEBUG){
                    e.printStackTrace();
                }
            }
        }else{
            return "http://" + host + path;
        }
        return ret;
    }


    /**
     * Gibt den in der in Extractor.config_language gespeicherten Sprache gebräuchigen Substring wieder, der eine Datei
     * identifiziert.
     *
     * @return String, der eine Datei identifiziert.
     */
    public static String[] getTranslatedFileNames() {
        return getTranslatedFileNames(Extractor.config_language);
    }


    /**
     * Gibt den in der Srache $Lang gebräuchigen Substring wieder, der eine Datei identifiziert. Im Falle, dass es
     * $Lang keine bekannt Sprache ist, wir das Ergebnis der englischen Sprache zurückgegeben.
     *
     * @param lang
     * @return String, der eine Datei identifiziert.
     */
    public static String[] getTranslatedFileNames(String lang) {
        String[] ret = translateFiles.get(lang);
        if (ret == null) {
            ret = translateFiles.get("en");
        }

        return ret;
    }


    /**
     * Initialisiert die Hashmaps, die für die Filter bnötigt werden.
     */
    public static void init() {
        Filter.translateFiles.put("de", new String[]{"Datei", "Bild", "Image"});
        Filter.translateFiles.put("als", new String[]{"Datei", "Bild"});
        Filter.translateFiles.put("eu", new String[]{"Fitxategi", "Irudi"});
        Filter.translateFiles.put("en", new String[]{"File", "Image"});
        Filter.translateFiles.put("es", new String[]{"File", "Image"});
        Filter.translateFiles.put("it", new String[]{"File", "Image"});
        Filter.translateFiles.put("fr", new String[]{"File", "Image"});
        Filter.translateFiles.put("ru", new String[]{"File", "Image"});
        Filter.translateFiles.put("zh", new String[]{"File", "Image"});

        Filter.translateCategories.put("eu", "Kategoria");
        Filter.translateCategories.put("de", "Kategorie");
        Filter.translateCategories.put("en", "Category");
        Filter.translateCategories.put("es", "Categoría");
        Filter.translateCategories.put("it", "Categoria");
        Filter.translateCategories.put("fr", "Catégorie");
        Filter.translateCategories.put("ru", "Категория");
        Filter.translateCategories.put("zh", "Category");

    }
}
