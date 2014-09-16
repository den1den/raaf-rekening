/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld;

import data.types.HasNaam;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Dennis
 */
abstract class SumMap<K extends HasNaam> {

    private final Map<K, Sum> m;
    private final List<Event> all = new LinkedList<>();

    public SumMap() {
        m = new HashMap<>(10);
    }

    public SumMap(int initialCapacity) {
        m = new HashMap<>(initialCapacity);
    }

    /**
     * Add a value to the sum of the key.
     *
     * @param key
     * @param value
     * @return index to difference
     */
    public void add(K key, int value, Event e) {
        Sum s = m.get(key);
        if (s == null) {
            s = new MapSum(key);
            m.put(key, s);
        }
        s.verreken(value, e);
    }

    public int get(K key) {
        Sum l = m.get(key);
        if (l == null) {
            return 0;
        } else {
            return l.get();
        }
    }

    public int get() {
        int totaal = 0;
        for (Map.Entry<K, Sum> s : m.entrySet()) {
            totaal += s.getValue().get();
        }
        return totaal;
    }

    private class MapSum extends Sum {

        private final K van;

        public MapSum(K van) {
            this.van = van;
        }

        @Override
        public String naam() {
            return "" + SumMap.this.getNaam() + " van " + van.getNaam();
        }

    }

    abstract String getNaam();
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
