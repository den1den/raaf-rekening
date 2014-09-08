/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package data.types;

import data.Bonnetje;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dennis
 */
public interface HasBedrag {

    public static List<Bonnetje> searchOn(List<Bonnetje> bonnetjes, int bedrag){
        List<Bonnetje> result = new ArrayList<>(1);
        for (Bonnetje bonnetje : bonnetjes) {
            if(bonnetje.getBedrag() == bedrag){
                result.add(bonnetje);
            }
        }
        return result;
    }
    public int getBedrag();
}
