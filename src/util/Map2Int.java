/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Dennis
 */
public class Map2Int<K> {

    final int intitalCap;
    final Map<K, Integer> map;
    final int nullVal;

    public Map2Int(int intialCap) {
        this(intialCap, -1);
    }

    public Map2Int(int intialCap, int nullVal) {
        this.intitalCap = intialCap;
        this.map = new HashMap<>(intialCap);
        this.nullVal = nullVal;
    }

    /**
     * 
     * @param key
     * @param value
     * @return previous value or this.nullVal
     */
    public int put(K key, int value) {
        if (value == nullVal) {
            throw new IllegalArgumentException("Null value " + value + " only for null");
        }
        Integer vOld = map.put(key, value);
        if (vOld == null) {
            return nullVal;
        }
        return vOld;
    }
    
    public void putOnlyNew(K key, int value){
        if(put(key, value) == nullVal){
            throw new UnsupportedOperationException("Trying to overwrite...");
        }
    }

    public int get(K key) {
        Integer old = map.get(key);
        if (old == null) {
            return nullVal;
        } else {
            return old;
        }
    }

    public Set<K> keySet() {
        return map.keySet();
    }

    public Set<Map.Entry<K, Integer>> entrySet() {
        return map.entrySet();
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
