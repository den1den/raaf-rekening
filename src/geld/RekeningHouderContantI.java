/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package geld;


public class RekeningHouderContantI extends RekeningHouderI implements RekeningHouderContant{

    final private ContantRekening contant;
    
    public RekeningHouderContantI(String naam) {
        super(naam);
        contant = new ContantRekening();
    }

    @Override
    public RekeningHouder getContant() {
        return contant;
    }

    @Override
    public String getNaam() {
        return super.getNaam(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void pin(int bedrag) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void stort(int bedrag) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private class ContantRekening extends RekeningHouderI implements RekeningHouder {
    }
    
}
