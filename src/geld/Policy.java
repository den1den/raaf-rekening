/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld;

import data.Afschrift;
import data.BewoonPeriode;
import data.Bonnetje;
import data.Incasso;
import data.Kookdag;
import data.Persoon;
import data.Winkel;
import data.memory.Memory;
import data.types.HasBedrag;
import data.types.HasDate;
import geld.Transactie.Record;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import tijd.Datum;
import util.diplay.ResultPrintStream;

/**
 *
 * @author Dennis
 */
public class Policy {

    final private int version;
    final private RekeningHouderContant raafrekeneing;
    final private Comparator<Datum> byDay = HasDate.getOnDay();
    private Memory memory = null;

    public Policy(int version, RekeningHouderContant raaf, RekeningHouder kookRekening) {
        this.version = version;
        if (raaf == null || kookRekening == null) {
            throw new IllegalArgumentException();
        }
        this.raafrekeneing = raaf;
        this.kookRekening = kookRekening;
    }

    public void setMemory(Memory memory) {
        if (memory == null) {
            throw new IllegalArgumentException();
        }
        this.memory = memory;
    }

    public void verrekenKookdagen(List<Kookdag> allDagen, Map<Persoon, Persoon> kookSchuldDelers) {
        for (Kookdag kookdag : allDagen) {
            verrekenKookdag(kookdag, kookSchuldDelers);
        }
    }
    private final RekeningHouder kookRekening;

    private void verrekenKookdag(Kookdag kookdag, Map<Persoon, Persoon> kookSchuldDelers) {
        Persoon kok = kookdag.getKok();
        Map<Persoon, Integer> meeters = kookdag.getMeeters();

        if (kok.kwijtschelden()) {
            //niets te verekenen
        } else {
            int totaalMeeters = 0;
            for (Map.Entry<Persoon, Integer> meeter : meeters.entrySet()) {
                if (!meeter.getKey().kwijtschelden()) {
                    totaalMeeters += meeter.getValue();
                }
            }

            /*
             * Betalende personen die moeten opdraaien voor de kosten, inclusief
             * de kok dus.
             */
            double prijspp = (double) (kookdag.getBedrag()) / totaalMeeters;
            int prijs = (int) (Math.ceil(prijspp));
            int kokPrijs = kookdag.getBedrag() - (totaalMeeters - 1) * prijs;

            int total = 0;
            for (Map.Entry<Persoon, Integer> m : meeters.entrySet()) {
                Persoon persoon = m.getKey();
                int verreken;

                Persoon verantwoordelijk = kookSchuldDelers.get(persoon);
                if (verantwoordelijk == null) {
                    verantwoordelijk = persoon;
                }

                if (persoon.equals(kok)) {
                    verreken = kookdag.getBedrag()
                            - kokPrijs - prijs * (m.getValue() - 1);
                    kookRekening.addSchuld(verantwoordelijk, verreken, kookdag);
                    total += verreken;

                } else if (!persoon.kwijtschelden()) {
                    verreken = prijs * (m.getValue());
                    verantwoordelijk.addSchuld(kookRekening, verreken, kookdag);
                    total -= verreken;
                }
            }
            if (total != 0) {
                throw new Error();
            }
            new ResultPrintStream(version).showSchuld(memory.personen.getAll(), kookRekening);
        }
    }

    public List<Record> verrekenBewoonPeriodes(
            Collection<BewoonPeriode> bewoonPeriodes) {
        List<Record> records = new LinkedList<>();
        for (BewoonPeriode bewoonPeriode : bewoonPeriodes) {
            Persoon persoon = bewoonPeriode.getPersoon();
            for (BewoonPeriode.SubPeriode subPeriode : bewoonPeriode) {
                final int bedrag = 2000;
                persoon.addSchuld(raafrekeneing, bedrag, subPeriode);
            }
        }
        return records;
    }

