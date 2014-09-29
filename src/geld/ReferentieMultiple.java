/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld;

import data.Afschrift;
import data.ContantRecord;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import tijd.Datum;

/**
 *
 * @author Dennis
 */
public class ReferentieMultiple extends Referentie {

    List<Referentie> refs;

    public ReferentieMultiple(List<Referentie> refs) {
        if (refs.size() <= 1) {
            throw new IllegalArgumentException();
        }
        this.refs = refs;
    }

    public ReferentieMultiple(Referentie... rs) {
        this(Arrays.asList(rs));
    }

    private Datum d = null;
    private String s = null;

    @Override
    public Datum getDatum() {
        if (d == null) {
            List<Datum> ds = new ArrayList<>(refs.size());
            for (Referentie ref : refs) {
                Datum datm = ref.getDatum();
                if (datm != null) {
                    if(!ds.contains(datm)){
                        ds.add(datm);
                    }
                }
            }
            if (ds.size() != 1) {
                throw new IllegalStateException();
            }
            d = ds.get(0);
        }
        return d;
    }

    @Override
    public String toString() {
        if (s == null) {
            Iterator<Referentie> it = refs.iterator();
            s = "[" + it.next().getRefString() + "]";
            while (it.hasNext()) {
                s += ", [" + it.next().getRefString() + "]";
            }
        }
        return s;
    }

}
