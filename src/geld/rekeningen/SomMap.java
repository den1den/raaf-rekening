/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.rekeningen;

import data.types.HasNaam;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Dennis
 * @param <K>
 */
public class SomMap<K extends HasNaam> {

    private final SomMapParams params;
    private final Map<K, Som> all;
    private final Som total;

    SomMap(int initialCapacity, SomMapParams<K> params) {
        if (params == null) {
            throw new IllegalArgumentException();
        }
        this.params = params;
        this.all = new HashMap<>(initialCapacity);
        this.total = new TotalSom();
    }

    /**
     * Add a value to the sum of the key.
     */
    public void add(K key, int value, Event e) {
        if(key == null || e == null){
            throw new IllegalArgumentException();
        }
        Som s = all.get(key);
        if (s == null) {
            s = new SubSom(key);
            all.put(key, s);
        }

        total.put(value, e);
        s.put(value, e);
    }

    abstract static class SomMapParams<K> {

        abstract String getBeschrijvingTotaal();

        abstract String getBeschrijvingSubSom(K van);
    }

    private class SubSom extends Som {

        final K van;

        public SubSom(K van) {
            this.van = van;
        }

        @Override
        public String beschrijving() {
            return params.getBeschrijvingSubSom(van);
        }

    }

    private class TotalSom extends Som {

        @Override
        public String beschrijving() {
            return params.getBeschrijvingTotaal();
        }

    }
}
