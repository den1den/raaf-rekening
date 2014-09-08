/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package geld.OLD;

import tijd.Time;

/**
 *
 * @author Dennis
 */
public interface RekeningHouderContant {
    
    /**
     * 
     * @param aan betalen aan
     * @param bedrag het bedrag
     * @param referentie
     * @param datum mag null zijn
     * @return 
     */
    public Factuur betaalContant(RekeningHouder aan, int bedrag,
            ReferentieInterface referentie, Time datum);
    
    public int getBalansContant();
}
