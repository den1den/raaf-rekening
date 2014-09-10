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
class StMapRek<T extends HasNaam> extends StMapNaam<T> implements data.memory.interfaces.StMapRek<T>{

    StMap<T> rekening;
    
    public StMapRek(int initialCapacity) {
        super(initialCapacity);
        rekening = new StMap<T>(initialCapacity);
    }

    @Override
    public T getRek(String rekening) {
        return this.rekening.get(rekening);
    }

    @Override
    public void putRek(T t, String rekening) {
        //rekening filtering?
        put(t);
        this.rekening.put(t, rekening);
    }

    @Override
    public boolean replace(T old, T t) {
        return super.replace(old, t) || rekening.replace(old, t);
    }
}
