/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package data;

import java.util.Collection;

/**
 *
 * @author Dennis
 */
public class SchuldDeler{
    Persoon schuldDeler;
    Collection<Persoon> kwijtgescholden;

    public SchuldDeler(Persoon schuldDeler, Collection<Persoon> kwijtgescholden) {
        this.schuldDeler = schuldDeler;
        this.kwijtgescholden = kwijtgescholden;
    }
    
}
