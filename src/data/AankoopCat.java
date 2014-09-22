/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

/**
 *
 * @author Dennis
 */
public class AankoopCat {

    final String naam;

    public AankoopCat(String naam) {
        this.naam = naam;
    }

    boolean heet(String naam) {
        return naam.trim().equalsIgnoreCase(this.naam.trim());
    }

    @Override
    public String toString() {
        return naam;
    }
    
    public static final AankoopCat ONBEKEND = new Onbekend();

    private static final class Onbekend extends AankoopCat {

        public Onbekend() {
            super("Onbekend");
        }

    }
}
