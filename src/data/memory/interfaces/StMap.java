/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.memory.interfaces;

/**
 *
 * @author Dennis
 * @param <T>
 */
public interface StMap<T> extends Map<T> {

    public void put(T t, String index);

    public boolean replace(T old, T t);
}
