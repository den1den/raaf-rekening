/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.memory;

import data.AankoopCat;
import data.Incasso;
import data.Persoon;
import data.Winkel;
import file.manager.PathEntry;
import geld.Rekening;

/**
 *
 * @author Dennis
 */
public class Memory {

    public final DyMapRekLast<Persoon> personen;
    public final DyMapRek<Incasso> incassos;
    public final StMapMede<Winkel> winkels;
    public final DyMap<AankoopCat> aankoopCats;
    public final DyMap<PathEntry> paths;

    public Memory(int initialCapacity) {
        this(
                new DyMapRekLast<Persoon>(initialCapacity) {

                    @Override
                    Persoon create(String naam) {
                        return new Persoon(naam);
                    }
                },
                new DyMapRek.DyMapRekIncasso(initialCapacity),
                new StMapMede<>(initialCapacity),
                new DyMap<AankoopCat>(initialCapacity) {

                    @Override
                    AankoopCat create(String index) {
                        return new AankoopCat(index);
                    }
                },
                new DyMap<PathEntry>(initialCapacity) {

                    @Override
                    PathEntry create(String index) {
                        return new PathEntry(index);
                    }
                }
        );
    }

    private Memory(
            DyMapRekLast<Persoon> personen,
            DyMapRek<Incasso> incassos,
            StMapMede<Winkel> winkelsByMede,
            DyMap<AankoopCat> aankoopCats,
            DyMap<PathEntry> paths) {
        this.personen = personen;
        this.incassos = incassos;
        this.winkels = winkelsByMede;
        this.aankoopCats = aankoopCats;
        this.paths = paths;
    }
    /*
     public static Memory getMem(int initialCapacity) {
     DyMapRekLast<Persoon> personen
     = new DyMapRekLast<Persoon>(initialCapacity) {

     @Override
     Persoon create(String naam) {
     return new Persoon(naam);
     }
     };
     DyMapRek<Incasso> incassos
     = new DyMapRek.DyMapRekIncasso(initialCapacity);
     StMapMede<Winkel> winkels
     = new StMapMede<>(initialCapacity);
     DyMap<AankoopCat> aankoopCats
     = new DyMap<AankoopCat>(initialCapacity) {

     @Override
     AankoopCat create(String index) {
     return new AankoopCat(index);
     }
     };
     DyMap<PathEntry> paths
     = new DyMap<PathEntry>(initialCapacity) {

     @Override
     PathEntry create(String index) {
     return new PathEntry(index);
     }
     };
     return new Memory(personen, incassos, winkels, aankoopCats, paths);
     }*/

    public Rekening getByRekening(String rekening) {
        Persoon p = personen.getRek(rekening);
        Incasso i = incassos.getRek(rekening);
        if (p == null) {
            if (i == null) {
                return null;
            } else {
                return i;
            }
        }
        if (i == null) {
            throw new UnsupportedOperationException();
            return p;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public int getN() {
        int n = 10;
        System.err.println("getN: " + n);
        return n;
    }
}
