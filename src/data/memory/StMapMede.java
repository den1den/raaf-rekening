/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package data.memory;

import data.types.HasNaam;

/**
 *
 * @author Dennis
 */
public class StMapMede<T extends HasNaam> extends StMapRek<T> {

    public StMapMede(int initialCapacity) {
        super(initialCapacity);
    }

    public T getMede(String mededeling) {
        return super.getRek(mededeling);
    }

    public void putMede(T t, String mededeling) {
        super.putRek(t, mededeling);
    }
    
}
