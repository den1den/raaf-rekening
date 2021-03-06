/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import data.types.HasDate;
import geld.Referentie;
import geld.RekeningHouder;
import tijd.Datum;

/**
 *
 * @author Dennis
 */
public class Afschrift 
        implements HasDate, Referentie {

    private final Datum datum;
    private final String van;
    private final String vanRekening;
    private final String dezeRekening;
    private final String code;
    private final boolean af;
    private final int bedrag;
    private final String mutatieSoort;
    private final String mededeling;
    
    private RekeningHouder vanRHouder;
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
        if (datum == null || van == null || vanRekening == null || dezeRekening==null ||code == null || mutatieSoort == null || mededeling == null) {
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

    public RekeningHouder getVanRHouder() {
        return vanRHouder;
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

    @Override
    public String getRef() {
        return toString();
    }

    @Override
    public Datum getDate() {
        return datum;
    }

    public void setVanRHouder(RekeningHouder vanRHouder) {
        if (vanRHouder == null) {
            throw new IllegalArgumentException();
        }
        this.vanRHouder = vanRHouder;
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
        if(isAf()){
            b = -bedrag;
        }else{
            b = bedrag;
        }
        return "Afschrift: "+datum+" "+b+" "+getMededeling();
    }
}
