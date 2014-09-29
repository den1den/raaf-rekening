/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tijd;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author Dennis
 */
public class IntervalDatums {

    static final DateFormat timeFormat = new SimpleDateFormat("dd-MM-yyyy H:ss:S");
    static public final IntervalDatums TOT_NU = vanaf(Datum.begin);

    public static IntervalDatums tot(Datum eind) {
        return new IntervalDatums(Datum.begin, eind);
    }

    public static IntervalDatums vanaf(Datum begin) {
        return new IntervalDatums(begin, Datum.nu);
    }

    private final Datum begin;
    private final Datum eind;

    public IntervalDatums(Datum begin, Datum eind) {
        this.begin = begin;
        this.eind = eind;
    }

    public Datum getBegin() {
        return begin;
    }

    public Datum getEind() {
        return eind;
    }

    @Override
    public String toString() {
        return "[" + begin + ", "
                + eind + "]";
    }

    /**
     * Of de datum in de periode valt (inclusief de randen van de periode)
     *
     * @param datum
     * @return true <==> in periode
     */
    public boolean isIn(Datum datum) {
        return datum.isIn(this);
    }

    /**
     *
     * @return iterator over hele maanden in deze interval
     */
    public Iterator<IntervalDatums> perVolMaand() {
        return new VolMaandIterator();
    }

    public boolean strictBefore(Datum until) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean isNegative() {
        if(begin == null || eind == null)
            throw new IllegalArgumentException();
        return !begin.before(eind);
    }

    private class VolMaandIterator implements Iterator<IntervalDatums> {

        private Datum begin = getBegin();
        private Datum eind = begin.getNextMonth();

        @Override
        public boolean hasNext() {
            return this.eind.before(getEind());
        }

        @Override
        public IntervalDatums next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            IntervalDatums next = new IntervalDatums(begin, eind);
            begin = eind;
            eind = eind.getNextMonth();
            return next;
        }
    }
}
