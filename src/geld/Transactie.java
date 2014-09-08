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

    final private Datum datum;
    final private int bedrag;
    final private boolean af;
    final private Referentie referentie;

    public Transactie(Bonnetje bonnetje, boolean af) {
        this(bonnetje.getDate(), bonnetje.getBedrag(), af, bonnetje);
    }

    public Transactie(Datum datum, int bedrag, boolean af, Referentie referentie) {
        if (bedrag < 0 ||
                (bedrag == 0 && !(referentie instanceof Kookdag))) {
            throw new IllegalArgumentException("Geen transactie!");
        }
        if (referentie == null) {
            throw new IllegalArgumentException();
        }
        this.datum = datum;
        this.bedrag = bedrag;
        this.af = af;
        this.referentie = referentie;
    }

    Transactie getOposite() {
        return new Transactie(datum, bedrag, !af, referentie);
    }

    public int getBedrag() {
        return bedrag;
    }

    public Datum getDatum() {
        return datum;
    }

    public Referentie getReferentie() {
        return referentie;
    }

    public boolean isAf() {
        return af;
    }
}
