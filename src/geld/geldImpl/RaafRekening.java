/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.geldImpl;

import data.Afschrift;
import data.BewoonPeriode;
import data.Persoon;
import geld.Referentie;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import util.MultiMap;

/**
 *
 * @author Dennis
 */
public class RaafRekening extends LeenRekening implements HasContant{

    final String naam;
    private final Sum budget = new Sum();
    private final Sum contant = new Sum();

    private final MultiMap<Object, Record> history = new MultiMap<>();
    
    public RaafRekening(final String naam) {
        this(naam, 10);
    }

    public RaafRekening(final String naam, final int capacity) {
        super();
        this.naam = naam;
    }
    
    public void schietVoor(Object bedrag, Referentie referentie){
        throw new UnsupportedOperationException();
        //budget.af(bedrag); //bedrag > 0
        //van.putLening(this, bedrag, referentie);
    }
    
    public void betaal(Rekening van, int bedrag, Referentie referentie){
        if(bedrag < 0){
            throw new IllegalArgumentException();
        }
        throw new UnsupportedOperationException();
        //this.budget.bij(bedrag);
        //this.bedrag += bedrag;
    }
    @Override
    public String getNaam() {
        return naam;
    }

    /**
     * this krijgt nog wat geld van iemand
     * @param bedrag >= 0
     * @param van degene die THIS wat veerschuldigd is
     * @param referentie 
     */
    public void krijgtNog(int bedrag, LeenRekening van, Referentie referentie) {
        bij(bedrag, van, referentie);
        van.leentVan(this, bedrag, referentie);
        //getSchulden().putSchuld(persoon, bedrag, referentie);
    }
    
    public void moetNogBetalen(LeenRekening aan, int bedrag, Referentie referentie){
        if(bedrag < 0){
            throw new IllegalArgumentException();
        }
        aan.getKrijgtNog().Add(this, bedrag, referentie);
        this.getKrijgtNog().Add(aan, -bedrag, referentie);
    }
    
    public void betaald(int bedrag, Referentie referentie){
        if(bedrag < 0){
            throw new IllegalArgumentException();
        }
        budget.af(bedrag, referentie);
    }
    
    public void betaaldDoor(LeenRekening voorschieter, int bedrag, Referentie referentie){
        betaald(bedrag, referentie);
        moetNogBetalen(voorschieter, bedrag, referentie);
    }

    public void bij(int bedrag, LeenRekening r, Referentie referentie) {
        if(bedrag < 0){
            throw new IllegalArgumentException();
        }
        budget.bij(bedrag, referentie);
    }
    public void krijgt(int bedrag, LeenRekening r, Referentie referentie) {
        bij(bedrag, r, referentie);
        System.err.println("TODO: r.af() zodat je ziet heveel er word betaald. Persoon>RaafRekening");
    }
}
