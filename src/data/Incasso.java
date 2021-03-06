/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package data;

import data.types.HasNaam;
import geld.RekeningHouderI;

/**
 *
 * @author Dennis
 */
public class Incasso extends RekeningHouderI implements HasNaam{
    
    private final String rekeningNummer;

    public Incasso(String naam, String rekeningNummer) {
        super(naam);
        if(naam == null || naam.isEmpty() ||
                rekeningNummer == null ){
            throw new IllegalArgumentException();
        }
        this.rekeningNummer = rekeningNummer;
    }

    public String getRekeningNummer() {
        return rekeningNummer;
    }
}
