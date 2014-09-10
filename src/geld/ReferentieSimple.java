/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package geld;

import tijd.Time;


public class ReferentieSimple implements Referentie {
final String referentie;

    public ReferentieSimple(String referentie) {
        if(referentie == null || referentie.isEmpty())
            throw new IllegalArgumentException();
        this.referentie = referentie;
    }

    @Override
    public String getRef() {
        return referentie;
    }
    
    public static final Referentie REFERENTIE_CONTANT = new ReferentieSimple("Contant");

    @Override
    public Time getTime() {
        return null;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+"["+getRef()+"]";
    }
}
