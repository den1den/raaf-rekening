/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld;

/**
 *
 * @author Dennis
 */
interface RekeningI {

    void addSchuld(RekeningHouder aan, int bedrag, Referentie referentie);

    void payBack(RekeningHouder aan, int bedrag, Referentie referentie);

    int getSchuld(RekeningHouder rh);
}
