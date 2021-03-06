/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import geld.ReferentieByToString;
import java.util.Map;

/**
 *
 * @author Dennis
 */
public class Kookdag extends ReferentieByToString{
    public final int id;
    private final int totaalBedrag;
    private final Persoon kok;
    private final Map<Persoon, Integer> eters;
    public Kookdag(int ID, int bedrag, Persoon kok, Map<Persoon, Integer> eters) {
        this.id = ID;
        this.totaalBedrag = bedrag;
        this.kok = kok;
        this.eters = eters;
        if(eters.get(kok) == null){
            throw new IllegalArgumentException("Kok at niet mee ?!");
        }
    }

    public int getBedrag() {
        return totaalBedrag;
    }

    public Persoon getKok() {
        return kok;
    }

    public Map<Persoon, Integer> getMeeters() {
        return eters;
    }

    @Override
    public String getRef() {
        return "Kookdag ("+id+") van "+getBedrag()+"ct met "+getTotaalEters()+" man";
    }
    
    /**
     * geen kwijtschelden
     * @return 
     */
    public int getTotaalEters(){
        int tot = 0;
        for (Integer p : eters.values()) {
            tot += p;
        }
        return tot;
    }
}
