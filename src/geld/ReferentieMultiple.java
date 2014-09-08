/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package geld;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


public class ReferentieMultiple implements Referentie {
final ArrayList<Referentie> other;

    public ReferentieMultiple(Collection<? extends Referentie> c) {
        this.other = new ArrayList<>(c);
    }

    public ReferentieMultiple(ArrayList<Referentie> other) {
        if (other.size() < 2) {
            throw new IllegalArgumentException("Not multiple: " + other);
        }
        this.other = other;
    }
    @Override
    public String getRef() {
        
        String ref = getClass().getSimpleName() + ": ";
        Iterator<Referentie> it = other.iterator();
        ref += it.next();
        while (it.hasNext()) {
            ref += ", " + it.next().getRef();
        }
        return ref;
    }
    
}
