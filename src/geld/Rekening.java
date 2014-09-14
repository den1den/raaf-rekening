/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package geld;

import data.types.HasNaam;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Dennis
 */
public abstract class Rekening implements HasNaam{

    int opRekening = 0;
    final private List<?> geschiedenis = new LinkedList<>();

    public List<?> getGeschiedenis() {
        return new ArrayList<>(geschiedenis);
    }
    
    public static class Virtueel extends Rekening{
        
        private final String naam;

        public Virtueel(String naam) {
            this.naam = naam;
        }

        @Override
        public String getNaam() {
            return naam;
        }
        
    }
}
