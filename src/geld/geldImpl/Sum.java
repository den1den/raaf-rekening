/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.geldImpl;

import geld.Referentie;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Dennis
 */
class Sum {

    private int total;
    List<Record> changes;

    public Sum() {
        this.total = 0;
        this.changes = new LinkedList<>();
    }

    public Sum(int bedrag, Referentie referentie) {
        this.total = bedrag;
        this.changes = new LinkedList<>();
        Record r = new Record(bedrag, referentie);
        this.changes.add(r);
    }
    
    Record af(int bedrag, Referentie referentie) {
        if(bedrag < 0){
            throw new IllegalArgumentException();
        }
        return verreken(-bedrag, referentie);
    }

    Record bij(int bedrag, Referentie referentie) {
        if(bedrag < 0){
            throw new IllegalArgumentException();
        }
        return verreken(bedrag, referentie);
    }
    
    Record verreken(int change, Referentie referentie){
        Record r = new Record(change, referentie);
        changes.add(r);
        total += change;
        return r;
    }

    int get() {
        return total;
    }

    @Override
    public String toString() {
        double centen = this.total;
        return "€"+centen/100;
    }
}
