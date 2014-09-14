/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package geld.geldImpl;

import data.Kookdag;
import data.types.HasNaam;
import geld.Referentie;

/**
 *
 * @author Dennis
 */
public interface HasSchulden extends HasNaam{
    SumMap getKrijgtNogVan();

    @Override
    public String getNaam();

    void moetBetalenAan(HasSchulden hs, int bedrag, Referentie r);

    void moetKrijgenVan(HasSchulden hs, int bedrag, Referentie r);
    
    void addHistory(RecordComplete recordComplete);
}
