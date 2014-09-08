/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import geld.ReferentieByToString;
import tijd.Interval;

/**
 *
 * @author Dennis
 */
public class BewoonPeriode extends ReferentieByToString{

    final Persoon persoon;
    final Interval interval;

    public BewoonPeriode(Persoon persoon, Interval interval) {
        this.persoon = persoon;
        this.interval = interval;
    }


    @Override
    public String toString() {
        return getClass().getSimpleName() + " van " + persoon + " is " + interval;
    }

    public Persoon getPersoon() {
        return persoon;
    }

    public Interval getInterval() {
        return interval;
    }

}
