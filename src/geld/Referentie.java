/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package geld;

/**
 *
 * @author Dennis
 */
public interface Referentie {
    default String getRefString(){
        return toString();
    }
}
