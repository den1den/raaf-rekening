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
public class TRecord {
    
    final Transactie transactie;
    final RekeningHouder van;
    final RekeningHouder naar;

    public TRecord(Transactie transactie, RekeningHouder van, RekeningHouder naar) {
        if(transactie == null || van == null){
            throw new IllegalArgumentException();
        }
        this.transactie = transactie;
        this.van = van;
        this.naar = naar;
    }
}
