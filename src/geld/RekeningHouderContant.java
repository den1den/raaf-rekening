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
    public int getSaldo() {
        return getRekeningSaldo() + getContantSaldo();
    }

    @Override
    public int getSaldo(RekeningHouderInterface aan) {
        return getRekeningSaldo(aan) + getContantSaldo(aan);
    }

    @Override
    public int getRekeningSaldo(RekeningHouderInterface rh) {
        return super.getSaldo(rh);
    }

    @Override
    public int getRekeningSaldo() {
        return super.getSaldo();
    }

    @Override
    public int getContantSaldo(RekeningHouderInterface rh) {
        return contant.getSaldo(rh);
    }

    @Override
    public int getContantSaldo() {
        return contant.getSaldo();
    }


    private class ContantRekening extends RekeningHouder {

        @Override
        public String getNaam() {
            return RekeningHouderContant.this.naam + " contant";
        }
    }
    
}
