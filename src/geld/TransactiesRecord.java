/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld;

/**
 *
 * @author Dennis
 */
public class TransactiesRecord {

    final private Transactie transactie;
    final private RekeningHouderInterface van;
    final private RekeningHouderInterface naar;

    public TransactiesRecord(Transactie transactie, RekeningHouderInterface van, RekeningHouderInterface naar) {
        this.transactie = transactie;
        this.van = van;
        this.naar = naar;
    }

    public Transactie getTransactie() {
        return transactie;
    }

    @Override
    public String toString() {
        RekeningHouderInterface van2;
        RekeningHouderInterface naar2;
        double bedrag = transactie.getBedrag();;
        if (transactie.isAf()) {
            //van -> naar
            van2 = van;
            naar2 = naar;
        } else {
            van2 = naar;
            naar2 = van;
        }
        return "TransactieR: €" + bedrag / 100 + " van: " + van + " naar: "
                + naar + " ref[" + transactie.getReferentie().toString() + "]";
    }
}
