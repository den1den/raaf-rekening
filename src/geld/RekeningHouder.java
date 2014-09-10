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
import util.diplay.ResultPrintStream;

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
        if (bedrag < 0) {
            throw new IllegalArgumentException();
        }
        Relatie r;
        r = this.findRelatie(aan);
        r.moetBetalen(bedrag, referentie);
        
        //oposite
        r = aan.findRelatie(this);
        r.krijgtnog(bedrag, referentie);
    }

    @Override
    public void krijgtNog(RekeningHouder aan, int bedrag, Referentie referentie) {
        aan.betaaldNog(this, bedrag, referentie);
    }

    @Override
    public void geeft(RekeningHouder aan, int bedrag, Referentie referentie) {
        if (bedrag < 0) {
            throw new IllegalArgumentException();
        }
        Relatie r;
        r = this.findRelatie(aan);
        r.nu(true, bedrag, referentie);
        r = aan.findRelatie(this);
        r.nu(false, bedrag, referentie);
    }

    @Override
    public void krijgt(RekeningHouder aan, int bedrag, Referentie referentie) {
        aan.geeft(this, bedrag, referentie);
    }

    @Override
    public int getKrijgtNog(RekeningHouderInterface aan) {
        Relatie r = getRelatie(aan);
        if (r == null) {
            return 0;
        }
        return r.getKrijgtNog();
    }

    @Override
    public int getGekregen(RekeningHouderInterface aan) {
        Relatie r = getRelatie(aan);
        if (r == null) {
            return 0;
        }
        return r.getGekregen();
    }

    @Override
    public int getSchuld(RekeningHouderInterface aan) {
        Relatie r = getRelatie(aan);
        if (r == null) {
            return 0;
        }
        return r.getSchuld();
    }

    @Override
    public int getSchuld() {
        int saldo = 0;
        for (RekeningHouderInterface rhis : trackers.keySet()) {
            Relatie r = getRelatie(rhis);
            saldo += r.getSchuld();
        }
        return saldo;
    }

    @Override
    public List<TransactiesRecord> getTransacties(RekeningHouderInterface rhc) {
        Relatie r = getRelatie(rhc);
        if (r == null) {
            return new ArrayList<>(1);
        }
        ArrayList<TransactiesRecord> result = new ArrayList<>(r.geschiedenis.size());
        for (Transactie gesch : r.geschiedenis) {
            result.add(new TransactiesRecord(gesch, this, rhc));
        }
        return result;
    }

    public String relatieToString(RekeningHouderInterface rhi) {
        Relatie r = getRelatie(rhi);
        if (r == null) {
            return "geen";
        } else {
            return "heeft: " + r.gekregen + " krijgtNog:" + r.krijgtNog + " lastT: " + r.geschiedenis.getLast();
        }
    }

    private class Relatie {

        public Relatie() {
            indexesOfMoetNogGebeuren = new HashSet<>(geschiedenis.size() / 2);
        }

        /**
         * wat het onderwerp heeft gekregen
         */
        int gekregen = 0;
        /**
         * wat het onderwerp nog krijgt
         */
        int krijgtNog = 0;

        /**
         * Only add
         */
        final LinkedList<Transactie> geschiedenis = new LinkedList<>();
        final Set<Integer> indexesOfMoetNogGebeuren;

        void krijgtnog(int bedrag, Referentie referentie) {
            Transactie t = new Transactie(false, bedrag, referentie);
            krijgtNog += bedrag;
            indexesOfMoetNogGebeuren.add(geschiedenis.size());
            geschiedenis.add(t);
        }

        void moetBetalen(int bedrag, Referentie referentie) {
            Transactie t = new Transactie(true, bedrag, referentie);
            krijgtNog -= bedrag;
            indexesOfMoetNogGebeuren.add(geschiedenis.size());
            geschiedenis.add(t);
        }
        
        void betaald(int bedrag, Referentie referentie){
            Transactie t = new Transactie(false, bedrag, referentie);
            gekregen += bedrag;
            geschiedenis.add(t);
        }
        
        void ontvangt(int bedrag, Referentie referentie){
            Transactie t = new Transactie(true, bedrag, referentie);
            gekregen -= bedrag;
            geschiedenis.add(t);
        }

        void nu(boolean af, int bedrag, Referentie referentie) {
            Transactie t = new Transactie(af, bedrag, referentie);
            if (af) {
                gekregen -= bedrag;
            } else {
                gekregen += bedrag;
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

        public int getKrijgtNog() {
            return krijgtNog;
        }

        public int getGekregen() {
            return gekregen;
        }

        public int getSchuld() {
            return getKrijgtNog() - getGekregen();
        }
    }

    public static void main(String[] args) {
        System.out.println("testing:");
        
        RekeningHouder raaf = new RekeningHouderSimple("raaf");
        RekeningHouder den = new RekeningHouderSimple("den");
        
        Referentie r = new ReferentieSimple("Wasmachine");
        System.out.println("dennis koopt "+r+" voor raaf (50e)");
        den.krijgtNog(raaf, 5000, r);
        ResultPrintStream.showLastT(den, raaf);
        System.out.println();
        
        System.out.println("krijgt geld terug");
        den.krijgt(raaf, 4000, r);
        ResultPrintStream.showLastT(den, raaf);
        System.out.println();
        
        System.out.println("Testing done");
    }
}
