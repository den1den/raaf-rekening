/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class Rekening implements RekeningI {

    private final Map<Rekening, List<Transactie>> schuldTransacties;
    private final Map<Rekening, Integer> schuldBedrag;

    public Rekening() {
        this.schuldTransacties = new HashMap<>(2);
        this.schuldBedrag = new HashMap<>(2);
    }

    @Override
    public final int getSchuld(Rekening rh) {
        Integer schuld = schuldBedrag.get(rh);
        if (schuld == null) {
            return 0;
        } else {
            return schuld;
        }
    }

    @Override
    public void payBack(Rekening aan, int bedrag, Referentie referentie) {
        add(false, aan, bedrag, referentie);
        aan.add(true, aan, bedrag, referentie);
    }

    @Override
    public void add(boolean af, Rekening aan, int bedrag, Referentie referentie) {
        List<Transactie> transacties = getNotNullList(aan);
        transacties.add(new Transactie(af, bedrag, referentie));
        int newBedrag;
        if (af) {
            newBedrag = getSchuld(aan) - bedrag;
        } else {
            newBedrag = getSchuld(aan) + bedrag;
        }
        schuldBedrag.put(aan, newBedrag);
    }

    private List<Transactie> getNotNullList(Rekening rh) {
        List<Transactie> ls = getTransacties(rh);
        if (ls == null) {
            ls = new LinkedList<>();
            schuldTransacties.put(rh, ls);
        }
        return ls;
    }
    
    @Override
    public final List<Transactie> getTransacties(Rekening r){
        return schuldTransacties.get(r);
    }
}
