/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.util.HashMap;

/**
 *
 * @author Dennis
 */
public class FrequencyMap<K>extends HashMap<K, Integer> {

    public FrequencyMap() {
        super();
    }

    public Integer put(K key) {
        Integer v = get(key);
        if(v == null){
            v = 1;
        }else{
            v++;
        }
        return put(key, v);
    }
    
    public K getMax(){
        
        K max = null;
        Integer freq = 0;
        
        for (Entry<K, Integer> entry : this.entrySet()) {
            Integer integer = entry.getValue();
            if(integer > freq){
                max = entry.getKey();
                freq = integer;
            }
        }
        
        return max;
    }
}
