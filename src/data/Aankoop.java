/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package data;

import data.types.HasNaam;

/**
 *
 * @author Dennis
 */
public class Aankoop implements HasNaam{

    String naam;

    @Override
    public String getNaam() {
        return naam;
    }
}
