/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import tijd.Datum;

/**
 *
 * @author Dennis
 */
class RekeningI implements Rekening{

    int saldo = 0;

    private final List<Transactie> transacties = new LinkedList<>();
    private final Schulden schulden = new Schulden();

    @Override
    public List<Transactie> getTransacties() {
        return transacties;
    }
    
    public List<Transactie> getTransactiesEnSchulden(){
        List<Transactie> ts = getTransacties();
        for (Entry<RekeningHouder, List<Transactie>> e : schulden.entrySet()) {
            ts.addAll(e.getValue());
        }
        return ts;
    }

    @Override
    public int getSaldo() {
        return saldo;
    }

    @Override
    public void putSchuld(RekeningHouder van, Transactie t) {
        schulden.add(van, t);
    }

    @Override
    public void bij(Datum datum, int bedrag, Referentie referentie) {
        bij(new Transactie(datum, bedrag, false, referentie));
    }

    @Override
    public void bij(Transactie t) {
        if(t.isAf()){
            throw new IllegalArgumentException();
        }
        transacties.add(t);
        saldo += t.getBedrag();
    }

    @Override
    public void af(Datum datum, int bedrag, Referentie referentie) {
        af(new Transactie(datum, bedrag, true, referentie));
    }

    @Override
    public void af(Transactie t) {
        if(!t.isAf())
            throw new IllegalArgumentException();
        transacties.add(t);
        saldo -= t.getBedrag();
    }

    private class Schulden {

        Map<RekeningHouder, List<Transactie>> map = new HashMap<>(2);

        void add(RekeningHouder rh, Transactie t) {
            List<Transactie> transacties = map.get(rh);
            if (transacties == null) {
                transacties = new LinkedList<>();
            }
            transacties.add(t);
            if(t.isAf()){
                saldo -= t.getBedrag();
            }else saldo += t.getBedrag();
            map.put(rh, transacties);
        }

        List<Transactie> get(RekeningHouder rh) {
            return map.get(rh);
        }

        Set<Entry<RekeningHouder, List<Transactie>>> entrySet() {
            return map.entrySet();
        }

        /*
         @Override
         public Iterator<Entry<RekekingHouder, Transactie>> iterator() {
         return new DoubleEntryIt(entrySet().iterator());
         }

         private class DoubleEntryIt implements Iterator<Entry<RekekingHouder, Transactie>> {
         final Iterator<Entry<RekekingHouder, List<Transactie>>> it;
         RekekingHouder nxtRekeningHouder;
         Iterator<Transactie> nxtTransactie;

         public DoubleEntryIt(Iterator<Entry<RekekingHouder, List<Transactie>>> it) {
         this.it = it;
                
         }
            
         @Override
         public boolean hasNext() {
         if(lastList.isEmpty()){
         return loadStack();
         }else
         return true;
         }

         @Override
         public Entry<RekekingHouder, Transactie> next() {
         throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
         }

         private boolean loadStack() {
         while (it.hasNext()) {
         Entry<RekekingHouder, List<Transactie>> entry = it.next();
         rekekingHouder = entry.getKey();
         }
         }
         }*/
    }
}
