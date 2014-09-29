/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import data.types.HasBedrag;
import data.types.HasDatum;
import geld.Referentie;
import java.beans.PropertyChangeSupport;
import java.util.HashSet;
import java.util.Set;
import tijd.Datum;

/**
 *
 * @author Dennis
 */
public class ContantRecord extends Referentie implements HasBedrag, HasDatum {

    private final Datum datum;
    private final int bedrag;
    private final Persoon eigenaar; //can be null
    private final String opmerking;
    private Afschrift afschrift = null;

    public ContantRecord(Datum datum, int bedrag, Persoon eigenaar, String opmerking) {
        if (datum == null || bedrag == 0 || opmerking == null || opmerking.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.datum = datum;
        this.bedrag = bedrag;
        this.eigenaar = eigenaar;
        this.opmerking = opmerking;
    }
    
    public boolean isPin(){
        return bedrag < 0;
    }
    
    public boolean isStorting(){
        return !isPin();
    }

    /**
     * @return the datum
     */
    @Override
    public Datum getDatum() {
        return datum;
    }

    /**
     * @return the bedrag
     */
    @Override
    public int getBedrag() {
        return Math.abs(bedrag);
    }

    /**
     * @return the eigenaar
     */
    public Persoon getEigenaar() {
        return eigenaar;
    }

    /**
     * @return the opmerking
     */
    public String getOpmerking() {
        return opmerking;
    }

    public Afschrift getAfschrift() {
        return afschrift;
    }

    public void setAfschrift(Afschrift afschrift) {
        if (afschrift == null) {
            throw new IllegalArgumentException();
        }
        this.afschrift = afschrift;
    }

    @Override
    public String toString() {
        if(isPin()){
            return "Pin"+hashCode();
        }
        if(isStorting()){
            return "Storting"+hashCode();
        }
        throw new UnsupportedClassVersionError();
    }
    
    public static class DoubleContantRecordSet{
        Set<ContantRecord> st;
        Set<ContantRecord> pn;

        public DoubleContantRecordSet(int cap) {
            st = new HashSet<>(cap);
            pn = new HashSet<>(st);
        }
        
        public boolean add(ContantRecord cr){
            if(cr.isStorting()){
                return st.add(cr);
            }
            if(cr.isPin()){
                return pn.add(cr);
            }
            throw new UnsupportedClassVersionError();
        }

        public Set<ContantRecord> stortingen() {
            return st;
        }

        public Set<ContantRecord> pins() {
            return pn;
        }
         
    }
}
