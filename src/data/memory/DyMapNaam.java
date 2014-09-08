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
 */
abstract class DyMapNaam<T extends HasNaam> extends DyMap<T> implements data.memory.interfaces.DyMapNaam<T>{
    public DyMapNaam(int intialCapacity) {
        super(intialCapacity);
    }

    @Override
    public void put(T t) {
        put(t, t.getNaam());
    }
    
    abstract T create(String naam);

}
