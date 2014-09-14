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
import geld.Rekening;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import util.MultiMap;

/**
 *
 * @author Dennis
 */
public class RaafRekening extends LeenRekening implements HasContant {

    final String naam;
    private final Sum kas = new Sum();
    private final Sum contant = new Sum();

    private final MultiMap<Object, Record> history = new MultiMap<>();

    public RaafRekening(final String naam) {
        this(naam, 10);
    }

    public RaafRekening(final String naam, final int capacity) {
        super();
        this.naam = naam;
    }

    public void schietVoor(Object bedrag, Referentie referentie) {
        throw new UnsupportedOperationException();
        //budget.af(bedrag); //bedrag > 0
        //van.putLening(this, bedrag, referentie);
    }

    public void betaal(Rekening van, int bedrag, Referentie referentie) {
        if (bedrag < 0) {
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
     * this krijgt nog wat geld van iemand budget++ this moetKrijgenVan
     * gebruiker
     *
     * @param bedrag >= 0
     * @param gebruiker degene die THIS wat veerschuldigd is
     * @param referentie
     */
    public void krijgtNog(int bedrag, LeenRekening gebruiker, Referentie referentie) {
        bij(bedrag, gebruiker, referentie);
        this.moetKrijgenVan(gebruiker, bedrag, referentie);
    }

    /**
     * Als iets voorgeschoten is. (budget--) en (voorschieter moetKrijgenVan
     * this)
     *
     * @param voorschieter
     * @param bedrag
     * @param referentie
     */
    public void betaaldDoor(LeenRekening voorschieter, int bedrag, Referentie referentie) {
        af(bedrag, referentie);
        voorschieter.moetKrijgenVan(this, bedrag, referentie);
    }

    public void af(int bedrag, Referentie referentie) {
        if (bedrag < 0) {
            throw new IllegalArgumentException();
        }
        kas.af(bedrag, referentie);
    }

    public void bij(int bedrag, LeenRekening r, Referentie referentie) {
        if (bedrag < 0) {
            throw new IllegalArgumentException();
        }
        kas.bij(bedrag, referentie);
    }

    public void plusContributie(LeenRekening van, int bedrag, Referentie referentie) {
        if (bedrag < 0) {
            throw new IllegalArgumentException();
        }
        //budget +
        kas.bij(bedrag, referentie);
        System.err.println("TODO2: r.af() zodat je ziet heveel er word betaald. Persoon>RaafRekening");
        //van komt in de plus te staan bij this
        van.getKrijgtNogVan().add(this, bedrag, referentie);
        //this moet nog betalen aan van
        this.getKrijgtNogVan().add(van, -bedrag, referentie);
    }

    /**
     * Iets gekocht bij Rekening r.
     * Er gaat iets uit de kas en bij degene waar het is gekocht gaat iets bij.
     * @param bij
     * @param bedrag
     * @param referentie 
     */
    public void gekocht(Rekening bij, int bedrag, Referentie referentie) {
        af(bedrag, referentie);
        System.err.println("TODO3: bij rekening 'bij' moet er geld bij");
        if(bij == null)throw new IllegalArgumentException();
    }
}
