/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import geld.RekeningHouder;
import geld.RekeningHouderInterface;
import java.util.Comparator;

/**
 *
 * @author Dennis
 */
public class SchuldenComparator implements Comparator<RekeningHouderInterface> {

    private final RekeningHouderInterface subject;

    public SchuldenComparator(RekeningHouderInterface subject) {
        if (subject == null) {
            throw new IllegalArgumentException();
        }
        this.subject = subject;
    }

    @Override
    public int compare(RekeningHouderInterface o1, RekeningHouderInterface o2) {
        return Integer.compare(o1.getSchuld(subject), o2.getSchuld(subject));
    }

    static public Comparator<RekeningHouderInterface> byMag(RekeningHouderInterface rhi) {
        return new ByMagnitude(rhi);
    }

    static public Comparator<RekeningHouderInterface> by(RekeningHouderInterface[] voorRekening) {
        SchuldenComparator[] s = new SchuldenComparator[voorRekening.length];
        for (int i = 0; i < voorRekening.length; i++) {
            s[i] = new SchuldenComparator(voorRekening[i]);
        }
        return new Mutiple(s);
    }

    private static class ByMagnitude extends SchuldenComparator {

        public ByMagnitude(RekeningHouderInterface subject) {
            super(subject);
        }

        @Override
        public int compare(RekeningHouderInterface o1, RekeningHouderInterface o2) {
            return Integer.compareUnsigned(o1.getSchuld(super.subject), o2.getSchuld(super.subject));
        }
    }

    private static class Mutiple implements Comparator<RekeningHouderInterface> {

        Comparator<RekeningHouderInterface>[] multi;

        public Mutiple(Comparator<RekeningHouderInterface>[] multi) {
            if (multi == null || multi.length == 0) {
                throw new IllegalArgumentException();
            }
            this.multi = multi;
        }

        @Override
        public int compare(RekeningHouderInterface o1, RekeningHouderInterface o2) {
            for (int i = 0; i < multi.length; i++) {
                int comp = multi[i].compare(o1, o2);
                if (comp != 0) {
                    return comp;
                }
            }
            return Integer.compare(o1.getSchuld(), o2.getSchuld());
        }

    }
}
