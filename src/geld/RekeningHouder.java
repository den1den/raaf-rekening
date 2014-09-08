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


public abstract class RekeningHouder implements RekeningHouderInterface {

    private final Map<RekeningHouderInterface, List<Transactie>> schuldTransacties;
    private final Map<RekeningHouderInterface, Integer> schuldBedrag;

    public RekeningHouder() {
        this.schuldTransacties = new HashMap<>(2);
        this.schuldBedrag = new HashMap<>(2);
    }

    @Override
    public final int getSchuld(RekeningHouderInterface rh) {
        Integer schuld = schuldBedrag.get(rh);
        if (schuld == null) {
            return 0;
        } else {
            return schuld;
        }
    }

    @Override
    public void payBack(RekeningHouderInterface aan, int bedrag, Referentie referentie) {
        add(false, aan, bedrag, referentie);
        aan.add(true, aan, bedrag, referentie);
    }

    @Override
    public void add(boolean af, RekeningHouderInterface aan, int bedrag, Referentie referentie) {
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

    private List<Transactie> getNotNullList(RekeningHouderInterface rh) {
        List<Transactie> ls = getTransacties(rh);
        if (ls == null) {
            ls = new LinkedList<>();
            schuldTransacties.put(rh, ls);
        }
        return ls;
    }
    
    @Override
    public final List<Transactie> getTransacties(RekeningHouderInterface r){
        return schuldTransacties.get(r);
    }

    @Override
    public void addSchuld(RekeningHouderInterface aan, int bedrag, Referentie referentie) {
        this.add(true, aan, bedrag, referentie);
        aan.add(false, this, bedrag, referentie);
    }
    
}
