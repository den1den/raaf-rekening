/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import data.types.HasNaam;
import geld.RekeningHouderContantInterface;
import geld.RekeningHouderSimple;

/**
 *
 * @author Dennis
 */
public class Persoon extends RekeningHouderSimple implements HasNaam{

    protected Boolean kwijtschelden = null;

    public Persoon(String naam) {
        super(naam);
        if(naam.equalsIgnoreCase("rk")){
            throw new IllegalArgumentException();
        }
    }

    public boolean kwijtschelden() {
        if (kwijtschelden == null) {
            return false;
        } else {
            return kwijtschelden;
        }
    }

    public Boolean getKwijtschelden() {
        return kwijtschelden;
    }

    public void setKwijtschelden(Boolean kwijtschelden) {
        this.kwijtschelden = kwijtschelden;
    }
/*
    @Override
    public String toString() {
        return naam;
    }

    @Override
    public String getNaam() {
        return naam;
    }

    @Override
    public void moetBetalen(int bedrag, ReferentieInterface referentie) {
        if(naam.equalsIgnoreCase("dennis")){
            boolean breakpoint = true;
        }
        super.moetBetalen(bedrag, referentie); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void krijgtNog(int bedrag, ReferentieInterface referentie) {
        if(naam.equalsIgnoreCase("dennis")){
            boolean breakpoint = true;
        }
        super.krijgtNog(bedrag, referentie); //To change body of generated methods, choose Tools | Templates.
    }*/

    @Override
    public String toString() {
        return getNaam();
    }
}
