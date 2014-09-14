/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.geldImpl;

import geld.Referentie;

public class RecordComplete {

    private final String beschrijving;
    private final SUMTYPE sumtype;
    private int subjectI;
    private int otherI;

    public RecordComplete(String beschrijving, SUMTYPE sumtype, int subjectI, int otherI) {
        if (beschrijving == null || beschrijving.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.beschrijving = beschrijving;
        this.sumtype = sumtype;
        this.subjectI = subjectI;
        this.otherI = otherI;
    }

    public enum SUMTYPE {

        krijgt_nog
    }
}
