/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.OLD;

import data.Bonnetje;
import tijd.Time;

/**
 *
 * @author Dennis
 */
abstract public class RekeningHouder extends RekeningHouderSimple implements ReferentieInterface{
/**
     *
     * @param aan betalen aan
     * @param bedrag het bedrag
     * @param referentie
     * @param datum mag null zijn
     * @return
     */
    public Factuur betaal(RekeningHouder aan, int bedrag,
            ReferentieInterface referentie, Time datum) {
        Factuur factuur = new Factuur(this, aan, bedrag, datum, referentie);
        moetBetalen(bedrag, referentie);
        aan.krijgtNog(bedrag, referentie);
        return factuur;
    }
    
    public Factuur betaal(RekeningHouder aan, Bonnetje bonnetje){
        return betaal(aan, bonnetje.getBedrag(), null, bonnetje.getDate());
    }
    
    public Factuur betaalAan(Bonnetje bonnetje){
        return betaal(null, bonnetje);
    }
    
    abstract public String getNaam();

    @Override
    public String getRef() {
        return getNaam();
    }

    @Override
    public String toString() {
        return getNaam();
    }
}