    public List<Record> verrekenBonnetjes(
            Set<Bonnetje> bonnetjes) {
        List<Record> records = new LinkedList<>();
        for (Bonnetje bonnetje : bonnetjes) {
            raafrekeneing.addSchuld(bonnetje.getPersoon(), bonnetje.getBedrag(), bonnetje);
        }
        return records;
    }

    public Collection<Record> verwerkAfschriften(Collection<Afschrift> afschriften,
            Collection<Bonnetje> bonnetjes) {
        Collection<Record> records = new LinkedList<>();
        ArrayList<Bonnetje> bonnetjes1 = new ArrayList<>(bonnetjes);
        Collections.sort(bonnetjes1, Bonnetje.getByDate());
        for (Afschrift afschrift : afschriften) {
            try {
                records.add(verwerkAfschrift(afschrift, bonnetjes1));
            } catch (Error e) {
                throw new Error("At afschrift: " + afschrift, e);
            }
        }
        return records;
    }
    final private String ING_NAAM = "ING";
    final private String UPC_NAAM = "UPC";
    final private Winkel WINKEL_UNKNOWN = new Winkel("Niet bekend", null, null);

    private <RL extends List<Bonnetje> & RandomAccess> Record verwerkAfschrift(
            Afschrift afschrift,
            RL bonnetjes) {
        Incasso ING_INCASSO = memory.incassos.get(ING_NAAM);

        RekeningHouder tegenRekeningHouder; //must be set
        Referentie referentie = null; //must be set

        switch (afschrift.getCode()) {
            case "BA": //betaalautomaat
                if (!afschrift.getMutatieSoort().equals("Betaalautomaat")) {
                    throw new Error("Betaalautomaat verwacht ipv: " + afschrift.getMutatieSoort());
                }
                List<Bonnetje> candidates = HasBedrag.searchOn(
                        HasDate.searchOn(bonnetjes, afschrift.getDate(), byDay),
                        afschrift.getBedrag());
                if (candidates.size() == 1) {
                    Bonnetje bonnetje = candidates.get(0);
                    bonnetjes.remove(bonnetje);
                    referentie = bonnetje;
                    tegenRekeningHouder = bonnetje.getWinkel();
                } else if (candidates.isEmpty()) {
                    try {
                        throw new Exception("afschrift not found in: " + bonnetjes);
                    } catch (Exception ex) {
                        Logger.getLogger(Policy.class.getName()).log(Level.INFO, "Je moet het bonnetje zoeken van " + afschrift, ex);
                    }
                    referentie = afschrift;
                    tegenRekeningHouder = WINKEL_UNKNOWN;
                } else { // > 1
                    referentie = new ReferentieMultiple(candidates);
                    throw new UnsupportedOperationException("Wat is de tegenrekeninghouder? Undertermind?");
                }
                break;
            case "OV":
                if (!afschrift.getMutatieSoort().equals("Overschrijving")) {
                    throw new Error("Overschrijving verwacht ipv: " + afschrift.getMutatieSoort());
                }
                //overboeking
                tegenRekeningHouder = memory.personen.findRek(afschrift.getVan(), afschrift.getVanRekening());
                referentie = afschrift;
                break;
            case "GT":
                if (!afschrift.getMutatieSoort().equals("Internetbankieren")) {
                    throw new Error("Internetbankieren verwacht ipv: " + afschrift.getMutatieSoort());
                }
                //kan incasso zijn => handmatige overboeking
                try {
                    throw new UnsupportedOperationException();
                } catch (UnsupportedOperationException ex) {
                    Logger.getLogger(Policy.class.getName()).log(Level.SEVERE, "GT gaat nog fout", ex);
                }

                if (afschrift.isAf()) {
                    //terugboeking
                    tegenRekeningHouder = memory.getByRekening(afschrift.getVanRekening());
                } else {

                    if (isMededelingRaRe(afschrift.getMededeling())) {
                        //huisrekening
                        tegenRekeningHouder = memory.personen.findRek(afschrift);
                    } else {
                        throw new UnsupportedOperationException();
                    }
                }

                referentie = afschrift;
                break;
            case "DV":
                if (!afschrift.getMutatieSoort().equals("Diversen")) {
                    throw new Error("Diversen verwacht ipv: " + afschrift.getMutatieSoort());
                }
                if (afschrift.getMededeling().contains("productrekening")
                        && afschrift.getMededeling().contains("ING")) {
                    //defeniatly ing
                    if (ING_INCASSO == null) {
                        ING_INCASSO = memory.incassos.findRek(ING_NAAM, afschrift.getVanRekening());
                    }

                    tegenRekeningHouder = ING_INCASSO;
                    referentie = afschrift;
                } else {
                    throw new Error();
                }
                break;
            case "IC": //incasso
                if (!afschrift.getMutatieSoort().equals("Incasso")) {
                    throw new Error("Incasso verwacht ipv: " + afschrift.getMutatieSoort());
                }
                //incasso van UPC ofzo
                if (afschrift.getMededeling().trim().substring(0, 3).equalsIgnoreCase("upc")
                        || afschrift.getMededeling().contains("UPC Nederland B.V.")) {
                    tegenRekeningHouder = memory.incassos.findRek(UPC_NAAM, afschrift.getVanRekening());
                } else {
                    throw new UnsupportedOperationException();
                }
                referentie = afschrift;
                break;
            case "PO":
                if (!afschrift.getMutatieSoort().equals("Periodieke overschrijving")) {
                    throw new Error("Periodieke overschrijving verwacht ipv: " + afschrift.getMutatieSoort());
                }
                if (isMededelingRaRe(afschrift.getMededeling())) {
                    referentie = afschrift;
                    tegenRekeningHouder = memory.personen.findRek(afschrift);
                } else {
                    throw new UnsupportedOperationException();
                }
                break;
            case "ST":
                if (!afschrift.getMutatieSoort().equals("Storting")) {
                    throw new Error("Storting verwacht ipv: " + afschrift.getMutatieSoort());
                }
                referentie = afschrift;
                tegenRekeningHouder = raafrekeneing.getContant();
                break;
            case "GM":
                if (!afschrift.getMutatieSoort().equals("Geldautomaat")) {
                    throw new Error("Geldautomaat verwacht ipv: " + afschrift.getMutatieSoort());
                }
                //naar contant
                if (afschrift.getBedrag() % 500 != 0) {
                    throw new Error("bedrag: " + afschrift.getBedrag() + " kan niet gepind zijn...");
                }
                referentie = afschrift;
                tegenRekeningHouder = raafrekeneing.getContant();
                break;
            default:
                throw new Error("Code: " + afschrift.getCode() + " niet bekend");
        }

        if (referentie == null) {
            try {
                throw new Exception("referentie = " + referentie);
            } catch (Exception ex) {
                Logger.getLogger(Policy.class.getName()).log(Level.SEVERE, "Geen referentie detected at: " + afschrift, ex);
            }
            referentie = afschrift;
        }
        if (tegenRekeningHouder == null) {
            try {
                throw new Exception("tegenRekeningHouden = " + tegenRekeningHouder);
            } catch (Exception ex) {
                Logger.getLogger(Policy.class.getName()).log(Level.SEVERE, "Regenrekeninghouder could not be identified at: " + afschrift, ex);
            }
        }

        RekeningHouder van, naar;
        if (afschrift.isAf()) {
            van = raafrekeneing;
            naar = tegenRekeningHouder;
        } else {
            van = tegenRekeningHouder;
            naar = raafrekeneing;
        }
        int bedrag = afschrift.getBedrag();
        Datum datum = afschrift.getDate();

        //return van.betaald(naar, bedrag, datum, referentie);
        //return van.moetBetalen(naar, bedrag, datum, referentie);
        //return van.betaal(naar, bedrag, referentie, datum)
    }

    private boolean isMededelingRaRe(String mededeling) {
        String meld = mededeling.toLowerCase();
        return meld.contains("huisrekening")
                || meld.contains("huis rekening")
                || (meld.contains("huiskosten"));
    }

}
