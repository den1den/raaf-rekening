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
public interface DyMapRekLast<T extends HasNaam> extends StMapRekLast<T>, DyMapRek<T> {

    public T findRekLast(String naam, String rekening, String lastRekening);
/**
 * Know LastRekening guess naam
 * @param naam
 * @param lastRekening
 * @return 
 */
    public T findLast(String naam, String lastRekening);
}
