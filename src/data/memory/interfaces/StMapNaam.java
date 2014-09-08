/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package data.memory.interfaces;

import data.types.HasNaam;

/**
 *
 * @author Dennis
 * @param <T>
 */
public interface StMapNaam<T extends HasNaam> extends StMap<T>{

    @Override
    public T get(String naam);
    
    public void put(T t);

    @Override
    public void put(T t, String index);
}
