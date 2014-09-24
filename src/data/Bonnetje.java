/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import data.types.HasBedrag;
import data.types.HasDatum;
import geld.Referentie;
import java.util.Comparator;
import java.util.List;
import tijd.Datum;

/**
 *
 * @author Dennis
 */
public class Bonnetje implements Referentie, HasBedrag, HasDatum{

    public static Comparator<Bonnetje> getByDate() {
        return new Comparator<Bonnetje>() {

            public int compare(Bonnetje o1, Bonnetje o2) {
                return o1.datum.compareTo(o2.datum);
            }
        };
    }

    final private int id;
    final private int bedrag;
    final private Persoon persoon;
    final private String pasEindigd;
    final private Datum datum;
    final private Winkel winkel;
    final private List<AankoopCat> items;

    public Bonnetje(int id, int bedrag, Persoon persoon, String pasEindigd,
          Datum datum, Winkel winkel, List<AankoopCat> items) {
        if(datum == null || winkel == null){
            throw new IllegalArgumentException();
        }
        this.id = id;
        this.bedrag = bedrag;
        this.persoon = persoon;
        this.pasEindigd = pasEindigd;
        this.datum = datum;
        this.winkel = winkel;
        this.items = items;
    }

    public Persoon getPersoon() {
        return persoon;
    }

    @Override
    public int getBedrag() {
        return bedrag;
    }

    @Override
    public String toString() {
        return "Bonnetje "+bedrag+" "+datum + " "+winkel + " "+items;
    }

    @Override
    public Datum getDatum() {
        return datum;
    }

    public Winkel getWinkel() {
        return winkel;
    }
}
