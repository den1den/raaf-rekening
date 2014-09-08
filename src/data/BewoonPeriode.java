/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import data.BewoonPeriode.SubPeriode;
import geld.Referentie;
import java.util.Iterator;
import java.util.NoSuchElementException;
import tijd.Datum;
import tijd.Interval;
import tijd.Time;

/**
 *
 * @author Dennis
 */
public class BewoonPeriode implements Iterable<SubPeriode> {

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

    @Override
    public Iterator<SubPeriode> iterator() {
        return new Iterator<SubPeriode>() {
            Time begin = interval.getBegin();
            Time end = begin.nextMonth();

            @Override
            public boolean hasNext() {
                return end.before(interval.getEnd());
            }

            @Override
            public SubPeriode next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                SubPeriode sub = new SubPeriode(new Interval(begin, end));
                begin = end;
                end = end.nextMonth();
                return sub;
            }
        };
    }

    public class SubPeriode implements Referentie{

        final Interval subInterval;

        public SubPeriode(Interval subInterval) {
            this.subInterval = subInterval;
        }

        @Override
        public Time getTime() {
            return subInterval.getEnd();
        }

    }
}
