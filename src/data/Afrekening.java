/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import data.types.HasDatum;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import tijd.Datum;

/**
 *
 * @author Dennis
 */
public class Afrekening implements HasDatum{
    private final Persoon persoon;
    private Datum datum; //als deze null is dan moet de datum van het einde van de periode gepakt worden

    public Afrekening(Persoon p, Datum d) {
        if(p == null)
            throw new IllegalArgumentException();
        this.persoon = p;
        this.datum = d;
    }

    public Persoon getPersoon() {
        return persoon;
    }

    @Override
    public Datum getDatum() {
        return datum;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+" van "+persoon+" op "+datum;
    }
    
    static public void match(Collection<Afrekening> afrekenings, Collection<BewoonPeriode> bewoonPeriode){
        for (Afrekening afrekening : afrekenings) {
            if(afrekening.datum == null){
                Persoon p = afrekening.persoon;
                List<BewoonPeriode> list = new LinkedList<>();
                for (BewoonPeriode per : bewoonPeriode) {
                    if(per.getPersoon().equals(p)){
                        list.add(per);
                    }
                }
                if(list.size() == 1){
                    afrekening.datum = list.get(0).getInterval().getEind();
                }else{
                    throw new IllegalStateException();
                }
            }
        }
    }
}
