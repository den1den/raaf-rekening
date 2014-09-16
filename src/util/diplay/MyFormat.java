/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util.diplay;

import java.text.DecimalFormat;

/**
 *
 * @author Dennis
 */
public class MyFormat {
    static DecimalFormat formatS = new DecimalFormat("##.00");
    static DecimalFormat formatM = new DecimalFormat("#0.00");
    static DecimalFormat formatL = new DecimalFormat("00.00");

    public String toAbsMoney(int cents){
        double euro = cents;
        euro = Math.abs(euro)/100;
        
        return "€" + formatL.format(euro);
    }
    
    public String toMoney(int cents){
        if(cents < 0){
            return "-"+toAbsMoney(cents);
        }
        return toAbsMoney(cents);
    }
}
