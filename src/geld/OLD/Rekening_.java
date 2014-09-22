/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.OLD;

import data.types.HasNaam;

/**
 *
 * @author Dennis
 */
public abstract class Rekening_ extends HasNaam {

    private final String naam;

    public Rekening_(String naam) {
        if (naam == null || naam.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.naam = naam;
    }

    @Override
    public String getNaam() {
        return naam;
    }

    public static class Onbekend extends Rekening {

        public Onbekend() {
            super("Onbekend");
        }
    }
}
