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
public class ItMap<K> {
    final Map<K, Integer> map;
    final int nullVal;

    public ItMap(int intialCap, int nullVal) {
        this.map = new HashMap<>(intialCap);
        this.nullVal = nullVal;
    }

    public int put(K key, int value) {
        if(value == nullVal){
            throw new IllegalArgumentException("Null value "+value+" only for null");
        }
        Integer vOld = map.put(key, value);
        if (vOld == null) {
            return nullVal;
        }
        return vOld;
    }

    public int get(K key) {
        Integer old = map.get(key);
        if(old == null){
            return nullVal;
        }else{
            return old;
        }
    }
    
    public Set<K> keySet(){
        return map.keySet();
    }
    
    public Set<Map.Entry<K, Integer>> entrySet(){
        return map.entrySet();
    }
}
