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
    public void geeft(RekeningHouder aan, int bedrag, Referentie referentie) {
        if(bedrag < 0){
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
        aan.betaald(this, bedrag, referentie);
    }

    @Override
    public int getBetaaldNog(RekeningHouderInterface aan) {
        Relatie r = getRelatie(aan);
        if (r == null) {
            return 0;
        }
        return r.getKrijgtNog();
    }

    @Override
    public int getBetaald(RekeningHouderInterface aan) {
        Relatie r = getRelatie(aan);
        if (r == null) {
            return 0;
        }
        return r.getHeeft();
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

        int heeft = 0;
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
                heeft -= bedrag;
            } else {
                heeft += bedrag;
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

        public int getHeeft() {
            return heeft;
        }

        public int getSaldo() {
            return getKrijgtNog() - getHeeft();
        }
    }


}
