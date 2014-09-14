/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.geldImpl;

import data.types.HasNaam;
import geld.Referentie;
import java.util.HashMap;
import java.util.Map;

public abstract class LeenRekening implements HasSchulden, HasNaam {

    KrijgtNog krijgtNog = new KrijgtNog();

    @Override
    public KrijgtNog getKrijgtNog() {
        return krijgtNog;
    }

    @Override
    public void leentVan(HasSchulden hasSchulden, int verreken, Referentie referentie) {
        KrijgtNog thiS = getKrijgtNog();
        KrijgtNog that = hasSchulden.getKrijgtNog();
        
        thiS.Add(hasSchulden, -verreken, referentie);
        that.Add(this, verreken, referentie);
    }

    public static class Easy extends LeenRekening {

        final String naam;

        public Easy(String naam) {
            this.naam = naam;
        }

        @Override
        public String getNaam() {
            return naam;
        }
        
    }

    @Override
    public String toString() {
        return LeenRekening.class.getSimpleName()+"["+getNaam()+"]";
    }
    
    protected String className(){
        return getClass().getSimpleName();
    }
}
