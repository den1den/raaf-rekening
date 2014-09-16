/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.geldImpl.OLD;

import geld.Event;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Dennis
 */
class Recorder {

    private final int n;
    private final List<Record> records = new LinkedList<>();

    public Recorder(int n) {
        this.n = n;
    }

    void record(Event e, int... changes) {
        Record r = new Record(e, changes); //checks for length
        records.add(r);
    }

    /**
     * @param changes word aangevuld met 0 als het te kort is
     * @return
     */
    void record0s(Event e, int... changes) {
        if (changes.length > n) {
            throw new IllegalArgumentException();
        } else if (changes.length < n) {
            int[] old = changes;
            changes = new int[n];
            System.arraycopy(old, 0, changes, 0, old.length); //en rest 0
        }
        return record(e, changes);
    }

    void recordMany(Event e, int[]  
        ... changes) {
        int[] changesFull = util.Arrays.combine(changes);
        return record(e, changesFull);
    }

    /**
     *
     * @param e
     * @param changes aangevuld met 0s als ie te kort is
     */
    void recordArray0s(Event e, int[] changes) {
        if (changes.length > n) {
            throw new IllegalArgumentException();
        } else if (changes.length < n) {
            int[] old = changes;
            changes = new int[n];
            System.arraycopy(old, 0, changes, 0, old.length); //en rest 0
        }
        return record(e, changes);
    }

    class Record {

        private final Event e;
        private final int[] changeIndex;

        Record(Event e, int[] changeIndex) {
            if (changeIndex.length != n) {
                throw new IllegalArgumentException("Not all changes specified");
            }
            this.e = e;
            this.changeIndex = changeIndex;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(e.toString());
            if (changeIndex.length > 0) {
                sb.append("; ").append(changeIndex[0]);
                for (int i = 1; i < changeIndex.length; i++) {
                    sb.append(", ").append(changeIndex[i]);
                }
            }
            return sb.toString();

        }
    }

}
