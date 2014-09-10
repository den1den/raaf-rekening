/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld;

import data.Afschrift;
import data.Incasso;
import geld.TransactiesRecord;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    public void addSchuld(RekeningHouderInterface aan, int bedrag, Referentie referentie) {
        aan.add(false, this, bedrag, referentie);
        add(true, aan, bedrag, referentie);
    }

    @Override
    public void payBack(RekeningHouderInterface aan, int bedrag, Referentie referentie) {
        aan.addSchuld(this, bedrag, referentie);
        try {
            //rather not used
            throw new Exception("Rathe rnot used");
        } catch (Exception ex) {
            Logger.getLogger(RekeningHouder.class.getName()).log(Level.INFO, null, ex);
        }
    }

    @Override
    public void verwerk(RekeningHouder AfsVan, Afschrift afschrift) {
        int bedrag = afschrift.getBedrag();
        if (afschrift.isAf()) {
            addSchuld(AfsVan, bedrag, afschrift);
        } else {
            AfsVan.addSchuld(this, bedrag, afschrift);
        }
    }

    @Override
    public void add(boolean af, RekeningHouderInterface aan, int bedrag, Referentie referentie) {
        Transactie t = new Transactie(af, bedrag, referentie);
        pi.put(aan, t);
    }

    @Override
    public final List<Transactie> getTransactiesRef(RekeningHouderInterface r) {
        return pi.getTransacties(r);
    }

    @Override
    public final List<Transactie> getTransactiesCopy(RekeningHouderInterface r) {
        List<Transactie> get = getTransactiesRef(r);
        if (get == null) {
            return new ArrayList<>(0);
        } else {
            return new ArrayList<>(get);
        }
    }

    @Override
    public String toString() {
        return getNaam() + " €" + ((double) pi.getTotaal()) / 100;
    }

    @Override
    public List<TransactiesRecord> getAllTransacties() {
        return pi.getAllTransacties();
    }

    public List<TransactiesRecord> getAllTransacties(RekeningHouderInterface rhi) {
        List<Transactie> trs = getTransactiesRef(rhi);
        if (trs == null) {
            return new ArrayList<>(0);
        } else {
            List<TransactiesRecord> trsR = new LinkedList<>();
            for (Transactie tr : trs) {
                if (tr.isAf()) {
                    trsR.add(new TransactiesRecord(tr, this, rhi));
                } else {
                    trsR.add(new TransactiesRecord(tr, rhi, this));
                }
            }
            return trsR;
        }
    }

    private class ProtectedImplementation {

        private ProtectedImplementation(int size) {
            this.transacties = new HashMap<>(size);
            this.schulden = new HashMap<>(size);
            this.allTransacties = new LinkedList<>();
        }

        private final Map<RekeningHouderInterface, List<Transactie>> transacties;
        private final Map<RekeningHouderInterface, Integer> schulden;
        private final List<TransactiesRecord> allTransacties;
        private int totaal;

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
            TransactiesRecord record;
            if (t.isAf()) {
                record = new TransactiesRecord(t, RekeningHouder.this, aan);
                newBedrag = schuld(aan) - t.getBedrag();
                totaal -= t.getBedrag();
            } else {
                record = new TransactiesRecord(t, aan, RekeningHouder.this);
                newBedrag = schuld(aan) + t.getBedrag();
                totaal += t.getBedrag();
            }
            schulden.put(aan, newBedrag);

            allTransacties.add(record);
        }

        private List<TransactiesRecord> getAllTransacties() {
            return allTransacties;
        }

        private int getTotaal() {
            return totaal;
        }

    }
}
