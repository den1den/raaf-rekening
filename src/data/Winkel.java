/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import geld.rekeningen.Rekening;

/**
 *
 * @author Dennis
 */
public class Winkel extends Rekening {

    AankoopCat defaultAankoopCat;

    public Winkel(String naam) {
        this(naam, AankoopCat.ONBEKEND);
    }

    public Winkel(String naam, AankoopCat defaultAankoopCat) {
        super(naam);
        if (defaultAankoopCat == null) {
            throw new IllegalArgumentException();
        }
        this.defaultAankoopCat = defaultAankoopCat;
    }

    public void foundDefaultCat(AankoopCat defaultCat) {
        if (defaultAankoopCat == AankoopCat.ONBEKEND) {
            defaultAankoopCat = defaultCat;
        } else {
            new Error("ehh? eerst: " + defaultAankoopCat + " en nu dit: " + defaultCat + " (Nothing done)").printStackTrace();
        }
    }
}
