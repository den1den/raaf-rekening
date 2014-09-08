/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package geld;


public class RekeningHouderContantI extends RekeningHouderI implements RekeningHouderContant{

    final private RekeningHouder contant;
    
    public RekeningHouderContantI(String naam) {
        super(naam);
        contant = new ContantRekening(naam);
    }

    @Override
    public void pin(Transactie t) {
        super.af(t);
        contant.bij(t.getOposite());
    }

    @Override
    public void stort(Transactie t) {
        super.bij(t);
        contant.bij(t.getOposite());
    }

    @Override
    public int getTotaal() {
        return saldo + contant.getSaldo();
    }

    @Override
    public RekeningHouder getContant() {
        return contant;
    }

    private class ContantRekening extends RekeningHouderI implements RekeningHouder {

        ContantRekening(String naam) {
            super(naam + " contant");
        }
    }
    
}
