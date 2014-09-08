/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package geld;


public class RekeningHouderSimple extends RekeningHouder {
    final String naam;

    public RekeningHouderSimple(String naam) {
        this.naam = naam;
    }

    @Override
    public String getNaam() {
        return naam;
    }
    
}
