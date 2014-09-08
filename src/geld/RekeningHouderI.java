/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld;

import data.Bonnetje;
import tijd.Datum;


public class RekeningHouderI extends RekeningI implements RekeningHouder {

    final String naam;

    public RekeningHouderI(String naam) {
        if (naam == null || naam.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.naam = naam;
    }

    @Override
    public String getNaam() {
        return naam;
    }

    @Override
    public TRecord moetBetalenAan(Bonnetje bonnetje) {
        return moetBetalen(bonnetje.getPersoon(), bonnetje);
    }

    @Override
    public TRecord moetBetalen(RekeningHouder rekeningHouder, Bonnetje bonnetje) {
        return moetBetalen(rekeningHouder, bonnetje.getBedrag(), bonnetje.getDate(), bonnetje);
    }

    @Override
    public TRecord moetBetalen(RekeningHouder rekeningHouder, int bedrag, Datum date, Referentie referentie) {
        Transactie moetB = new Transactie(date, bedrag, true, referentie);
        TRecord tr = new TRecord(moetB, this, rekeningHouder);
        
        this.putSchuld(rekeningHouder, moetB);
        rekeningHouder.putSchuld(this, moetB.getOposite());
        
        return tr;
    }

    @Override
    public TRecord krijgtVan(RekeningHouder rekeningHouder, int bedrag, Datum date, Referentie referentie) {
        return rekeningHouder.moetBetalen(this, bedrag, date, referentie);
    }

    @Override
    public TRecord betaald(RekeningHouder naar, int bedrag, Datum datum, Referentie referentie){
        Transactie betaal = new Transactie(datum, bedrag, false, referentie);
        TRecord tr = new TRecord(betaal, this, naar);
        
        this.bij(betaal);
        naar.af(betaal.getOposite());
        
        return tr;
    }
}
