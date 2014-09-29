/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.memory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Dennis
 * @param <T>
 */
class StMap<T> implements Iterable<T>{

    private final int intialCapacity;
    private final java.util.Map<String, T> map;

    public StMap(int intialCapacity) {
        this.intialCapacity = intialCapacity;
        this.map = new HashMap<>(intialCapacity);
    }
    
    protected String filter(String index) {
        return index.toUpperCase();
    }
    
    public void put(T t, String index){
        T old = doPut(t, index);
        if(old != null && old != t){
            throw new UnsupportedClassVersionError();
        }
    }

    protected T doPut(T t, String index) {
        return map.put(filter(index), t);
    }
    
    public T get(String index) {
        return map.get(filter(index));
    }

    public boolean replace(T old, T t) {
        boolean replace = false;
        for (java.util.Map.Entry<String, T> entry : map.entrySet()) {
            if (old.equals(entry.getValue())) {
                //old enry detected
                replace = true;
                put(t, entry.getKey()); //should be doPut
            }
        }
        return replace;
    }

    

    public Set<T> getAll() {
        return new HashSet<>(map.values());
    }

    

    public int size() {
        return map.size();
    }

    @Override
    public String toString() {
        return map.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new HashSet<>(this.map.values()).iterator();
    }
}
