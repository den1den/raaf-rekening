/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.memory.interfaces;

import java.util.Set;

/**
 *
 * @author Dennis
 * @param <T>
 */
public interface Map<T> {

    public T get(String index);
    
    public Set<T> getAll();
}
