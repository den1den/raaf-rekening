/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package geld.OLD;

import geld.rekeningen.Event;
import data.types.HasNaam;
import geld.Referentie;
import java.util.List;

/**
 *
 * @author Dennis
 */
public interface HasSchulden{
    //SumMap getKrijgtNogVan();
    
    int getKrijgtNogVan(HasSchulden iemand);
    int getKrijgtNogVan();

    public String getNaam();

    void moetBetalenAan(LeenRekening lr, int bedrag, Referentie r);

    void moetKrijgenVan(LeenRekening lr, int bedrag, Referentie r);

    List<Event> getHistory();
}
