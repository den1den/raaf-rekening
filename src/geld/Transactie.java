/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld;

import data.Bonnetje;
import data.Kookdag;
import tijd.Datum;

/**
 *
 * @author Dennis
 */
public class Transactie {

    final private boolean af;
    final private int bedrag;
    final private Referentie referentie;

    public Transactie(boolean af, int bedrag, Referentie referentie) {
        if (referentie == null
                || ((referentie instanceof Kookdag) && bedrag < 0)
                || (!(referentie instanceof Kookdag) && bedrag <= 0)) {
            throw new IllegalArgumentException();
        }
        this.af = af;
        this.bedrag = bedrag;
        this.referentie = referentie;
    }

    public boolean isAf() {
        return af;
    }

    public int getBedrag() {
        return bedrag;
    }

    public Referentie getReferentie() {
        return referentie;
    }

    public static class Record {

        final private Transactie transactie;
        final private RekeningHouderInterface van;
        final private RekeningHouderInterface naar;

        public Record(Transactie transactie, RekeningHouderInterface van, RekeningHouderInterface naar) {
            this.transactie = transactie;
            this.van = van;
            this.naar = naar;
        }

        public Transactie getTransactie() {
            return transactie;
        }

    }
}
