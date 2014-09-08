/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.OLD;

/**
 *
 * @author Dennis
 */
public class VirtueleRekening extends RekeningHouder {

    final String naam;

    public VirtueleRekening(String naam) {
        this.naam = naam;
    }

    @Override
    public String getNaam() {
        return naam;
    }

}
