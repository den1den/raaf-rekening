/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.memory;

import data.Afschrift;
import data.Incasso;
import data.types.HasNaam;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dennis
 * @param <T>
 */
abstract class DyMapRek<T extends HasNaam> extends DyMapNaam<T> implements data.memory.interfaces.DyMapRek<T> {

    StMap<T> rekening;

    public DyMapRek(int intialCapacity) {
        super(intialCapacity);
        rekening = new StMap<>(intialCapacity);
    }

    @Override
    abstract T create(String naam);

    @Override
    public T getRek(String rekening) {
        return this.rekening.get(rekening);
    }

    @Override
    public void putRek(T t, String rekening) {
        this.put(t);
        this.rekening.put(t, rekening);
    }

    @Override
    public T findRek(Afschrift afschrift) {
        return findRek(afschrift.getVan(), afschrift.getVanRekening());
    }

    @Override
    public T findRek(String naam, String rekening) {
        T tRek = getRek(rekening);
        T tNaam;
        if (tRek == null) {
            tNaam = find(naam);
            putRek(tNaam, rekening);
            tRek = tNaam;
        } else if ((tNaam = get(naam)) == null) {
            //Rek found but not name
            put(tRek, naam);
        } else if (tNaam == tRek) { //Rek found and Name found
            //bijde zelfde
            return tRek;
        } else {
            //different persons that are probably the same?
            try {
                throw new Exception("Diff persons in db");
            } catch (Exception ex) {
                Logger.getLogger(DyMapRek.class.getName()).log(Level.SEVERE,
                        tRek + " and " + tNaam + " are prop the same...", ex);
            }
        }
        return tRek;
    }

    public static class DyMapRekIncasso extends DyMapRek<Incasso> {

        public DyMapRekIncasso(int intialCapacity) {
            super(intialCapacity);
        }

        @Override
        Incasso create(String naam) {
            throw new IllegalAccessError("Incasso's must be made with two args");
        }

        @Override
        public Incasso findRek(String naam, String rekening) {
            Incasso byRekening = getRek(rekening);
            Incasso byNaam = get(naam);
            if (byRekening == byNaam) {
                if (byNaam == null) {
                    byNaam = new Incasso(naam, rekening);
                    put(byNaam);
                    putRek(byNaam, rekening);
                }
                return byNaam;
            }
            throw new UnsupportedOperationException("Not yet");
        }
    }
}
