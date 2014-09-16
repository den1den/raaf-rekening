/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *
 * @author Dennis
 */
public abstract class Sum implements Iterable<Integer>{

    private int total;
    private final int startBedrag;
    private final List<Integer> diffs = new LinkedList<>();
    private final List<Event> history = new LinkedList<>();

    public Sum() {
        this.total = 0;
        this.startBedrag = 0;
    }

    public Sum(int startBedrag) {
        this.total = startBedrag;
        this.startBedrag = startBedrag;
    }

    
    void af(int bedrag, Event e) {
        if(bedrag < 0){
            throw new IllegalArgumentException();
        }
        verreken(-bedrag, e);
    }

    void bij(int bedrag, Event e) {
        if(bedrag < 0){
            throw new IllegalArgumentException();
        }
        verreken(bedrag, e);
    }
    
    /**
     * add and record the change.
     * @param change the amount the sum should change
     * @return the index of the diff
     */
    void verreken(int change, Event e){
        e.addSum(this, diffs.size());
        
        total += change;
        diffs.add(change);
        history.add(e);
    }

    int get() {
        return total;
    }
    
    int get(int index){
        return diffs.get(index);
    }
    
    public abstract String naam();

    @Override
    public String toString() {
        double centen = this.total;
        return "€"+centen/100;
    }

    @Override
    public Iterator<Integer> iterator() {
        //iterates over totals
        return new TotalIterator();
    }
    
    private class TotalIterator implements Iterator<Integer>{

        int nextI;
        int total = startBedrag;

        public TotalIterator() {
            nextI = -1;
        }
        
        @Override
        public boolean hasNext() {
            return nextI < diffs.size();
        }

        @Override
        public Integer next() {
            if(!hasNext()){
                throw new NoSuchElementException();
            }
            
            Integer result = total;
            if(nextI >= 0){
                total += diffs.get(nextI);
            }
            nextI++;
            return result;
        }
        
    }
}
