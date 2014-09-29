/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.rekeningen;

import data.types.HasNaam;
import geld.Referentie;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Dennis
 */
public class Rekening extends HasNaam {

    private final Som bankRekening;
    private final Som contant;

    public Rekening(String naam) {
        this(naam, false);
    }

    public Rekening(String naam, boolean contant) {
        super(naam);
        this.bankRekening = new BankRekening();
        if (contant) {
            this.contant = new ContantRekening();
        } else {
            this.contant = null;
        }
    }

    public Som getContant() {
        return contant;
    }

    public Som getBankRekening() {
        return bankRekening;
    }

    protected void doRekeningBijSingle(int bedrag, Event e) {
        this.bankRekening.put(bedrag, e);
    }

    protected void doRekeningBijDuo(Rekening bij, int bedrag, Event e) {
        doRekeningBijSingle(bedrag, e);
        bij.doRekeningBijSingle(-bedrag, e);
    }

    private void doPin(int bedrag, Event e) {
        bankRekening.af(bedrag, e);
        contant.bij(bedrag, e);
    }

    private final List<Event> allHistory = new LinkedList<>();

    protected Event newE(Referentie referentie, String message) {
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

    public List<Event> getAllHistory() {
        return new ArrayList<>(allHistory);
    }

    protected Event newEInit(Referentie referentie) {
        String message = "Initialization ({0})";
        return new Event(new HasNaam[]{this}, referentie, message);
    }

    protected Event newEInit(HasNaam hasNaam, Referentie referentie) {
        String message = "Initialization ({0},{1})";
        return new Event(new HasNaam[]{this, hasNaam}, referentie, message);
    }

    private class BankRekening extends Som {

        @Override
        public String beschrijving() {
            return "Op de bank van " + Rekening.this.getNaam();
        }

    }

    private class ContantRekening extends Som {

        @Override
        public String beschrijving() {
            return "Contant geld van " + Rekening.this.getNaam();
        }

    }

    public void initBank(Referentie referentie, int bedrag) {
        Event e = newEInit(referentie);
        bankRekening.init();
        doRekeningBijSingle(bedrag, e);
    }
}
