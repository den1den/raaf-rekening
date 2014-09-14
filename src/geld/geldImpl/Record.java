/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package geld.geldImpl;

import geld.Referentie;

/**
 *
 * @author Dennis
 */
public class Record {
    private int change;
    private Referentie referentie;

    public Record(int change, Referentie referentie) {
        this.change = change;
        this.referentie = referentie;
    }

    public int getChange() {
        return change;
    }

    public Referentie getReferentie() {
        return referentie;
    }
    
}
