/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package geld.OLD;


public class ReferentieSimpel implements ReferentieInterface {
    public static ReferentieInterface CONTANT = new ReferentieSimpel("Contant");
    final private String ref;

    public ReferentieSimpel(String ref) {
        this.ref = ref;
    }

    @Override
    public String getRef() {
        return ref;
    }
}
