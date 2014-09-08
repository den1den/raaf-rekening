/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package geld;

import data.types.HasDate;
import tijd.Datum;

/**
 *
 * @author Dennis
 */
public interface ReferentieHasDate extends HasDate, Referentie{

    @Override
    public Datum getDate();
    
}
