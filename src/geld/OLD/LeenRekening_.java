/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.OLD;

import geld.OLD.HasSchulden;
import geld.rekeningen.Event;
import data.types.HasNaam;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Len rekening that iterates over history
 *
 * @author Dennis
 */
public abstract class LeenRekening_ extends Rekening implements HasSchulden, HasNaam {

    private final SumMap_<HasSchulden> krijgtNogVan = new Leneingen();
    protected final List<Event> history = new LinkedList<>();

    public LeenRekening_() {
    }

    

    

    @Override
    public int getKrijgtNogVan() {
        return krijgtNogVan.get();
    }
    
    public Sum_ getKrijgtNogVanSum(){
        return krijgtNogVan.getTotal();
    }

    @Override
    public int getKrijgtNogVan(HasSchulden iemand) {
        return krijgtNogVan.get(iemand);
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
        return LeenRekening.class.getSimpleName() + "[" + getNaam() + "]";
    }

    protected String className() {
        return getClass().getSimpleName();
    }

    private class Leneingen extends SumMap_<HasSchulden> {

        @Override
        String getNaam() {
            return LeenRekening.this.getNaam() + " leent";
        }

    }

    

    @Override
    public List<Event> getHistory() {
        return history;
    }
    
    public List<Event> getSortedHistory(){
        ArrayList<Event> list = new ArrayList<>(history);
        Collections.sort(list);
        return list;
    }
}
