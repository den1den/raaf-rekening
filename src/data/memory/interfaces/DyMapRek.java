/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package data.memory.interfaces;

import data.Afschrift;
import data.types.HasNaam;

/**
 *
 * @author Dennis
 * @param <T>
 */
public interface DyMapRek<T extends HasNaam> extends StMapRek<T>, DyMapNaam<T>{
    /**
     * Know rekening guess naam.
     * @param naam
     * @param rekening
     * @return 
     */
    public T findRek(String naam, String rekening);
    public T findRek(Afschrift afschrift);
}
