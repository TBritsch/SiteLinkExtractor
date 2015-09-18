package com.tb.ba.extraction.writer;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstrakte Klasse eines Tripels einer tsv-Datei.
 *
 * Created by T. Britsch on 09.07.2015.
 */
public abstract class TupleElement {

    Map<String, Object> map = new HashMap<String, Object>();

    public Map<String, Object> getElements(){
        return this.map;
    }

    public abstract String toString();



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TupleElement that = (TupleElement) o;

        return !(map != null ? !map.equals(that.map) : that.map != null);

    }

    @Override
    public int hashCode() {
        return map != null ? map.hashCode() : 0;
    }
}
