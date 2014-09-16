/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld;

import data.types.HasNaam;
import java.util.LinkedList;
import java.util.List;

/**
 * Len rekening that iterates over history
 *
 * @author Dennis
 */
public abstract class LeenRekening implements HasSchulden, HasNaam {

    private final SumMap<HasSchulden> krijgtNogVan = new Leneingen();
    protected final List<Event> history = new LinkedList<>();

    public LeenRekening() {
    }

    @Override
    public void moetBetalenAan(LeenRekening lr, int bedrag, Referentie r) {
        if (bedrag < 0) {
            throw new IllegalArgumentException();
        }

        String message = getNaam() + " moet betalen aan " + lr.getNaam();

        Event e = newE(lr, r, message);

        doBetalenAan(lr, bedrag, e);
    }

    void doBetalenAan(LeenRekening lr, int bedrag, Event e) {
        this.krijgtNogVan.add(lr, -bedrag, e);
        history.add(e);
        lr.krijgtNogVan.add(this, bedrag, e);
        lr.history.add(e);
    }

    @Override
    public void moetKrijgenVan(LeenRekening lr, int bedrag, Referentie r) {
        if (bedrag < 0) {
            throw new IllegalArgumentException();
        }

        String message = getNaam() + " moet krijgen van " + lr.getNaam();

        Event e = newE(lr, r, message);

        doMoetKrijgenVan(lr, bedrag, e);
    }

    void doMoetKrijgenVan(LeenRekening lr, int bedrag, Event e) {
        this.krijgtNogVan.add(lr, bedrag, e);
        this.history.add(e);
        lr.krijgtNogVan.add(this, -bedrag, e);
        lr.history.add(e);
    }

    @Override
    public int krijgtNogVan() {
        return krijgtNogVan.get();
    }

    @Override
    public int krijgtNogVan(HasSchulden iemand) {
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
    
    private class Leneingen extends SumMap<HasSchulden>{

        @Override
        String getNaam() {
            return LeenRekening.this.getNaam()+" leent";
        }
        
    }
    
    Event newE(HasNaam betreft, Referentie referentie, String message){
        Event e = new Event(this, betreft, referentie, message);
        //history.add(e);
        return e;
    }

    @Override
    public List<Event> getHistory() {
        return history;
    }
}
