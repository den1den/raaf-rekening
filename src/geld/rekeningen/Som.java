/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.rekeningen;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Dennis
 */
public abstract class Som {

    private final int startBedrag;

    private final LinkedList<Integer> added = new LinkedList<>();

    private final List<Event> history = new LinkedList<>();

    public Som() {
        this.startBedrag = 0;
    }

    public Som(int startBedrag) {
        this.startBedrag = startBedrag;
    }

    int getTotal() {
        int total = startBedrag;
        for (Integer diff : added) {
            total += diff;
        }
        return total;
    }

    int getTotal(int atIndex) {
        if (atIndex < 0 || atIndex >= added.size()) {
            throw new IllegalArgumentException();
        }
        int total = startBedrag;

        int index = 0;
        for (Integer diff : added) {
            if (index == atIndex) {
                return total;
            }
            total += diff;
            index++;
        }
        return total;
    }

    int put(int diff, Event e) {
        int index = added.size();
        added.add(diff);
        history.add(e);
        return index;
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

    public List<Event> getHistory() {
        return history;
    }

    abstract public String beschrijving();
}
