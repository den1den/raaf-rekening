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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import tijd.Datum;
import util.Map2Ints;
import util.Map2Ints.Map2IntsAcending;

/**
 *
 * @author Dennis
 */
public class Event implements Comparable<Event>, HasDatum{

    private final HasNaam[] betreft;
    private final Referentie referentie;
    private final String message;
    private final Map2Ints<Som> indexesPerSom = new Map2IntsAcending<>(2);

    Event(String message, Referentie referentie, HasNaam... betreft) {
        this(betreft, referentie, message);
    }

    Event(HasNaam[] betreft, Referentie referentie, String message) {
        if (!message.contains("{") && betreft.length > 0) {
            throw new UnsupportedOperationException("Lazy");
        }
        if (referentie == null || betreft.length < 1) {
            throw new IllegalArgumentException();
        }
        for (HasNaam betreft1 : betreft) {
            if (betreft1 == null) {
                throw new IllegalArgumentException();
            }
        }
        this.betreft = betreft;
        this.referentie = referentie;
        this.message = message;
        
        Object[] arguments = new String[betreft.length];
        for (int i = 0; i < betreft.length; i++) {
            arguments[i] = betreft[i].getNaam();
        }
        this.messageFormatted = java.text.MessageFormat.format(message, arguments);
        if(messageFormatted.contains("{"))
            throw new Error("Wss niet genoeg argumenten ("+betreft.length+") voor: "+message);
    }
    
    private String messageFormatted;

    @Override
    public String toString() {
        String m = Arrays.deepToString(betreft);
        return messageFormatted + "[m:" + m + ", r:" + referentie.getRefString() + "]";
    }

    void addSum(Som s, int index) {
        indexesPerSom.put(s, index);
    }

    public Set<Som> getSums() {
        return indexesPerSom.keySet();
    }
    
    public Set<Map.Entry<Som, List<Integer>>> entrySet(){
        return indexesPerSom.entrySet();
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

    public String getMessage() {
        return messageFormatted;
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
