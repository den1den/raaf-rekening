/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.diplay;

import data.Persoon;
import geld.RekeningHouder;
import geld.RekeningHouderContant;
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
    
    abstract public void listResultaat(Collection<? extends RekeningHouderInterface> rhs, RekeningHouder tov);

    abstract public <RH extends RekeningHouderInterface> void showDetailledTov(Collection<RH> subject, RH... rhs);
}
