/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import geld.Rekening;
import geld.geldImpl.HasSchulden;
import java.util.Comparator;

/**
 *
 * @author Dennis
 */
public class SchuldenComparator implements Comparator<HasSchulden> {

    private final HasSchulden subject;

    public SchuldenComparator(HasSchulden subject) {
        if (subject == null) {
            throw new IllegalArgumentException();
        }
        this.subject = subject;
    }

    static public Comparator<HasSchulden> by(HasSchulden... voorRekening) {
        SchuldenComparator[] s = new SchuldenComparator[voorRekening.length];
        for (int i = 0; i < voorRekening.length; i++) {
            s[i] = new SchuldenComparator(voorRekening[i]);
        }
        return new Mutiple(s);
    }

    @Override
    public int compare(HasSchulden o1, HasSchulden o2) {
        return Integer.compare(o1.getKrijgtNog().From(subject), o2.getKrijgtNog().From(subject));
    }

    private static class ByMagnitude extends SchuldenComparator {

        public ByMagnitude(HasSchulden subject) {
            super(subject);
        }

        @Override
        public int compare(HasSchulden o1, HasSchulden o2) {
            return Integer.compareUnsigned(o1.getKrijgtNog().From(super.subject), o2.getKrijgtNog().From(super.subject));
        }
    }

    private static class Mutiple implements Comparator<HasSchulden> {

        Comparator<HasSchulden>[] multi;

        public Mutiple(Comparator<HasSchulden>[] multi) {
            if (multi == null || multi.length == 0) {
                throw new IllegalArgumentException();
            }
            this.multi = multi;
        }

        @Override
        public int compare(HasSchulden o1, HasSchulden o2) {
            for (int i = 0; i < multi.length; i++) {
                int comp = multi[i].compare(o1, o2);
                if (comp != 0) {
                    return comp;
                }
            }
            return Integer.compare(o1.getKrijgtNog().Total(), o2.getKrijgtNog().Total());
        }

    }
}
