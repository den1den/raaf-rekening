/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import data.types.HasNaam;
import geld.RekeningHouderSimple;

/**
 *
 * @author Dennis
 */
public class Winkel extends RekeningHouderSimple implements HasNaam{

    
    AankoopCat defaultAankoopCat;
    String[] beschrijvingContains;

    public Winkel(String naam, AankoopCat defaultAankoopCat, String[] beschrijvingContains) {
        super(naam);
        this.defaultAankoopCat = defaultAankoopCat;
        this.beschrijvingContains = beschrijvingContains;
    }

    public void foundDefaultCat(AankoopCat defaultCat) {
        if (defaultAankoopCat == null) {
            defaultAankoopCat = defaultCat;
        } else {
            new Error("ehh? eerst: " + defaultAankoopCat + " en nu dit: " + defaultCat + " (Nothing done)").printStackTrace();
        }
    }
}
