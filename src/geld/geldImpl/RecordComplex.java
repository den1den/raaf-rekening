/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package geld.geldImpl;

import geld.Referentie;


public class RecordComplex {
    String[] name;
    

    public RecordComplex(String... name) {
        this.name = name;
        throw new UnsupportedOperationException();
    }
    
    void record(Referentie r, int... bedrag){
        if(bedrag.length > name.length)
            throw new IllegalArgumentException();
        
    }
}
