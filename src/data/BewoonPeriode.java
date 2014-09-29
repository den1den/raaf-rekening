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

    final private Persoon persoon;
    final private IntervalDatums interval;
    final private int bedrag;

    public BewoonPeriode(Persoon persoon, IntervalDatums interval, int bedrag) {
        this.persoon = persoon;
        if(interval.getBegin() == Datum.begin){
            throw new IllegalArgumentException("Kan niet zo");
        }
        if(interval.isNegative()){
            throw new IllegalArgumentException("Bewoon periode mag niet negatief zijn");
        }
        this.interval = interval;
        this.bedrag = bedrag;
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

    public int getBedrag() {
        return bedrag;
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

    public class SubPeriode extends Referentie {

        final IntervalDatums subInterval;

        public SubPeriode(IntervalDatums subInterval) {
            this.subInterval = subInterval;
        }

        public Datum getEind() {
            return subInterval.getEind();
        }

        @Override
        public String toString() {
            return subInterval.toString();
        }

        @Override
        public Datum getDatum() {
            return subInterval.getBegin();
        }

    }
}
