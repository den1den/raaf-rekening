/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package data.memory;

import data.types.HasNaam;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dennis
 */
class StMapRekLast<T extends HasNaam> extends StMapRek<T> implements data.memory.interfaces.StMapRekLast<T>{

    StMap<T> lastRekening;

    public StMapRekLast(int intialCapacity) {
        super(intialCapacity);
        lastRekening = new StMap<>(intialCapacity);
    }
    
    @Override
    public T getLast(String rekeningLast) {
        return lastRekening.get(rekeningLast);
    }

    @Override
    public T getRekLast(String rekening, String rekeningLast) {
        T rekT = getRek(rekening);
        T lasT = getLast(rekeningLast);
        if(rekT != null && lasT == null){
            putLast(rekT, rekeningLast);
            return rekT;
        }
        if(rekT == null && lasT != null){
            putRek(lasT, rekening);
            return lasT;
        }
        if(rekT!= null && lasT != null){
            try {
                throw new Exception();
            } catch (Exception ex) {
                Logger.getLogger(StMapRekLast.class.getName()).log(Level.SEVERE,
                        "Dubbele entry wss", ex);
            }
        }
        return null;
    }

    @Override
    public void putLast(T t, String rekeningLast) {
        put(t);
        lastRekening.put(t, rekeningLast);
    }

    @Override
    public boolean replace(T old, T t) {
        return super.replace(old, t) || lastRekening.replace(old, t);
    }
}
