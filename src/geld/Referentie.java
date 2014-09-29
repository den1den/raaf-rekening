/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package geld;

import tijd.Datum;

/**
 *
 * @author Dennis
 */
public abstract class Referentie {
    public String getRefString(){
        return toString();
    }
    /**
     * De tijd van de referentie.
     * @return de datum en null als deze er niet is.
     */
    public abstract Datum getDatum();

    @Override
    abstract public String toString();
}
