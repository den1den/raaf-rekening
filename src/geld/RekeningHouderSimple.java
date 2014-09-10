/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package geld;

import java.util.List;


public class RekeningHouderSimple extends RekeningHouder {
    final String naam;

    public RekeningHouderSimple(String naam) {
        this.naam = naam;
    }

    @Override
    public String getNaam() {
        return naam;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+"["+getNaam()+"]";
    }
}
