/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld;

import data.Bonnetje;
import tijd.Datum;


public class RekeningHouderI extends Rekening implements RekeningHouder {

    final String naam;

    public RekeningHouderI(String naam) {
        if (naam == null || naam.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.naam = naam;
    }

    @Override
    public String getNaam() {
        return naam;
    }
}
