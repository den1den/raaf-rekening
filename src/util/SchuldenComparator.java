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

    public static class ByMagnitude extends SchuldenComparator {

        public ByMagnitude(RekeningHouderInterface subject) {
            super(subject);
        }

        @Override
        public int compare(RekeningHouderInterface o1, RekeningHouderInterface o2) {
            return Integer.compareUnsigned(o1.getSchuld(super.subject), o2.getSchuld(super.subject));
        }

    }
}
