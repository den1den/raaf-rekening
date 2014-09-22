/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.rekeningen;

import data.Bonnetje;
import data.types.HasNaam;
import geld.Referentie;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Dennis
 */
public class Rekening extends HasNaam {

    private final Som bankRekening;

    public Rekening(String naam) {
        super(naam);
        this.bankRekening = new BankRekening();
    }
    
    public void Af(int bedrag, Referentie referentie){
        String message = "Er gaat iets af";
        Event e = newE(referentie, message);
        this.bankRekening.af(bedrag, e);
    }
    
    protected void doVerreken(int bedrag, Event e){
        this.bankRekening.put(bedrag, e);
    }
    
    public void Bij(int bedrag, Referentie referentie){
        String message = "Er gaat iets bij";
        Event e = newE(referentie, message);
        this.bankRekening.bij(bedrag, e);
    }
    
    public void besteedDirect(Bonnetje bonnetje){
        besteedDirect(bonnetje.getWinkel(), bonnetje.getBedrag(), bonnetje);
    }
    
    public void besteedDirect(Rekening bij, int bedrag, Referentie referentie){
        String message = "{0} heeft iets gekocht bij {1}";
        Event e = newE(bij, referentie, message);
        doBesteedDirect(bij, bedrag, e);
    }
    
    protected void doBesteedDirect(Rekening bij, int bedrag, Event e){
        doVerreken(bedrag, e);
        bij.doVerreken(-bedrag, e);
    }
    
    protected void doVerreken(Rekening bij, int bedrag, Event e) {
        doVerreken(bedrag, e);
        bij.doVerreken(-bedrag, e);
    }

    private final List<Event> allHistory = new LinkedList<>();

    private Event newE(Referentie referentie, String message){
        Event e = new Event(new HasNaam[]{this}, referentie, message);
        allHistory.add(e);
        return e;
    }
    
    protected Event newE(HasNaam betrokken0, Referentie referentie, String message) {
        Event e = new Event(new HasNaam[]{this, betrokken0}, referentie, message);
        allHistory.add(e);
        return e;
    }

    protected Event newE(HasNaam betrokken0, HasNaam betrokken1, Referentie referentie, String message) {
        Event e = new Event(new HasNaam[]{this, betrokken0, betrokken1}, referentie, message);
        allHistory.add(e);
        return e;
    }

    private class BankRekening extends Som {

        @Override
        public String beschrijving() {
            return "Op de bank van " + Rekening.this.getNaam();
        }

    }

    public Som getBankRekening() {
        return bankRekening;
    }
    
}
