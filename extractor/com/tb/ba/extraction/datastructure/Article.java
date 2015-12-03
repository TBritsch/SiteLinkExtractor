package com.tb.ba.extraction.datastructure;

import com.tb.ba.extraction.Extractor;
import com.tb.ba.extraction.filter.Filter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Repräsentiert einen Artikel.
 * Created by T. Britsch on 02.07.2015.
 */
public class Article {


    /**
     * Maximale Länge eine Abstracts
     */
    public static final int CONFIG_MAX_ABSTRACT_LENGTH = 500;
    public static final int CONFIG_MAX_FILELINK_LENGT = 400;


    private HashMap<Integer, Template> templates = new HashMap<>();
    private String title;
    private String wikitext;
    private String wikitextExcludedTemplates; //Templates are Exluded in hashmap templates and replaced with
    // ##TemplateXX# or so
    private String wikiTextDeletedTemplates; //Templates are deleted
    private TokenList tokenList;
    private int namspace;


    /**
     * Erstellt einen neuen Artikel.
     *
     * @param title     Titel des Artikels.
     * @param wikitext  Text des Artikels.
     * @param namespace Namespace-ID des Artikels.
     */
    public Article(String title, String wikitext, int namespace) {
        this.title = title;
        this.namspace = namespace;
        if (namespace == 0) {
            //System.out.print("title: " + title + ":");

            this.wikitext = removeRefs(wikitext);

            this.handleTemplates();
            this.tokenize();
        }

    }


    private static String removeRefs(String input) {
        //input = input.replaceAll("<ref(\\s)*name(\\s)*=(\\w|\\s){1,100}/>", "");
        input = input.replaceAll("<ref .*?(</ref>|/>)", "");
        Document doc = Jsoup.parse(input, "", Parser.xmlParser());
        Elements els = doc.select("ref");
        for (Element e : els) {
            //System.out.println(e.text() + " " + e.attr("name"));
            //System.out.println(e.text().length());

            if (e.text().length() < 1000) {
                e.remove();
            }

        }

        removeComments(doc);

        return doc.toString();
    }


    private static void removeComments(Node node) {
        for (int i = 0; i < node.childNodes().size(); ) {
            Node child = node.childNode(i);
            //System.out.println(child.nodeName());
            if (child.nodeName().equals("#comment")) {
                child.remove();
            } else {
                removeComments(child);
                i++;
            }
        }
    }


