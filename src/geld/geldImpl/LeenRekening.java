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

    SumMap krijgtNog = new SumMap();

    @Override
    public SumMap getKrijgtNogVan() {
        return krijgtNog;
    }
    
    @Override
    public void moetBetalenAan(HasSchulden hs, int bedrag, Referentie r){
        if(bedrag < 0){
            throw new IllegalArgumentException();
        }
        hs.getKrijgtNogVan().add(this, bedrag, r);
        this.getKrijgtNogVan().add(hs, -bedrag, r);
    }
    
    @Override
    public void moetKrijgenVan(HasSchulden hs, int bedrag, Referentie r){
        if(bedrag < 0){
            throw new IllegalArgumentException();
        }
        this.getKrijgtNogVan().add(hs, bedrag, r);
        hs.getKrijgtNogVan().add(this, -bedrag, r);
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
