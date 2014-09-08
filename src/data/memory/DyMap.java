/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.memory;

/**
 *
 * @author Dennis
 * @param <T>
 */
public abstract class DyMap<T> extends StMap<T> implements data.memory.interfaces.DyMap<T>{

    public DyMap(int intialCapacity) {
        super(intialCapacity);
    }
    
    abstract T create(String index);

    @Override
    public T find(String index) {
        T t = get(index);
        if (t == null) {
            t = create(index);
            put(t, index);
        }
        return t;
    }
}
