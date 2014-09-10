/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package geld;

import data.Afschrift;
import data.Incasso;


public class RekeningHouderContant extends RekeningHouderSimple implements RekeningHouderContantInterface{

    public RekeningHouderContant(String naam) {
        super(naam);
    }

    private final RekeningHouder contant = new ContantRekening();
    
    @Override
    public RekeningHouder getContant() {
        return contant;
    }

    @Override
    public void pin(int bedrag) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void stort(int bedrag) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getTotaalSchuld(RekeningHouder rh) {
        return getSchuld(rh) + contant.getSchuld(rh);
    }


    private class ContantRekening extends RekeningHouder {

        @Override
        public String getNaam() {
            return RekeningHouderContant.this.naam + " contant";
        }
    }
    
}
