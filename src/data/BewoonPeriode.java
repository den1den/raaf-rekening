/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import geld.Referentie;
import java.util.Iterator;
import tijd.Datum;
import tijd.IntervalDatums;

/**
 *
 * @author Dennis
 */
public class BewoonPeriode implements Iterable<BewoonPeriode.SubPeriode> {

    final Persoon persoon;
    final IntervalDatums interval;

    public BewoonPeriode(Persoon persoon, IntervalDatums interval) {
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

    public IntervalDatums getInterval() {
        return interval;
    }

    @Override
    public Iterator<BewoonPeriode.SubPeriode> iterator() {
        return new SubPerIt();
    }

    private class SubPerIt implements Iterator<BewoonPeriode.SubPeriode> {

        Iterator<IntervalDatums> datumIt = interval.perVolMaand();

        @Override
        public boolean hasNext() {
            return datumIt.hasNext();
        }

        @Override
        public BewoonPeriode.SubPeriode next() {
            return new BewoonPeriode.SubPeriode(datumIt.next());
        }

    }

    public class SubPeriode implements Referentie {

        final IntervalDatums subInterval;

        public SubPeriode(IntervalDatums subInterval) {
            this.subInterval = subInterval;
        }

        public Datum getEind() {
            return subInterval.getEind();
        }

    }
}