    /**
     * Gibt sämtliche Links, welche im Wiki-Syntax formatiert sind und im String $str enthalten sind, zurück.
     *
     * @param str Der zu untersuchende String.
     * @return Liste aller Links.
     */
    public static ArrayList<Link> getLinks(String str) {
        ArrayList<Link> links = new ArrayList<>();

        int indexOpen = str.indexOf("[[");
        int indexOpenOld = -2;
        while (indexOpen >= 0) { //Dursuche den Text nach allen Stellen, an den ein Template mittels "{{" geöffnet wird.
            if (indexOpen >= indexOpenOld + 2) {
                if (Filter.isFile(str.substring(indexOpen + 2, Math.min(indexOpen + 8, str.length())))) {
                    //Dateilink -> Hier gibt es rekurision
                    Stack<Occurrence> stack = new Stack<>();
                    stack.push(new Occurrence(indexOpen, Occurrence.OPEN));
                    //System.out.println("Dateilink");
                    boolean running = true;
                    while (running) {
                        int nextOcc = Math.min(str.indexOf("[[", indexOpen + 2), str.indexOf("]]", indexOpen + 2));
                        nextOcc = Math.min(nextOcc, indexOpen + CONFIG_MAX_FILELINK_LENGT);
                        int max;
                        if (nextOcc < 0) {
                            max = str.length();
                        } else {
                            max = Math.min(nextOcc, str.length());

                        }

                        String linktext = str.substring(indexOpen + 2, Math.min(str.length(), max));
                        if (linktext.length() > 5 && Filter.isFile(linktext.substring(0, Math.min(linktext
                                .length(), 6)))) {
                            String[] link_array = linktext.split(Pattern.quote("|"));
                            //links.add(new Link(link_array[0], Link.PARENT_FILE));
                            links.add(new Link(link_array[0], Link.PARENT_FILE, indexOpen + 2));
                            //System.out.print("|||" + link_array[0]);
                        }
                        if (nextOcc != -1) {
                            indexOpen = nextOcc;
                            if (str.charAt(nextOcc) == '[') {
                                //Nächstes Auftreten ist [[
                                stack.push(new Occurrence(indexOpen, Occurrence.OPEN));
                            } else if (str.charAt(nextOcc) == ']') {
                                // ]]
                                if (!stack.empty()) {
                                    stack.pop();
                                }

                                if (stack.empty()) {
                                    running = false;
                                }

                            } else {
                                running = false;
                            }
                        } else {
                            running = false;
                        }


                    }
                } else {
                    //keine Rekursion

                    String linktext = null;
                    String[] link_array = new String[0];
                    int end = 0;
                    try {
                        if (str.indexOf("]]", Math.min(indexOpen, str.length())) > 0) {
                            end = Math.min(str.length(), str.indexOf("]]", Math.min(indexOpen, str.length())));
                        } else {
                            end = str.length();
                        }
                        end = Math.min(end, indexOpen + CONFIG_MAX_FILELINK_LENGT);
                        linktext = str.substring(Math.min(indexOpen + 2, str.length()), end);
                        if (!linktext.equals("|")) {
                            link_array = linktext.split(Pattern.quote("|"));
                            links.add(new Link(link_array[0], Link.PARENT_UNDEFINED, indexOpen + 2));
                            //System.out.print("|||" + link_array[0]);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println("indexOpen:" + indexOpen + " | strLength:" + str.length() + " | end:" + end);
                        e.printStackTrace();
                    } catch (StringIndexOutOfBoundsException e) {
                        System.out.println("indexOpen:" + indexOpen + " | strLength:" + str.length() + " | end:" + end);
                        e.printStackTrace();
                    } catch (Exception e) {
                        if (Extractor.DEBUG) {
                            System.out.println("Error: Article: Could not add: " + linktext + "( Length: " +
                                    link_array.length +
                                    "At Position: " + indexOpen + 2 + ")");
                            System.out.println(str.substring(0, Math.min(str.length(), 500)));
                            e.printStackTrace();
                        }
                    }
                }

            }
            // ArrayList hinzu
            indexOpenOld = indexOpen;
            indexOpen = str.indexOf("[[", indexOpen + 1);
        }


        return links;
    }


    /*
    public static String removeFileLinks(String wikitext) {
        StringBuffer output = new StringBuffer();

        ArrayList<Link> links = new ArrayList<>();


        int indexOpen = wikitext.indexOf("[[");
        int indexOpenOld = -2;
        int nextOcc = -2;
        ArrayList<PositionMarker> positionMarkers = new ArrayList<>();
        while (indexOpen >= 0) { //Dursuche den Text nach allen Stellen, an den ein Template mittels "{{" geöffnet wird.
            PositionMarker positionMarker = new PositionMarker();
            if (indexOpen >= indexOpenOld + 2) {
                if (Filter.isFile(wikitext.substring(indexOpen + 2, Math.min(indexOpen + 8, wikitext.length())))) {
                    positionMarker.setStart(indexOpen);
                    //Dateilink -> Hier gibt es rekurision
                    Stack<Occurrence> stack = new Stack<>();
                    stack.push(new Occurrence(indexOpen, Occurrence.OPEN));
                    //System.out.println("Dateilink");
                    boolean running = true;
                    while (running) {
                        nextOcc = Math.min(wikitext.indexOf("[[", indexOpen + 2), wikitext.indexOf("]]", indexOpen +
                                2));
                        nextOcc = Math.min(nextOcc, indexOpen + CONFIG_MAX_FILELINK_LENGT);
                        int max;
                        if (nextOcc < 0) {
                            max = wikitext.length();
                        } else {
                            max = Math.min(nextOcc, wikitext.length());

                        }

                        String linktext = wikitext.substring(indexOpen + 2, Math.min(wikitext.length(), max));
                        if (linktext.length() > 5 && Filter.isFile(linktext.substring(0, Math.min(linktext
                                .length(), 6)))) {
                            String[] link_array = linktext.split(Pattern.quote("|"));
                            links.add(new Link(link_array[0], Link.PARENT_FILE));
                            //System.out.print("|||" + link_array[0]);
                        }
                        if (nextOcc != -1) {
                            indexOpen = nextOcc;
                            if (wikitext.charAt(nextOcc) == '[') {
                                //Nächstes Auftreten ist [[
                                stack.push(new Occurrence(indexOpen, Occurrence.OPEN));
                            } else if (wikitext.charAt(nextOcc) == ']') {
                                // ]]
                                if (!stack.empty()) {
                                    stack.pop();
                                }

                                if (stack.empty()) {
                                    running = false;
                                }

                            } else {
                                running = false;
                            }
                        } else {
                            running = false;
                            nextOcc = wikitext.length();
                        }


                    }


                    positionMarker.setEnd(nextOcc + 2);
                    positionMarkers.add(positionMarker);
                } else {
                }

            }
            // ArrayList hinzu
            indexOpenOld = indexOpen;
            indexOpen = wikitext.indexOf("[[", indexOpen + 1);
        }

        if (positionMarkers.size() > 0) {
            output.append(wikitext.substring(0, positionMarkers.get(0).getStart()));
            PositionMarker marker = null, old = null;
            for (int i = 0; i < positionMarkers.size(); i++) {
                old = marker;
                marker = positionMarkers.get(i);

                if (marker != null && old != null) {
                    output.append(wikitext.substring(old.getEnd(CONFIG_MAX_FILELINK_LENGT), marker.getStart()));
                }

            }
            output.append(wikitext.substring(Math.min(marker.getEnd(CONFIG_MAX_FILELINK_LENGT), wikitext.length()),
                    wikitext.length()));

        } else {
            output.append(wikitext);
        }


        return output.toString();
    }
*/

    /**
     * Trennt den Artikeltext in Tokens auf
     */
    private void tokenize() {
        this.tokenList = new TokenList(null); //Initialisiere Liste aller Tokens
        HashMap<Integer, String> links = new HashMap<>(); // Initialisiere Liste mit allen Tokens, die einen Link
        // enthalten


        StringBuffer sblink = new StringBuffer();
        Pattern p = Pattern.compile("(?i)\\[\\[(.*?)\\]\\]", Pattern.DOTALL); //Regulärer Ausdruck, der alle Links
        // enthält
        // Matcher m = p.matcher(removeFileLinks(this.getWikiTextDeletedTemplates()));//Wende diesn Ausdruck auf dem
        // Wikitext mit entfernten
        Matcher m = p.matcher((this.getWikiTextDeletedTemplates()));//Wende diesn Ausdruck auf dem Wikitext mit
        // entfernten

        // Templates an
        int i = 0;
        while (m.find()) {//Iteration über alle Fundstellen eines Links
            m.appendReplacement(sblink, " ##link" + i + "##");//Ersetze den jeweiligen Link mit einem Platzhalter
            //Beispiel: [[Module der ISS]] -> ##LINK1##
            links.put(i, m.group(1).toString());//Füge Link der Liste der Links hinzu
            i++;
        }
        m.appendTail(sblink);
        String[] tokens = sblink.toString().replaceAll("\\s+", " ").split(" ");//Entferne das Vorkommen von mehreren
        // Leerzeichen und Zerteile dann den String bei jedem Leerzeichen

        for (int j = 0; j < tokens.length; j++) {//Iteration über alle Tokens
            String token = tokens[j];
            if (Pattern.matches(".*##link\\d+##.*", token)) {//Wenn Token einen Link enthält
                //Wichtig: Auch Links am Ende eines Satzes müssen gefunden werden, bspw. der Link "[[Link]]."

                Pattern p2 = Pattern.compile("(?i)##link\\d+##", Pattern.DOTALL);
                Matcher m2 = p2.matcher(token);
                if (m2.find()) {
                    Pattern p3 = Pattern.compile("(?i)\\d+", Pattern.DOTALL);//Suche ID des Links
                    Matcher m3 = p3.matcher(m2.group());
                    if (m3.find()) {
                        String link_string = null;//Lade Link mit der gegebenen ID
                        try {
                            link_string = links.get(Integer.parseInt(m3.group()));
                        } catch (NumberFormatException e) {
                            if (Extractor.DEBUG) {
                                System.out.println("(" + m3.group() + "| " + links.size() + ")" + "(" + m2.group() +
                                        ")");

                                e.printStackTrace();
                            }
                        }
                        if (link_string != null) {
                            if (!link_string.equals("|")) {
                                Link link = null;//Extrahiere den Link aus dem
                                try {
                                    link = Article.getLinks("[[" + link_string + "]]").get(0);
                                    this.tokenList.add(new LinkToken(link.getUrl(), j));//Speichere den Link und die
                                    // Stelle des
                                    // Vorkommens des Links
                                } catch (Exception e) {
                                    if (Extractor.DEBUG) {
                                        System.out.println("tokenize: (" + link_string + ") (" + m3.group() + "| " +
                                                links.size() + ")");
                                        for (String link_ : links.values()) {
                                            System.out.print(link_ + ", ");
                                        }
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        // String

                    }
                }
            }
        }

        this.tokenList.setTotalLength(tokens.length); //Speichere die Gesamtanzahl aller Tokens
    }


    /**
     * Diese Methode extrahiert die Templates aus dem eigentlichen Wiki-Text und speichert zwei weitere Varianten des
     * Textes in der Klasse Article.
     * wikitextExcludedTemplates
     * wikiTextDeletedTemplates
     */
    private void handleTemplates() {

        ArrayList<Occurrence> occurrences = new ArrayList(); //Sammelt das Auftreten vom Beginn, bzw. vom Ende eines
        // Templates
        ArrayList<Template> templates = new ArrayList(); //Speichert die Templates, die extrahiert werden
        Stack<Occurrence> stack = new Stack<>(); //Kellerspeicher, der die geöffneten Templates speichert


        int indexOpen = this.wikitext.indexOf("{{");
        int indexOpenOld = -2;  //speichert den zuletzt gültigen Wert
        while (indexOpen >= 0) { //Dursuche den Text nach allen Stellen, an den ein Template mittels "{{" geöffnet wird.
            if (indexOpen >= indexOpenOld + 2) {
                occurrences.add(new Occurrence(indexOpen, Occurrence.OPEN)); //Füge neues Auftreten eines Templates der
                // ArrayList hinzu
            }

            indexOpenOld = indexOpen;
            indexOpen = this.wikitext.indexOf("{{", indexOpen + 1);
        }
        int indexClose = this.wikitext.indexOf("}}");
        int indexCloseOld = -2;
        while (indexClose >= 0) {
            if (indexClose >= indexCloseOld + 2) {
                occurrences.add(new Occurrence(indexClose, Occurrence.CLOSE)); //Füge neues Auftreten des schließen
                // eines Templates der ArrayList hinzu
            }
            indexCloseOld = indexClose;
            indexClose = this.wikitext.indexOf("}}", indexClose + 1);
        }

        Collections.sort(occurrences); // Sortiere die aufgetretenen Templates der Größe nach

        int id = 0;
        for (Occurrence occurrence : occurrences) {
            if (occurrence.getType().equals(Occurrence.OPEN)) {
                //Wenn es sich bei dem Marker um eine Öffnung des Templates handelt, füge diese dem Stack hinzu
                stack.push(occurrence);
            } else {
                //Falls nicht, versuche das geöffnete Template zu finden, um es zu schließen. Da es zwischen dem
                // geöffneten Template und dieser Fundstelle keine weiteren Marker gibt, handelt es sich hier um das
                // zu schließende Template
                try {
                    if (!stack.empty()) {
                        Occurrence open = stack.pop(); //Stack entält noch Elemente -> Äußere Klammer noch nicht
                        // erreicht
                        templates.add(new Template(id, open.getPos(), occurrence.getPos(), this.wikitext.substring(open
                                        .getPos() + 2,
                                occurrence.getPos())));//füge neues Template hinzu
                    }

                } catch (EmptyStackException e) {
                    if (Extractor.DEBUG) {
                        System.out.println("handleTemplates: Article: " + this.getTitle() + ": " + occurrence.getPos());
                        for (Occurrence occurrence1 : occurrences) {
                            System.out.print(occurrence1.getPos() + occurrence1.getType() + ", ");
                        }
                        System.out.println();
                        e.printStackTrace();
                    }
                }

            }

            id++;
        }

        Collections.sort(templates);//sortiere die Templates nach Startstelle der Templates


        ArrayList<Template> result = new ArrayList<>(templates);

        for (Template template : templates) {
            for (Template compareTemplate : templates) {
                if (template.getStart() != compareTemplate.getStart()) {
                    //Prüfe mit allen Templates, außer mit sich selbst, ob sich ein Template innerhalb eines
                    // Templates befindet
                    if (compareTemplate.getStart() > template.getStart() && compareTemplate.getEnd() < template
                            .getEnd()) {
                        //Wenn ja, dann dominiert das eigentliche Template das compareTemplate. Das compareTemplate
                        // muss daraufhin nicht mehr beachtet werden und kann gelöscht werden
                        result.remove(compareTemplate);
                    }
                }
            }
        }

        Collections.sort(result);//Sortiere die Templates erneut
        Collections.reverse(result);// in der umgekehrten Reihenfolge
        StringBuffer excludeTemplates = new StringBuffer();
        StringBuffer deleteTemplates = new StringBuffer();
        excludeTemplates.append(this.wikitext);
        deleteTemplates.append(this.wikitext);
        for (Template template : result) {
            //Entferne die Templates aus den jeweiligen Artikeltexten und füge, wenn nötig einen Platzhalter hinzu.
            excludeTemplates.replace(template.getStart(), template.getEnd() + 2, "##TEMPLATE" + template.getStart() +
                    "##");
            deleteTemplates.replace(template.getStart(), template.getEnd() + 2, "");
            this.templates.put(template.getStart(), template);
        }

        this.wikitextExcludedTemplates = excludeTemplates.toString();
        this.wikiTextDeletedTemplates = deleteTemplates.toString();

    }


    /**
     * Ermittelt den Abstract des Artikels.
     *
     * @return Der Abstract des Artikels.
     */
    public String getAbstract() {
        String wikiText = this.wikiTextDeletedTemplates;
        if (wikiText.length() < CONFIG_MAX_ABSTRACT_LENGTH) {
            return wikiText;
        }

        StringBuilder builder = new StringBuilder();
        int size = 0;
        String[] stentences = wikiText.split("(?<=\n|[.](?<!\\d)(?!\\d))");
        for (String sentence : stentences) {
            if (size + sentence.length() > CONFIG_MAX_ABSTRACT_LENGTH) {
                if (builder.toString().equals("")) {
                    return sentence;
                }
                return builder.toString().trim();
            }

            size += sentence.length();
            builder.append(sentence);
        }


        return builder.toString().trim();


        /*
                for(String sentence : stentences){
            if(size + sentence.length() > CONFIG_MAX_ABSTRACT_LENGTH)
            {
                if (builder.toString().equals(""))
                {
                    return sentence;
                }
                return builder.toString().trim();
            }

            size += sentence.length();
            builder.append(sentence);
        }

        return builder.toString().trim();
         */

    }

/*
    //Gibt einen gesaeuberten Wikitext wieder. Sorgt dafür, dass Links entfernt werden und Templates;
    private String getCleanedWikiTextAbstract(){
        StringBuffer wikitext = new StringBuffer();

        Pattern p = Pattern.compile("(?i)\\[\\[(.*?)\\]\\]", Pattern.DOTALL);
        Matcher m = p.matcher(this.getWikitext().split("==")[0]);
        while(m.find()) {
            String link = m.group(1).toString();
            String[] link_array = link.split(Pattern.quote("|"));
            String replace = link_array[0];
            if(link_array.length > 1){
                replace = link_array[1];
            }

            try {
                m.appendReplacement(wikitext, Matcher.quoteReplacement(replace));

            }catch (IndexOutOfBoundsException e){
                System.out.println("Error: Could not add: " + link + " in Article: " + this.getTitle());
            }


        }
        m.appendTail(wikitext);

        return wikitext.toString();

    }

    */


    public String getTitle() {
        return title;
    }

    /**
     * Setzt den Titel des Artikels.
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    public String toString() {
        return this.title;
    }


    /**
     * Gibt sämtliche in der Methode "handleTemplates" ermittelten Templates zurück. Die jeweilige ID der Templates
     * ist ein Integer-Wert.
     *
     * @return Sämtliche Templates, welche gefunden wurden.
     */
    public HashMap<Integer, Template> getTemplates() {
        return templates;
    }

    public int getNamspace() {
        return namspace;
    }

    /**
     * Setzt den Namespace des Artikels.
     *
     * @param namspace die Namespace-ID.
     */
    public void setNamspace(int namspace) {
        this.namspace = namspace;
    }

    /**
     * Gibt die Liste sämtlicher Link-Tokens zurück.
     *
     * @return Liste Sämtlicher Link-Tokens.
     */
    public TokenList getTokenList() {

        ArrayList<Link> links= getLinks(wikiTextDeletedTemplates);
        ArrayList<LinkToken> filteredList = new ArrayList<>();
        ArrayList<String> addedLinks = new ArrayList<>();
        /*
                for (LinkToken token : tokenList.getTokens()){
            for (Link link: links){
                if(token.getLink().equals(link.getUrl()) && !addedLinks.contains(token.getLink())){

                    System.out.println(link);

                    filteredList.add(token);
                    addedLinks.add(token.getLink());
                    break;
                }
                //System.out.println(token.getLink() + " : " + link.getUrl() + " ("+filteredList.size()+")");
            }
        }

         */


        for (LinkToken token : tokenList.getTokens()){
            for (Link link: links){
                if(token.getLink().equals(link.getUrl())){
                    if( !addedLinks.contains(token.getLink())){

                        filteredList.add(token);
                        addedLinks.add(token.getLink());
                        break;
                    }else{
                        //System.out.println("1" + link);

                    }
                }else{
                    //System.out.println("2" + link);
                }
                //System.out.println(token.getLink() + " : " + link.getUrl() + " ("+filteredList.size()+")");
            }
        }

        tokenList.setTokens(filteredList);

        return tokenList;
    }


    /**
     * Gibt den Wikitext des Artikels zurück.
     *
     * @return Der Wikitext des Artikels.
     */
    public String getWikitext() {
        return wikitext;
    }

    /**
     * Gibt den Wikitext zurück, nachdem aus diesem sämtliche Templates gelöscht wurden.
     *
     * @return Wikitext mit gelöschten Templates.
     */
    public String getWikiTextDeletedTemplates() {
        return wikiTextDeletedTemplates;
    }

    /**
     * Gibt den Wikitext zurück, nachdem aus diesem sämtliche Templates durch einen Platzhalter ersetzt wurden.
     *
     * @return Wikitext mit ersetzten Templates.
     */
    public String getWikitextExcludedTemplates() {
        return wikitextExcludedTemplates;
    }
}
