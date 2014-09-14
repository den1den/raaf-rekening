/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package data.memory;

/**
 *
 * @author Dennis
 * @param <T>
 */
class StMap<T> extends Map<T>{

    public StMap(int intialCapacity) {
        super(intialCapacity);
    }
    
    public void put(T t, String index){
        map.put(filter(index), t);
    }

    public boolean replace(T old, T t) {
        boolean replace = false;
        for (java.util.Map.Entry<String, T> entry : map.entrySet()) {
            if(old.equals(entry.getValue())){
                //old enry detected
                replace = true;
                put(t, entry.getKey());
            }
        }
        return replace;
    }
}
