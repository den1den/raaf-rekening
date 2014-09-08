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
 * @param <T>
 */
abstract class DyMapRekLast<T extends HasNaam> extends DyMapRek<T> implements data.memory.interfaces.DyMapRekLast<T>{
    StMap<T> lastRekening;

    public DyMapRekLast(int intialCapacity) {
        super(intialCapacity);
        lastRekening = new StMap<>(intialCapacity);
    }

    @Override
    abstract T create(String naam);

    @Override
    public T findRekLast(String naam, String rekening, String lastRekening) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T findLast(String naam, String lastRekening) {
        T t = getLast(lastRekening);
        if( t == null){
            t = find(naam);
            putLast(t, lastRekening);
        }else{
            T t2 = find(naam);
            if(t == t2){
                //in both Maps
            }else{
                try {
                    //different persons that are probably the same?
                    throw new Exception("Diff persons in db");
                } catch (Exception ex) {
                    Logger.getLogger(DyMapRek.class.getName()).log(Level.SEVERE, 
                            t + " and "+t2+" are prop the same...", ex);
                }
            }
        }
        return t;
    }

    @Override
    public T getLast(String rekeningLast) {
        return this.lastRekening.get(rekeningLast);
    }

    @Override
    public T getRekLast(String rekening, String rekeningLast) {
        T rek = getRek(rekening);
        T last = getLast(rekeningLast);
        throw new UnsupportedOperationException();
    }

    @Override
    public void putLast(T t, String rekeningLast) {
        this.put(t);
        this.lastRekening.put(t, rekeningLast);
    }
}
