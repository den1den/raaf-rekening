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

public class Rekening implements RekeningI {

    private final Map<RekeningHouder, List<Transactie>> schuldTransacties;
    private final Map<RekeningHouder, Integer> schuldBedrag;

    public Rekening() {
        this.schuldTransacties = new HashMap<>(2);
        this.schuldBedrag = new HashMap<>(2);
    }

    @Override
    public int getSchuld(RekeningHouder rh) {
        Integer schuld = schuldBedrag.get(rh);
        if (schuld == null) {
            return 0;
        } else {
            return schuld;
        }
    }

    @Override
    public void addSchuld(RekeningHouder aan, int bedrag, Referentie referentie) {
        add(true, aan, bedrag, referentie);
    }

    @Override
    public void payBack(RekeningHouder aan, int bedrag, Referentie referentie) {
        add(false, aan, bedrag, referentie);
    }

    private void add(boolean af, RekeningHouder aan, int bedrag, Referentie referentie) {
        List<Transactie> transacties = getNotNullList(aan);
        transacties.add(new Transactie(af, bedrag, referentie));
        int newBedrag;
        if (af) {
            newBedrag = schuldBedrag.get(aan) - bedrag;
        } else {
            newBedrag = schuldBedrag.get(aan) + bedrag;
        }
        schuldBedrag.put(aan, newBedrag);
    }

    private List<Transactie> getNotNullList(RekeningHouder rh) {
        List<Transactie> ls = schuldTransacties.get(rh);
        if (ls == null) {
            ls = new LinkedList<>();
            schuldTransacties.put(rh, ls);
        }
        return ls;
    }
}
