/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import geld.RekeningHouder;
import java.util.Comparator;

/**
 *
 * @author Dennis
 */
public class SchuldenComparator implements Comparator<RekeningHouder> {

    private final RekeningHouder subject;

    public SchuldenComparator(RekeningHouder subject) {
        if (subject == null) {
            throw new IllegalArgumentException();
        }
        this.subject = subject;
    }

    @Override
    public int compare(RekeningHouder o1, RekeningHouder o2) {
        return Integer.compare(o1.getSchuld(subject), o2.getSchuld(subject));
    }

    public static class ByMagnitude extends SchuldenComparator {

        public ByMagnitude(RekeningHouder subject) {
            super(subject);
        }

        @Override
        public int compare(RekeningHouder o1, RekeningHouder o2) {
            return Integer.compareUnsigned(o1.getSchuld(super.subject), o2.getSchuld(super.subject));
        }

    }
}
