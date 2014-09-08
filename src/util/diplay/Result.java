/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util.diplay;

import geld.RekeningHouder;
import geld.Transactie;
import java.text.DecimalFormat;
import java.util.Collection;

/**
 *
 * @author Dennis
 */
public abstract class Result {
    abstract public <R extends RekeningHouder> void showBalances(Collection<R> collection);
    
    abstract public <R extends RekeningHouder> void showDetailed(R rekeninghouder);

    abstract public void showTransacties(Collection<Transactie> factuurs);
    
    protected final DecimalFormat FORMAT = new DecimalFormat();
}
