/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.memory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Dennis
 * @param <T>
 */
class Map<T> {

    protected final int intialCapacity;
    protected final java.util.Map<String, T> map;

    public Map(int intialCapacity) {
        this.map = new HashMap<>(intialCapacity);
        this.intialCapacity = intialCapacity;
    }

    public T get(String index) {
        return map.get(filter(index));
    }

    public Set<T> getAll() {
        return new HashSet<>(map.values());
    }

    String filter(String index) {
        return index.toUpperCase();
    }

    public int size() {
        return map.size();
    }
}
