/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.rekeningen;

import data.Bonnetje;
import geld.Referentie;

/**
 * Rekening met verschuldegingne, leningen, en RealMoney
 *
 * @author Dennis
 */
public class RekeningLeenBudget extends RekeningVLeen {

    private final Som contant = new ContantSom();

    public RekeningLeenBudget(String naam) {
        super(naam);
    }

    @Override
    public Som getContant() {
        return contant;
    }

    /**
     * Deze rekening besteed een bedrag aan iets bij een bepaalde rekening, maar
     * dit werd voorgeschoten door via.
     *
     * @param via
     * @param bij
     * @param bedrag
     * @param r
     */
    public void besteedVia(RekeningVLeen via, Rekening bij, int bedrag, Referentie r) {
        String message = "{1} heeft iets gekocht bij "
                + "{2} voor {0}, {1} moet het dus not terug "
                + "krijgen van {0}";
        Event e = newE(via, bij, r, message);
        doBesteedVia(via, bij, bedrag, e);
    }

    /**
     * see {@link #besteedVia(geld.LeenRekening, geld.Rekening, int, geld.Referentie)
     * besteedVia(b.getPersoon(), b.getWinkel(), b.getBedrag(), b);}
     *
     * @param b
     */
    public void besteedVia(Bonnetje b) {
        besteedVia(b.getPersoon(), b.getWinkel(), b.getBedrag(), b);
        
    }

    protected void doBesteedVia(RekeningVLeen via, Rekening bij, int bedrag, Event e) {
        via.doBesteedDirect(bij, bedrag, e);
        doMoetKrijgenVan(via, -bedrag, e);
    }

    public void betaaldUit(RekeningVLeen van, int bedrag, Referentie referentie) {
        throw new UnsupportedOperationException("Not supported yet. zie excel"); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Som getBankRekening() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    

    private class ContantSom extends Som {

        @Override
        public String beschrijving() {
            return "Contant geld van " + RekeningLeenBudget.this.getNaam();
        }

    }
}
