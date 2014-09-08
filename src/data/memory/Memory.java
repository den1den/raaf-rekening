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
import geld.RekeningHouder;

/**
 *
 * @author Dennis
 */
public class Memory {

    public static final int TYPE_REKENING = 0;
    public static final int TYPE_LAST_REKENING = 1;
    public static final int TYPE_CATEGORIE = 2;

    public final data.memory.interfaces.DyMapRekLast<Persoon> personen;
    public final data.memory.interfaces.DyMapRek<Incasso> incassos;
    public final data.memory.interfaces.StMapRek<Winkel> winkels;
    public final data.memory.interfaces.DyMap<AankoopCat> aankoopCats;
    public final data.memory.interfaces.DyMap<PathEntry> paths;

    public Memory(int initialCapacity) {
        this(
                new DyMapRekLast<Persoon>(initialCapacity) {

                    @Override
                    Persoon create(String naam) {
                        return new Persoon(naam);
                    }
                },
                new DyMapRek.DyMapRekIncasso(initialCapacity),
                new StMapRek<>(initialCapacity),
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

    public Memory(
            data.memory.interfaces.DyMapRekLast<Persoon> personen,
            data.memory.interfaces.DyMapRek<Incasso> incassos,
            data.memory.interfaces.StMapRek<Winkel> winkels,
            data.memory.interfaces.DyMap<AankoopCat> aankoopCats,
            data.memory.interfaces.DyMap<PathEntry> paths) {
        this.personen = personen;
        this.incassos = incassos;
        this.winkels = winkels;
        this.aankoopCats = aankoopCats;
        this.paths = paths;
    }

    public RekeningHouder getByRekening(String rekening) {
        Persoon p = personen.getRek(rekening);
        Incasso i = incassos.getRek(rekening);
        if (p == null) {
            if (i == null) {
                return null;
            }
            return i;
        }
        if (i == null) {
            return p;
        }
        throw new UnsupportedOperationException();
    }
}
