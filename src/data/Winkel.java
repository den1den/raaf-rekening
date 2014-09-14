/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import data.types.HasNaam;
import geld.Rekening;

/**
 *
 * @author Dennis
 */
public class Winkel extends Rekening implements HasNaam{

    final String naam;
    AankoopCat defaultAankoopCat;

    public Winkel(String naam, AankoopCat defaultAankoopCat) {
        this.naam = naam;
        this.defaultAankoopCat = defaultAankoopCat;
    }


    public void foundDefaultCat(AankoopCat defaultCat) {
        if (defaultAankoopCat == null) {
            defaultAankoopCat = defaultCat;
        } else {
            new Error("ehh? eerst: " + defaultAankoopCat + " en nu dit: " + defaultCat + " (Nothing done)").printStackTrace();
        }
    }

    @Override
    public String getNaam() {
        return naam;
    }
}
