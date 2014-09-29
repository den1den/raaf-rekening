/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.types;

import data.Bonnetje;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Dennis
 */
public interface HasBedrag {

    public static <HB extends HasBedrag> LinkedList<HB> search(Collection<HB> haystack, int bedrag) {
        LinkedList<HB> result = new LinkedList<>();
        for (HB haysta : haystack) {
            int haystaBedrag = haysta.getBedrag();
            if (haystaBedrag == bedrag) {
                result.add(haysta);
            }
        }
        return result;
    }

    public static <HB extends HasBedrag> LinkedList<HB> search(Collection<HB> haystack, int bedrag, Comparator<Integer> comparator) {
        LinkedList<HB> result = new LinkedList<>();
        for (HB haysta : haystack) {
            int haystaBedrag = haysta.getBedrag();
            if (comparator.compare(bedrag, haystaBedrag) == 0) {
                result.add(haysta);
            }
        }
        return result;
    }

    public static List<Bonnetje> searchOn(List<Bonnetje> bonnetjes, int bedrag) {
        List<Bonnetje> result = new ArrayList<>(1);
        for (Bonnetje bonnetje : bonnetjes) {
            if (bonnetje.getBedrag() == bedrag) {
                result.add(bonnetje);
            }
        }
        return result;
    }

    public int getBedrag();
}
