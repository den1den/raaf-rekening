/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.rekeningen;

import geld.Referentie;


public class _RekeningVLeen {

    private final SomMap<_RekeningVLeen> heeftGeleend;
    
    public _RekeningVLeen(String naam) {
        super(naam);
        heeftGeleend = new SomMap<>(10, new LeenParams());
    }
    
    /**
     * @param aan
     * @param bedrag
     * @param referentie 
     */
    public void leentUit(_RekeningVLeen aan, int bedrag, Referentie referentie){
        if(bedrag < 0)
            throw new IllegalArgumentException();
        String message = "{0} leent uit aan {1}";
        Event e = newE(aan, referentie, message);
        doLeenUit(aan, bedrag, e);
    }
    
    protected void doLeenUit(_RekeningVLeen rvl, int bedrag, Event e){
        this.heeftGeleend.add(rvl, bedrag, e);
        this.doMoetKrijgenVan(rvl, bedrag, e);
        this.doVerreken(-bedrag, e);
        rvl.heeftGeleend.add(this, -bedrag, e);
        rvl.doMoetKrijgenVan(this, -bedrag, e);
        rvl.doVerreken(bedrag, e);
    }
    /**
     * Krijgt geld terug
     * @param van
     * @param bedrag
     * @param referentie 
     */
    public void krijgtTerug(_RekeningVLeen van, int bedrag, Referentie referentie){
        String message = "{0} krijgt terug van {1}";
        Event e = newE(van, referentie, message);
        doLeenUit(van, -bedrag, e);
    }
    
    private class LeenParams extends SomMap.SomMapParams<_RekeningVLeen>{

        @Override
        String getBeschrijvingTotaal() {
            return "Totaal verschuldigd aan " + _RekeningVLeen.this.getNaam();
        }

        @Override
        String getBeschrijvingSubSom(_RekeningVLeen van) {
            return van.getNaam() + " verschuldigd aan " + _RekeningVLeen.this.getNaam();
        }
    }
    
}
