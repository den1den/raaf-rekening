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
public interface RekeningHouderInterface extends HasNaam {

    @Override
    public String getNaam();

    void addSchuld(RekeningHouderInterface aan, int bedrag, Referentie referentie);

    void payBack(RekeningHouderInterface aan, int bedrag, Referentie referentie);

    int getSchuld(RekeningHouderInterface rh);

    void add(boolean af, RekeningHouderInterface aan, int bedrag, Referentie referentie);

    List<Transactie> getTransacties(RekeningHouderInterface r);
}
