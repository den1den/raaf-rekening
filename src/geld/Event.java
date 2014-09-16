/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld;

import data.types.HasNaam;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import util.ItMap;

/**
 *
 * @author Dennis
 */
public class Event {

    private final HasNaam subject;
    private final HasNaam betreft;
    private final Referentie referentie;
    private final String message;
    private final ItMap<Sum> indexes;

    Event(HasNaam subject, HasNaam betreft, Referentie referentie, String message) {
        this.subject = subject;
        this.betreft = betreft;
        this.referentie = referentie;
        this.message = message;
        this.indexes = new ItMap<>(2, -1);
    }

    @Override
    public String toString() {
        return message+"["+subject+", "+betreft+", r:"+referentie.getRefString()+"]";
    }

    void addSum(Sum s, int index) {
        indexes.put(s, index);
    }
    
    public Set<Sum> getSums(){
        return indexes.keySet();
    }
    
    public List<Integer> getChanges(List<Sum> sums){
        List<Integer> changes = new ArrayList<>(sums.size());
        for (Sum sum : sums) {
            int index = indexes.get(sum);
            if(index != -1){
                changes.add(sum.get(index));
            }else{
                changes.add(0);
            }
        }
        return changes;
    }
    public Set<Map.Entry<Sum, Integer>> entrySetIndex(){
        return indexes.entrySet();
    }
    public Set<Map.Entry<Sum, Integer>> entrySet(){
        Set<Map.Entry<Sum, Integer>> entries = entrySetIndex();
        for (Map.Entry<Sum, Integer> entry : entries) {
            entry.setValue(entry.getKey().get(entry.getValue()));
        }
        return entries;
    }
}
