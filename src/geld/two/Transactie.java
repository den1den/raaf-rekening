/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.two;

import geld.*;
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
        if ( referentie == null
                || ( (referentie instanceof Kookdag) && bedrag < 0 )
                || ( !(referentie instanceof Kookdag) && bedrag <= 0 ) ) {
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
}
