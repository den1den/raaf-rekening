/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.diplay;

import geld.RekeningHouder;
import geld.RekeningHouderInterface;
import geld.Transactie;
import java.text.DecimalFormat;
import java.util.Collection;

/**
 *
 * @author Dennis
 */
public abstract class Result {

    protected final DecimalFormat FORMAT = new DecimalFormat();

    abstract public <R extends RekeningHouderInterface> void showDetailed(R rekeninghouder);

    public void showTransacties(Iterable<Transactie> trs){
        for (Transactie transactie : trs) {
            showTransactie(transactie);
        }
    }
    
    public abstract void showTransactie(Transactie trs);

    public abstract <R extends RekeningHouderInterface> void showSchuld(Collection<R> van, RekeningHouder voorRekening);
}
