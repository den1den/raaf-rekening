/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import data.types.HasDate;
import geld.Referentie;
import java.util.Comparator;
import tijd.Datum;

/**
 *
 * @author Dennis
 */
public class Afschrift
        implements Referentie, HasDate {

    private final Datum datum;
    private final String van;
    private final String vanRekening;
    private final String dezeRekening;
    private final String code;
    private final boolean af;
    private final int bedrag;
    private final String mutatieSoort;
    private final String mededeling;

    /**
     *
     * @param datum
     * @param van
     * @param vanRekening
     * @param dezeRekening
     * @param code
     * @param af
     * @param bedrag
     * @param mutatieSoort
     * @param mededeling
     */
    public Afschrift(Datum datum, String van, String vanRekening, String dezeRekening, String code, boolean af, int bedrag, String mutatieSoort, String mededeling) {
        if (datum == null || van == null || vanRekening == null || dezeRekening == null || code == null || mutatieSoort == null || mededeling == null) {
            throw new IllegalArgumentException();
        }
        this.datum = datum;
        this.van = van;
        this.vanRekening = vanRekening;
        this.dezeRekening = dezeRekening;
        this.code = code;
        this.af = af;
        this.bedrag = bedrag;
        this.mutatieSoort = mutatieSoort;
        this.mededeling = mededeling;
    }

    public String getDezeRekening() {
        return dezeRekening;
    }

    public String getCode() {
        return code;
    }

    public boolean isAf() {
        return af;
    }

    public int getBedrag() {
        return bedrag;
    }

    public String getMutatieSoort() {
        return mutatieSoort;
    }

    public String getMededeling() {
        return mededeling;
    }

    public String getVan() {
        return van;
    }

    public String getVanRekening() {
        return vanRekening;
    }

    @Override
    public String toString() {
        int b;
        if (isAf()) {
            b = -bedrag;
        } else {
            b = bedrag;
        }
        return "Afschrift: " + datum + " " + b + " " + getMededeling();
    }

    @Override
    public Datum getDate() {
     return datum;   
    }

    public static class CompByDate implements Comparator<Afschrift>{

        @Override
        public int compare(Afschrift o1, Afschrift o2) {
            return o1.getDate().compareTo(o2.getDate());
        }
    }
}
