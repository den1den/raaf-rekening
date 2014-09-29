/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.rekeningen;

import data.types.HasNaam;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Dennis
 * @param <K>
 */
public abstract class SomMap<K extends HasNaam> implements Iterable<Som> {

    private final Map<K, Som> all;
    private final Som total;

    SomMap(int initialCapacity) {
        this.all = new HashMap<>(initialCapacity);
        this.total = new TotalSom();
    }

    public Som getTotal() {
        return total;
    }
    
    public Som get(K k){
        return all.get(k);
    }

    /**
     * Add a value to the sum of the key.
     */
    public void add(K key, int value, Event e) {
        if (key == null || e == null) {
            throw new IllegalArgumentException();
        }
        Som s = getFind(key);

        total.put(value, e);
        s.put(value, e);
    }
    
    private Som getFind(K key){
        Som s = get(key);
        if (s == null) {
            s = new SubSom(key);
            all.put(key, s);
        }
        return s;
    }

    abstract String getBeschrijvingTotaal();

    abstract String getBeschrijvingSubSom(K van);

    void init(K rl) {
        Som s = getFind(rl);
        s.init();
    }

    public class SubSom extends Som {

        final K van;

        public SubSom(K van) {
            this.van = van;
        }

        @Override
        public String beschrijving() {
            return getBeschrijvingSubSom(van);
        }
    }

    private class TotalSom extends Som {

        @Override
        public String beschrijving() {
            return getBeschrijvingTotaal();
        }

    }

    @Override
    public Iterator<Som> iterator() {
        return new HashSet<>(all.values()).iterator();
    }
}
