/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package geld.two;

import geld.*;
import tijd.Time;

/**
 *
 * @author Dennis
 */
public interface Referentie {
    public default String getRef(){
        return toString();
    }
    public Time getTime();
}
