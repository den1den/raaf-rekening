/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.geldImpl;

import data.types.HasNaam;
import geld.Referentie;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Len rekening that iterates over history
 *
 * @author Dennis
 */
public abstract class LeenRekening implements HasSchulden, HasNaam, Iterable<RecordComplete> {

    SumMap krijgtNog = new SumMap();
    List<RecordComplete> history = new LinkedList<>();

    @Override
    public SumMap getKrijgtNogVan() {
        return krijgtNog;
    }

    public List<RecordComplete> getHistory() {
        return history;
    }
    
    public List<RecordComplete> getHistory(HasSchulden van) {
        //TODO: geschiedenis per persoon instellen. in sumMap verwerken? RecordComplete lijkt goed idee
        throw new UnsupportedOperationException();
    }
    

    @Override
    public void moetBetalenAan(HasSchulden hs, int bedrag, Referentie r) {
        if (bedrag < 0) {
            throw new IllegalArgumentException();
        }
        RecordComplete rc = new RecordComplete(getNaam() + " moet " + bedrag
                + " betalen aan " + hs.getNaam() + " voor: " + r,
                RecordComplete.SUMTYPE.krijgt_nog,
        );
        addHistory(rc);
        hs.addHistory(rc);

        hs.getKrijgtNogVan().add(this, bedrag);
        this.getKrijgtNogVan().add(hs, -bedrag);
    }

    @Override
    public void moetKrijgenVan(HasSchulden hs, int bedrag, Referentie r) {
        if (bedrag < 0) {
            throw new IllegalArgumentException();
        }
        this.getKrijgtNogVan().add(hs, bedrag);
        hs.getKrijgtNogVan().add(this, -bedrag);
    }

    @Override
    public Iterator<RecordComplete> iterator() {
        //iterate over history
        return history.iterator();
    }

    @Override
    public void addHistory(RecordComplete recordComplete) {
        history.add(recordComplete);
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
}
