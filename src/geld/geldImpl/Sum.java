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
    private final List<Integer> diffs;

    public Sum() {
        this.total = 0;
        this.diffs = new LinkedList<>();
    }

    public Sum(int bedrag) {
        this.total = bedrag;
        this.diffs = new LinkedList<>();
        this.diffs.add(bedrag);
    }
    
    int af(int bedrag) {
        if(bedrag < 0){
            throw new IllegalArgumentException();
        }
        return verreken(-bedrag);
    }

    int bij(int bedrag) {
        if(bedrag < 0){
            throw new IllegalArgumentException();
        }
        return verreken(bedrag);
    }
    
    /**
     * add and record the change.
     * @param change the amount the sum should change
     * @return the index of the diff
     */
    int verreken(int change){
        diffs.add(change);
        total += change;
        return diffs.size() - 1;
    }

    int get() {
        return total;
    }
    
    int get(int index){
        return diffs.get(index);
    }

    @Override
    public String toString() {
        double centen = this.total;
        return "€"+centen/100;
    }
}
