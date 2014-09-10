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
public interface StMapMede<T extends HasNaam> extends StMapNaam<T>{
    T getMede(String mededeling);
    void putMede(T t, String mededeling);
}
