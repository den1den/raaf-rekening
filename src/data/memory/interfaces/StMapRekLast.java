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
 */
public interface StMapRekLast<T extends HasNaam> extends StMapRek<T>{
    
    public T getLast(String rekeningLast);
    
    public T getRekLast(String rekening, String rekeningLast);
    
    public void putLast(T t, String rekeningLast);
    
}
