/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Dennis
 */
public class ItOverIt<T> implements Iterator<T>{
    
    Iterator<Iterator<T>> it;
    Iterator<T> curr = null;

    public ItOverIt(Iterator<T>... its) {
        it = java.util.Arrays.asList(its).iterator();
    }

    public ItOverIt(List<Iterator<T>> list) {
        it = new ArrayList<>(list).iterator();
    }

    public ItOverIt(Iterator<Iterator<T>> it) {
        this.it = it;
    }

    @Override
    public boolean hasNext() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T next() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
