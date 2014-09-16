/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.types;

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
public interface HasDate {

    public Datum getDate();

    /**
     *
     * @param <C>
     * @param cs a (by comp) sorted list
     * @param d
     * @param comp
     * @return All C's on Date d from List cs, or null when there are none
     */
    public static <C extends HasDate, RL extends List<C> & RandomAccess> LinkedList<C> searchOn(
            RL cs,
            Datum d,
            Comparator<Datum> comp) {
        LinkedList<C> result = new LinkedList<>();
        Iterator<C> it = cs.iterator();
        C c;
        if (it.hasNext()) {
            do {
                c = it.next();
            } while (comp.compare(c.getDate(), d) < 0);
            while (comp.compare(c.getDate(), d) == 0) {
                result.add(c);
                c = it.next();
            }
        }
        try {
            if(result.size()>1)
            throw new Exception("Nog ff checken!");
        } catch (Exception ex) {
            Logger.getLogger(HasDate.class.getName()).log(Level.SEVERE, "TODO", ex);
        }
        return result;
    }
}
