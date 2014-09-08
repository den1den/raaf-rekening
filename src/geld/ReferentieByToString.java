/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package geld;


public class ReferentieByToString implements Referentie {

    @Override
    public String getRef() {
        return toString();
    }
    
}
