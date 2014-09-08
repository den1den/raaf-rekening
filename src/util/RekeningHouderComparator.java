/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import geld.RekeningHouder;
import java.util.Comparator;

/**
 *
 * @author Dennis
 */
public class RekeningHouderComparator implements Comparator<RekeningHouder>{

    @Override
    public int compare(RekeningHouder o1, RekeningHouder o2) {
        return Integer.compare(o1.getSaldo(), o2.getSaldo());
    }
    
}
