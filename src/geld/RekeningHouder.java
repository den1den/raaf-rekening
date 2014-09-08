/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld;

import data.Bonnetje;
import data.types.HasNaam;
import tijd.Datum;

/**
 *
 * @author Dennis
 */
public interface RekeningHouder extends HasNaam, Rekening {

    @Override
    public String getNaam();
    
    void leen(RekeningHouder rh, Transactie t);
    
    void betaal(RekeningHouder rh, Transactie t);
    
    void betaalVoor(RekeningHouder th, Transactie t);
    
    void ontvang(RekeningHouder rh, Transactie t);
}
