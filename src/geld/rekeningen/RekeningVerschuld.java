/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.rekeningen;

import geld.Referentie;

public class RekeningVerschuld extends Rekening {

    private final SomMap<RekeningVerschuld> krijgtNogVan;

    public RekeningVerschuld(String naam) {
        super(naam);
        krijgtNogVan = new SomMap<>(10, new VerschuldDigdParams());
    }

    /**
     * Degene moet meer terug gaan betalen aan deze rekening. En deze rekening
     * moet minder gaan betalen aan degene.
     *
     * @param van degene
     * @param bedrag
     * @param r
     */
    public void moetKrijgenVan(RekeningVerschuld van, int bedrag, Referentie r) {
        if (bedrag < 0) {
            throw new IllegalArgumentException();
        }

        String message = "{0} moet krijgen van {1}";

        Event e = newE(van, r, message);

        doMoetKrijgenVan(van, bedrag, e);
    }

    protected void doMoetKrijgenVan(RekeningVerschuld lr, int bedrag, Event e) {
        this.krijgtNogVan.add(lr, bedrag, e);
        lr.krijgtNogVan.add(this, -bedrag, e);
    }

    /**
     * Degene krijgt nog een bedrag van deze rekening. Deze rekening moet nog
     * een bedrag betalen aan diegene.
     *
     * @param aan degene
     * @param bedrag
     * @param r
     */
    public void moetBetalenAan(RekeningVerschuld aan, int bedrag, Referentie r) {
        if (bedrag < 0) {
            throw new IllegalArgumentException();
        }

        String message = getNaam() + " moet betalen aan " + aan.getNaam();

        Event e = newE(aan, r, message);

        aan.doMoetKrijgenVan(this, bedrag, e);
        //or
        //doMoetKrijgenVan(aan, -bedrag, e);
    }

    private class VerschuldDigdParams extends SomMap.SomMapParams<RekeningVerschuld> {

        @Override
        String getBeschrijvingTotaal() {
            return "Totaal verschuldigd aan " + RekeningVerschuld.this.getNaam();
        }

        @Override
        String getBeschrijvingSubSom(RekeningVerschuld van) {
            return van.getNaam() + " verschuldigd aan " + RekeningVerschuld.this.getNaam();
        }

    }
}
