/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld;

import java.util.List;

/**
 *
 * @author Dennis
 */
interface RekeningI {

    void addSchuld(Rekening aan, int bedrag, Referentie referentie);

    void payBack(Rekening aan, int bedrag, Referentie referentie);

    int getSchuld(Rekening rh);

    void add(boolean af, Rekening aan, int bedrag, Referentie referentie);

    List<Transactie> getTransacties(Rekening r);
    
}
