package com.tb.ba.analysis.changenew;

/**
 * Repäsentiert eine Hashmap für die einzelnen Zeilen einer Datei.
 * Created by T. Britsch on 06.09.2015.
 */
public class LinesMap {

    public java.util.HashMap<String, Line> map =
            new java.util.HashMap<String, Line>();

    public Line get(String key) {
        return map.get(key);
    }

    public Line put(Line line) {
            if (map.containsKey(line.getUrl())) {
                // implement the logic you need here.
                // You might want to return `value` to indicate
                // that no changes applied
                return get(line.getUrl());
            } else {
                map.put(line.getUrl(), line);
                return line;
            }

    }


    public int size() {
        return map.size();
    }
}
