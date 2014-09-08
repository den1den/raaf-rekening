/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package geld.OLD;

import tijd.Datum;

/**
 *
 * @author Dennis
 */
public class Betaling {
    final private ReferentieInterface referentie;
    final private int bedrag;
    final private boolean af;
    private Datum datum;

    public Betaling(ReferentieInterface referentie, int bedrag, boolean af) {
        this(referentie, bedrag, af, null);
    }

    public Betaling(ReferentieInterface referentie, int bedrag, boolean af, Datum datum) {
        if(bedrag < 0){
            throw new IllegalArgumentException();
        }
        this.referentie = referentie;
        this.bedrag = bedrag;
        this.af = af;
        this.datum = datum;
    }

    public ReferentieInterface getReferentie() {
        return referentie;
    }

    public int getBedrag() {
        return bedrag;
    }

    public Datum getDatum() {
        return datum;
    }

    public boolean isAf() {
        return af;
    }
}
