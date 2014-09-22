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
 * @param <T>
 */
class StMapNaam<T extends HasNaam> extends StMap<T> {

    public StMapNaam(int intialCapacity) {
        super(intialCapacity);
    }

    @Override
    public T get(String naam) {
        if(naam.isEmpty()){
            throw new IllegalArgumentException();
        }
        return super.get(naam); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void put(T t){
        super.put(t, t.getNaam());
    }
}
