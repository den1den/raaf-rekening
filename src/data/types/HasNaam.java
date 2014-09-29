/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package data.types;

/**
 *
 * @author Dennis
 */
public class HasNaam{
    private final String naam;

    public HasNaam(String naam) {
        if(naam == null || naam.isEmpty())
            throw new IllegalArgumentException();
        this.naam = naam;
    }

    public String getNaam() {
        return naam;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+" "+getNaam();
    }
    
}
