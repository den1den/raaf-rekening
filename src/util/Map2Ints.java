/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import geld.rekeningen.Som;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 *
 * @author Dennis
 * @param <K>
 */
public class Map2Ints<K> extends HashMap<K, List<Integer>> {

    final int intitalCap;

    public Map2Ints(int intialCap) {
        super(intialCap);
        this.intitalCap = intialCap;
    }

    private Map2Ints(Map<? extends K, ? extends LinkedList<Integer>> m) {
        super(m);
        this.intitalCap = m.size();
    }

    public void put(K key, int i) {
        List<Integer> val = get(key);
        if (val == null) {
            val = new LinkedList<>();
            put(key, val);
        }
        val.add(i);
    }

    public static class Map2IntsAcending<K> extends Map2Ints<K> {

        public Map2IntsAcending(int intialCap) {
            super(intialCap);
        }

        @Override
        public void put(K key, int i) {
            List<Integer> val = get(key);
            if (val == null) {
                val = new LinkedList<>();
                put(key, val);
            } else if (val.get(val.size() - 1) > i) {
                throw new IllegalArgumentException("Not ascending!");
            }
            val.add(i);
        }

        @Override
        public List<Integer> put(K key, List<Integer> value) {
            if (value.size() > 1) {
                throw new UnsupportedOperationException();
            }
            return super.put(key, value);
        }

        @Override
        public void putAll(Map<? extends K, ? extends List<Integer>> m) {
            throw new UnsupportedOperationException();
        }
    }

}
