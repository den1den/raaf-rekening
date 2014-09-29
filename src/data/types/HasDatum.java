/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.types;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.RandomAccess;
import java.util.logging.Level;
import java.util.logging.Logger;
import tijd.Datum;

/**
 *
 * @author Dennis
 */
public interface HasDatum {

    public Datum getDatum();

    public static <HD extends HasDatum> LinkedList<HD> search(Collection<HD> haystack, Datum datum, Comparator<Datum> comparator) {
        LinkedList<HD> results = new LinkedList<>();
        for (HD haysta : haystack) {
            Datum haystaDatum = haysta.getDatum();
            if(comparator.compare(datum, haystaDatum) == 0){
                results.add(haysta);
            }
        }
        return results;
    }

    /**
     *
     * @param <C>
     * @param <RL>
     * @param cs a (by comp) sorted list
     * @param d
     * @param comp
     * @return All C's on Date d from List cs, or null when there are none
     */
    public static <C extends HasDatum, RL extends Collection<C>> LinkedList<C> searchOn(
            RL cs,
            Datum d,
            Comparator<Datum> comp) {
        LinkedList<C> result = new LinkedList<>();
        Iterator<C> it = cs.iterator();
        C c;
        if (it.hasNext()) {
            do {
                c = it.next();
            } while (comp.compare(c.getDatum(), d) < 0);
            while (comp.compare(c.getDatum(), d) == 0) {
                result.add(c);
                c = it.next();
            }
        }
        try {
            if (result.size() > 1) {
                throw new Exception("Nog ff checken!");
            }
        } catch (Exception ex) {
            Logger.getLogger(HasDatum.class.getName()).log(Level.SEVERE, "TODO", ex);
        }
        return result;
    }
    
    public static <HD extends HasDatum> void sort(List<HD> list, final Comparator<Datum> comparator){
        Collections.sort(list, (HD o1, HD o2) -> comparator.compare(o1.getDatum(), o2.getDatum()));
    }
}
