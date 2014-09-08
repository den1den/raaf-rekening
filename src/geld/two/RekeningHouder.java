/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.two;

import data.types.HasNaam;
import java.util.List;

/**
 *
 * @author Dennis
 */
public interface RekeningHouder extends HasNaam {

    @Override
    public String getNaam();

    void addSchuld(RekeningHouder aan, int bedrag, Referentie referentie);

    void payBack(RekeningHouder aan, int bedrag, Referentie referentie);

    int getSchuld(RekeningHouder rh);

    void add(boolean af, RekeningHouder aan, int bedrag, Referentie referentie);

    List<Transactie> getTransacties(RekeningHouder r);
}
