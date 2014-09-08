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
public interface DyMapNaam<T extends HasNaam> extends StMapNaam<T>, DyMap<T> {

    @Override
    public T find(String naam);

    @Override
    public T get(String naam);

}
