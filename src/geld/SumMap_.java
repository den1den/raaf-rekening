/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld;

import geld.rekeningen.Event;
import data.types.HasNaam;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dennis
 */
abstract class SumMap_<K extends HasNaam> implements HasHistory {

    private final Map<K, Sum_> all;
    private final Sum_ total = new SumMapTotal();

    public SumMap_() {
        this(10);
    }

    public SumMap_(int initialCapacity) {
        all = new HashMap<>(initialCapacity);
    }

    

    @Override
    public List<Event> getHistory() {
        return total.getHistory();
    }

    public Sum_ getTotal() {
        return total;
    }

    /**
     *
     * @param foR
     * @return History or null when foR doesnt exicsts in this mapping
     */
    public List<Event> getHistory(K foR) {
        Sum_ s = all.get(foR);
        if (s == null) {
            return null;
        } else {
            return s.getHistory();
        }
    }

    public int get(K key) {
        Sum_ l = all.get(key);
        if (l == null) {
            return 0;
        } else {
            return l.get();
        }
    }

    public int get() {
        int totaal = total.get();
        if (totaal != get_OLD()) {
            throw new AssertionError();
        }
        return totaal;
    }

    public int get_OLD() {
        int totaal = 0;
        for (Map.Entry<K, Sum_> s : all.entrySet()) {
            totaal += s.getValue().get();
        }
        return totaal;
    }

    abstract String getNaam();

    private class MapSum extends Sum_ {

        private final K van;

        public MapSum(K van) {
            this.van = van;
        }

        @Override
        public String naam() {
            return "" + SumMap_.this.getNaam() + " van " + van.getNaam();
        }

    }

    class SumMapTotal extends Sum_ {

        @Override
        public String naam() {
            return SumMap_.this.getNaam() + " (Total)";
        }

        @Override
        void verreken(int change, Event e) {
            super.verreken(change, e);
        }

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
