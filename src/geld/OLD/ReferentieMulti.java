/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.OLD;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReferentieMulti implements ReferentieInterface {

    final ArrayList<ReferentieInterface> referentieInterfaces;

    public ReferentieMulti(ArrayList<ReferentieInterface> referentieInterfaces) {
        if (referentieInterfaces.size() < 2) {
            throw new IllegalArgumentException("Not multiple: " + referentieInterfaces);
        }
        this.referentieInterfaces = referentieInterfaces;
    }

    public ReferentieMulti(List<? extends ReferentieInterface> referentieInterfaces) {
        this(new ArrayList<>(referentieInterfaces));
    }

    @Override
    public String getRef() {
        String ref = getClass().getSimpleName() + ": ";
        Iterator<ReferentieInterface> it = referentieInterfaces.iterator();
        ref += it.next();
        while (it.hasNext()) {
            ref += ", " + it.next().getRef();
        }
        return ref;
    }

}
