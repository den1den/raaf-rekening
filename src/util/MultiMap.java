/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dennis
 */
public class MultiMap<K, V> {
    Map<K, List<V>> map = new HashMap<>();
    List<V> put(K key, V value){
        List<V> vOld = map.get(key);
        if(vOld == null){
            vOld = new LinkedList<>();
            map.put(key, vOld);
        }
        vOld.add(value);
        return vOld;
    }
    List<V> get(K key){
        return map.get(key);
    }
    List<V> find(K key){
        List<V> old = get(key);
        if(old == null){
            return new ArrayList<>(0);
        }else{
            return old;
        }
    }
}
