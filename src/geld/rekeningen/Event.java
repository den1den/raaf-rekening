/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.rekeningen;

import data.types.HasDatum;
import data.types.HasNaam;
import geld.Referentie;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import tijd.Datum;
import util.Map2Int;

/**
 *
 * @author Dennis
 */
public class Event implements Comparable<Event>, HasDatum{

    private final HasNaam[] betreft;
    private final Referentie referentie;
    private final String message;
    private final Map2Int<Som> indexes;

    Event(String message, Referentie referentie, HasNaam... betreft) {
        this(betreft, referentie, message);
    }

    Event(HasNaam[] betreft, Referentie referentie, String message) {
        if (!message.contains("{") && betreft.length > 0) {
            throw new UnsupportedOperationException("Lazy");
        }
        if (referentie == null) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < betreft.length; i++) {
            if(betreft[i] == null)
                throw new IllegalArgumentException();
        }
        Object[] arguments = betreft;
        this.betreft = betreft;
        this.referentie = referentie;
        this.message = java.text.MessageFormat.format(message, arguments);
        this.indexes = new Map2Int<>(2, -1);
    }

    @Override
    public String toString() {
        String m = Arrays.deepToString(betreft);
        return message + "[m:" + m + ", r:" + referentie.getRefString() + "]";
    }

    void addSum(Som s, int index) {
        indexes.put(s, index);
    }

    public Set<Som> getSums() {
        return indexes.keySet();
    }

    @Deprecated
    public List<Integer> getChanges(List<Som> sums) {
        List<Integer> changes = new ArrayList<>(sums.size());
        for (Som sum : sums) {
            int index = indexes.get(sum);
            if (index != -1) {
                //changes.add(sum.get(index));
            } else {
                changes.add(0);
            }
        }
        return changes;
    }

    public Set<Map.Entry<Som, Integer>> entrySetIndex() {
        return indexes.entrySet();
    }

    @Deprecated
    public Set<Map.Entry<Som, Integer>> entrySet() {
        Set<Map.Entry<Som, Integer>> entries = entrySetIndex();
        for (Map.Entry<Som, Integer> entry : entries) {
            //entry.setValue(entry.getKey().get(entry.getValue()));
        }
        return entries;
    }

    @Override
    public int compareTo(Event o) {
        return referentie.getDatum().compareTo(o.referentie.getDatum());
    }

    @Override
    public Datum getDatum() {
        return referentie.getDatum();
    }

    public Referentie getReferentie() {
        return referentie;
    }
    
    public String[] showBetrokken(){
        String[] ses = new String[betreft.length];
        for (int i = 0; i < ses.length; i++) {
            ses[i] = betreft[i].getNaam();
        }
        return ses;
    }
    
    public String[] showBetrokkenSmall(){
        String[] ses = new String[betreft.length];
        for (int i = 0; i < ses.length; i++) {
            String betr = betreft[i].getNaam();
            if(betr.length() > 20){
                ses[i] = betr.substring(0, 20);
            }else
            ses[i] = betr;
        }
        return ses;
    }
}
