/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package geld;

import data.types.HasNaam;
import java.util.List;

/**
 *
 * @author Dennis
 */
public interface HasSchulden extends HasNaam{
    //SumMap getKrijgtNogVan();
    
    int krijgtNogVan(HasSchulden iemand);
    int krijgtNogVan();

    @Override
    public String getNaam();

    void moetBetalenAan(LeenRekening lr, int bedrag, Referentie r);

    void moetKrijgenVan(LeenRekening lr, int bedrag, Referentie r);

    List<Event> getHistory();
}
