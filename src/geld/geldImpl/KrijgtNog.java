/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.geldImpl;

import geld.Referentie;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dennis
 */
public class KrijgtNog {

    private final Map<HasSchulden, Sum> schulden = new HashMap<>(10);

    public void Add(HasSchulden bij, int bedrag, Referentie referentie) {
        Sum s = schulden.get(bij);
        if(s == null){
            s = new Sum(bedrag, referentie);
            schulden.put(bij, s);
        }else{
            s.verreken(bedrag, referentie);
        }
    }

    public int From(HasSchulden tov) {
        Sum l = schulden.get(tov);
        if (l == null) {
            return 0;
        } else {
            return l.get();
        }
    }

    public int Total() {
        int totaal = 0;
        for (Map.Entry<HasSchulden, Sum> s : schulden.entrySet()) {
            totaal += s.getValue().get();
        }
        return totaal;
    }
    static DecimalFormat formatS = new DecimalFormat("##.00");
    static DecimalFormat formatM = new DecimalFormat("#0.00");
    static DecimalFormat formatL = new DecimalFormat("00.00");
    public String showMoney(){
        double n = Total();
        n = n / 100;
        
        String s = formatS.format(n);
        String m = formatM.format(n);
        String l = formatL.format(n);
        
        if(s.length() >= l.length()){
            //nothing
        } else if (s.length() == m.length()){
            s = "0"+ s;
        }
        else {
            s = " -" + s;
        }
        return "€" + s;
    }
    public String show(){
        double n = Total();
        n /= 100;
        return format(n);
    }
    public String show(HasSchulden tov){
        double n = From(tov);
        n /= 100;
        return format(n);
    }
    
    static String format(double n){
        String s = "€" + formatL.format(Math.abs(n));
        if(n < 0){
            s = "-" + s;
        }
        return s;
    }
/*
    public String showMoneyFrom(HasSchulden tov) {
        int l = From(tov);
        if(l == 0){
            return "Geen";
        }else if (l > 0){
            return showMoney()+"  to "+tov.getNaam();
        }else{
            return showMoney()+" from "+tov.getNaam();
        }
    }

    public String showLening() {
        int l = Total();
        if(l == 0){
            return "Geen";
        }else if (l > 0){
            return showMoney()+" plus";
        }else{
            return showMoney()+" min";
        }
    }*/
}
