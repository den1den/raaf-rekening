/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class RekeningHouder implements RekeningHouderInterface {

    private final Map<RekeningHouderInterface, Relatie> trackers;

    public RekeningHouder() {
        trackers = new HashMap<>(2);
    }

    private Relatie findRelatie(RekeningHouderInterface rhi) {
        Relatie r = getRelatie(rhi);
        if (r == null) {
            r = new Relatie();
            trackers.put(rhi, r);
        }
        return r;
    }

    private Relatie getRelatie(RekeningHouderInterface rhi) {
        return trackers.get(rhi);
    }

    @Override
    public void betaaldNog(RekeningHouder aan, int bedrag, Referentie referentie) {
        if(bedrag < 0){
            throw new IllegalArgumentException();
        }
        Relatie r;
        r = this.findRelatie(aan);
        r.nog(true, bedrag, referentie);
        r = aan.findRelatie(this);
        r.nog(false, bedrag, referentie);
    }

    @Override
    public void krijgtNog(RekeningHouder aan, int bedrag, Referentie referentie) {
        aan.betaaldNog(this, bedrag, referentie);
    }

    @Override
    public void betaald(RekeningHouder aan, int bedrag, Referentie referentie) {
        if(bedrag < 0){
            throw new IllegalArgumentException();
        }
        Relatie r;
        r = this.findRelatie(aan);
        r.nu(false, bedrag, referentie);
        r = aan.findRelatie(this);
        r.nu(false, bedrag, referentie);
    }

    @Override
    public void krijgt(RekeningHouder aan, int bedrag, Referentie referentie) {
        aan.betaald(this, bedrag, referentie);
    }

    @Override
    public int getBetaaldNog(RekeningHouderInterface aan) {
        Relatie r = getRelatie(aan);
        if (r == null) {
            return 0;
        }
        return r.getMoetNogGebeuren();
    }

    @Override
    public int getBetaald(RekeningHouderInterface aan) {
        Relatie r = getRelatie(aan);
        if (r == null) {
            return 0;
        }
        return r.getBetaald();
    }

    @Override
    public int getSaldo(RekeningHouderInterface aan) {
        Relatie r = getRelatie(aan);
        if (r == null) {
            return 0;
        }
        return r.getSaldo();
    }

    @Override
    public int getSaldo() {
        int saldo = 0;
        for (RekeningHouderInterface rhis : trackers.keySet()) {
            Relatie r = getRelatie(rhis);
            saldo += r.getSaldo();
        }
        return saldo;
    }

    @Override
    public List<TransactiesRecord> getTransacties(RekeningHouderInterface rhc) {
        Relatie r = getRelatie(rhc);
        if(r == null){
            return new ArrayList<>(1);
        }
        ArrayList<TransactiesRecord> result = new ArrayList<>(r.geschiedenis.size());
        for (Transactie gesch : r.geschiedenis) {
                result.add(new TransactiesRecord(gesch, this, rhc));
        }
        return result;
    }

    private class Relatie {

        public Relatie() {
            indexesOfMoetNogGebeuren = new HashSet<>(geschiedenis.size() / 2);
        }

        int betaald = 0;
        int krijgtNog = 0;

        /**
         * Only add
         */
        final List<Transactie> geschiedenis = new LinkedList<>();
        final Set<Integer> indexesOfMoetNogGebeuren;

        void nog(boolean af, int bedrag, Referentie referentie) {
            Transactie t = new Transactie(af, bedrag, referentie);
            if (af) {
                krijgtNog -= bedrag;
            } else {
                krijgtNog += bedrag;
            }
            indexesOfMoetNogGebeuren.add(geschiedenis.size());
            geschiedenis.add(t);
        }

        void nu(boolean af, int bedrag, Referentie referentie) {
            Transactie t = new Transactie(af, bedrag, referentie);
            if (af) {
                betaald -= bedrag;
            } else {
                betaald += bedrag;
            }
            geschiedenis.add(t);
        }

        List<Transactie> getAll() {
            return new ArrayList<>(geschiedenis);
        }

        List<Transactie> getMoetNogGebeurenLs() {
            List<Transactie> result = new ArrayList<>(indexesOfMoetNogGebeuren.size());
            int index = 0;
            for (Transactie t : geschiedenis) {
                if (indexesOfMoetNogGebeuren.contains(index)) {
                    result.add(t);
                }
                index++;
            }
            return result;
        }

        List<Transactie> getBetaaldLs() {
            List<Transactie> result = new ArrayList<>(geschiedenis.size() - indexesOfMoetNogGebeuren.size());
            int index = 0;
            for (Transactie t : geschiedenis) {
                if (!indexesOfMoetNogGebeuren.contains(index)) {
                    result.add(t);
                }
                index++;
            }
            return result;
        }

        public int getMoetNogGebeuren() {
            return krijgtNog;
        }

        public int getBetaald() {
            return betaald;
        }

        public int getSaldo() {
            return getBetaald() + getMoetNogGebeuren();
        }
    }

    /*
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
     public String toString() {
     return getNaam() + " €" + ((double) pi.getTotaal()) / 100;
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

     }*/
}
