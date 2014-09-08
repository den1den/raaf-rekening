/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package data.memory;

import data.types.HasNaam;

/**
 *
 * @author Dennis
 * @param <T>
 */
class StMapNaam<T extends HasNaam> extends StMap<T> implements data.memory.interfaces.StMapNaam<T>{

    public StMapNaam(int intialCapacity) {
        super(intialCapacity);
    }

    @Override
    public T get(String naam) {
        return super.get(naam); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public void put(T t){
        T old = map.put(t.getNaam(), t);
        if(old != null){
            if(old != t){
                throw new IllegalArgumentException("Overwite of "+t+" over "+old+" not permitted");
            }
        }
    }
}
