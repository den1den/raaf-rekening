/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package geld.geldImpl;

/**
 *
 * @author Dennis
 */
interface HasContant {
    default void doContant(){
        throw new UnsupportedOperationException();
    }
}