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
public interface RekeningHouder extends HasNaam, RekeningI {

    @Override
    public String getNaam();
    
}
