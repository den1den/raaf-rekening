/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package geld;

import java.util.List;
import tijd.Datum;

/**
 *
 * @author Dennis
 */
public class ReferentieMultiple implements Referentie {

    List<Referentie> refs;

    public ReferentieMultiple(List<Referentie> refs) {
        if(refs.size() <= 1){
            throw new IllegalArgumentException();
        }
        this.refs = refs;
    }

    @Override
    public Datum getDatum() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
