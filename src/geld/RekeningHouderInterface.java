/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld;

import data.Afschrift;
import data.types.HasNaam;
import java.util.List;

/**
 *
 * @author Dennis
 */
public interface RekeningHouderInterface extends HasNaam {

    @Override
    public String getNaam();

    void addSchuld(RekeningHouderInterface aan, int bedrag, Referentie referentie);
    void payBack(RekeningHouderInterface aan, int bedrag, Referentie referentie);

    int getSchuld(RekeningHouderInterface rh);

    void add(boolean af, RekeningHouderInterface aan, int bedrag, Referentie referentie);

    /**
     * 
     * @param r
     * @return can be null
     */
    List<Transactie> getTransactiesRef(RekeningHouderInterface r);
    
    /**
     * 
     * @param r
     * @return != null
     */
    List<Transactie> getTransactiesCopy(RekeningHouderInterface r);
    
    List<TransactiesRecord> getAllTransacties();
    List<TransactiesRecord> getAllTransacties(RekeningHouderInterface i);

    void verwerk(RekeningHouder AfsVan, Afschrift afschrift);
}
