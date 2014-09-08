/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.OLD;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Dennis
 */
public class RekeningHouderSimple {

    private final List<Betaling> afschriften;
    private int balans = 0;

    public RekeningHouderSimple() {
        this.afschriften = new LinkedList<>();
    }

    public void moetBetalen(int bedrag, ReferentieInterface referentie) {
        if (bedrag < 0) {
            throw new IllegalArgumentException();
        }
        Betaling betaling = new Betaling(referentie, bedrag, true);
        balans -= bedrag;
        afschriften.add(betaling);
    }

    public void krijgtNog(int bedrag, ReferentieInterface referentie) {
        if (bedrag < 0) {
            throw new IllegalArgumentException();
        }
        Betaling betaling = new Betaling(referentie, bedrag, false);
        balans += bedrag;
        afschriften.add(betaling);
    }

    public int getBalans() {
        return balans;
    }

    public List<Betaling> getBetalingen() {
        return afschriften;
    }
}
