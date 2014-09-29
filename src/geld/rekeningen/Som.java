/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.rekeningen;

import geld.rekeningen.Som.Record;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import util.Map2Ints;

/**
 *
 * @author Dennis
 */
public abstract class Som implements Iterable<Record>{
    
    private final int startBedrag;
    private final LinkedList<Record> records = new LinkedList<>();
    private boolean init = false;

    public static class Record {
        public final int diff;
        public final Event history;

        public Record(int diff, Event history) {
            this.diff = diff;
            this.history = history;
        }
        
    }

    public Som() {
        this.startBedrag = 0;
    }

    public Som(int startBedrag) {
        this.startBedrag = startBedrag;
    }

    public LinkedList<Record> getRecords() {
        return records;
    }

    int put(int diff, Event e) {
        records.add(new Record(diff, e));
        
        int index = records.size();
        e.addSum(this, index);
        return index;
    }

    public int getDiff(int index) throws IndexOutOfBoundsException {
        checkChangeIndex(index);
        return records.get(index - 1).diff;
    }
    
    public Event getHistory(int index) {
        checkChangeIndex(index);
        return records.get(index - 1).history;
    }

    public int getTotal() {
        int total = startBedrag;
        for (Record r : records) {
            total += r.diff;
        }
        return total;
    }

    public int getTotal(int index) {
        checkIndex(index);

        int i = 0;
        int total = startBedrag;

        Iterator<Record> it = records.iterator();
        while (i < index) {
            total += it.next().diff;
            i++;
        }
        return total;
    }

    void af(int bedrag, Event e) {
        if (bedrag < 0) {
            throw new IllegalArgumentException();
        }
        put(-bedrag, e);
    }

    void bij(int bedrag, Event e) {
        if (bedrag < 0) {
            throw new IllegalArgumentException();
        }
        put(bedrag, e);
    }

    abstract public String beschrijving();

    @Override
    public String toString() {
        int totaal = getTotal();
        return beschrijving() + "€" + totaal / 100 + "." + totaal % 100;
    }

    /**
     *
     * @param key
     * @param indexes
     * @return
     */
    public int getTotalDiff(List<Integer> indexes) {
        int diff = 0;
        Iterator<Integer> it = indexes.iterator();
        if (!it.hasNext()) {
            throw new IllegalAccessError();
        }

        Integer index = it.next();
        diff += getDiff(index);
        while (it.hasNext()) {
            index = it.next();
            diff += getDiff(index);
        }
        
        return diff;
    }

    private void checkIndex(int index) {
        if (index < 0 || index > records.size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", numChanges: " + (index));
        }
    }

    private void checkChangeIndex(int index) {
        if (index < 1 || index > records.size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", numChanges: " + (index));
        }
    }

    @Override
    public Iterator<Record> iterator() {
        return records.iterator();
    }
    
    public void init(){
        if(init){
            throw new IllegalStateException();
        }
        init = true;
    }
}
