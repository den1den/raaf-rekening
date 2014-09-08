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
public class Factuur implements ReferentieInterface{
    RekeningHouder van;
    RekeningHouder naar;
    int bedrag;
    
    Time datum;
    ReferentieInterface referentie;

    protected Factuur(RekeningHouder van, RekeningHouder naar, int bedrag, Time datum, ReferentieInterface referentie) {
        this.van = van;
        this.naar = naar;
        this.bedrag = bedrag;
        this.datum = datum;
        this.referentie = referentie;
    }
    
    @Override
    public String getRef() {
        if(referentie == null){
            return "Geen referentie";
        }
        return  referentie.getRef();
    }
}
