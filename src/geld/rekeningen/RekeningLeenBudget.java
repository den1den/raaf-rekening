/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.rekeningen;

import data.Incasso;
import geld.Referentie;

/**
 * Rekening met verschuldegingne, leningen, en RealMoney
 *
 * @author Dennis
 */
public class RekeningLeenBudget extends RekeningLeen{

    private final Som budget = new Budget();

    public RekeningLeenBudget(String naam) {
        this(naam, false);
    }

    public RekeningLeenBudget(String naam, boolean contant) {
        super(naam, contant);
    }

    public Som getBudget() {
        return budget;
    }
    
    protected void doBudgetBijDuo(RekeningLeenBudget bij, int bedrag, Event e){
        doBudgetBijSingle(bedrag, e);
        bij.doBudgetBijSingle(-bedrag, e);
    }
    
    protected void doBudgetBijSingle(int bedrag, Event e){
        budget.put(bedrag, e);
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
    public void besteedVia_(RekeningLeen via, Rekening bij, int bedrag, Referentie r) {
        String message = "{1} heeft iets gekocht bij "
                + "{2} voor {0}, {1} moet het dus not terug "
                + "krijgen van {0}";
        Event e = newE(via, bij, r, message);
        
        via.doRekeningBijDuo(bij, bedrag, e);
        doKrijgtNogVanDuo(via, -bedrag, e);
    }

    void doVerwachtKostenVanDuo(Incasso incasso, int bedrag, Event e) {
        new UnsupportedOperationException("Not supported yet.").printStackTrace(); //To change body of generated methods, choose Tools | Templates.
    }

    private class Budget extends Som {

        @Override
        public String beschrijving() {
            return "Budget van "+RekeningLeenBudget.this.getNaam();
        }
    }
}
