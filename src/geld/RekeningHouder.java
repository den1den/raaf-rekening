/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld;

import geld.Transactie.Record;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class RekeningHouder implements RekeningHouderInterface {

    private final ProtectedImplementation pi;

    public RekeningHouder() {
        pi = new ProtectedImplementation(2);
    }

    @Override
    public final int getSchuld(RekeningHouderInterface rh) {
        return pi.schuld(rh);
    }

    @Override
    public void payBack(RekeningHouderInterface aan, int bedrag, Referentie referentie) {
        add(false, aan, bedrag, referentie);
        aan.add(true, aan, bedrag, referentie);
    }

    @Override
    public void add(boolean af, RekeningHouderInterface aan, int bedrag, Referentie referentie) {
        Transactie t = new Transactie(af, bedrag, referentie);
        pi.put(aan, t);
    }

    @Override
    public final List<Transactie> getTransactiesCopy(RekeningHouderInterface r) {
        List<Transactie> get = pi.getTransacties(r);
        if (get == null) {
            return new ArrayList<>(0);
        } else {
            return new ArrayList<>(get);
        }
    }

    @Override
    public void addSchuld(RekeningHouderInterface aan, int bedrag, Referentie referentie) {
        this.add(true, aan, bedrag, referentie);
        aan.add(false, this, bedrag, referentie);
    }

    @Override
    public String toString() {
        return getNaam();
    }

    @Override
    public List<Record> getAllTransacties() {
        return pi.getAllTransacties();
    }

    private class ProtectedImplementation {

        private ProtectedImplementation(int size) {
            this.transacties = new HashMap<>(size);
            this.schulden = new HashMap<>(size);
            this.allTransacties = new LinkedList<>();
        }

        private final Map<RekeningHouderInterface, List<Transactie>> transacties;
        private final Map<RekeningHouderInterface, Integer> schulden;
        private final List<Record> allTransacties;

        private int schuld(RekeningHouderInterface rh) {
            Integer schuld = schulden.get(rh);
            if (schuld == null) {
                return 0;
            } else {
                return schuld;
            }
        }

        private List<Transactie> createTransacties(RekeningHouderInterface r) {
            List<Transactie> list = getTransacties(r);
            if (list == null) {
                list = new LinkedList<>();
                transacties.put(r, list);
            }
            return list;
        }

        private List<Transactie> getTransacties(RekeningHouderInterface r) {
            return transacties.get(r);
        }

        private void put(RekeningHouderInterface aan, Transactie t) {
            List<Transactie> ts = createTransacties(aan);
            ts.add(t);

            int newBedrag;
            Record record;
            if (t.isAf()) {
                record = new Record(t, RekeningHouder.this, aan);
                newBedrag = schuld(aan) - t.getBedrag();
            } else {
                record = new Record(t, aan, RekeningHouder.this);
                newBedrag = schuld(aan) + t.getBedrag();
            }
            schulden.put(aan, newBedrag);

            allTransacties.add(record);
        }

        private List<Record> getAllTransacties() {
            return allTransacties;
        }

    }
}
