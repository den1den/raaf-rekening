/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package geld.OLD;

import tijd.Time;


public class VirtueleRekeningMContant extends VirtueleRekening implements RekeningHouderContant{

    RekeningHouder contant;
    
    public VirtueleRekeningMContant(String naam) {
        super(naam);
        contant = new VirtueleRekeningContant();
    }

    @Override
    public Factuur betaalContant(RekeningHouder aan, int bedrag, ReferentieInterface referentie, Time datum) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getBalansContant() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private String getThisRekNaam(){
        return getNaam();
    }
    
    public class VirtueleRekeningContant extends RekeningHouder {

        @Override
        public String getNaam() {
            return getThisRekNaam();
        }
        
    }
    
}
