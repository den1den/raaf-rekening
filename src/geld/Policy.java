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
import geld.geldImpl.LeenRekening;
import geld.geldImpl.RaafRekening;
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
    final private Memory memory;

    public Policy(int version, Memory memory) {
        this.version = version;
        if (version < 0 || memory == null) {
            throw new IllegalArgumentException();
        }
        this.memory = memory;
    }

    final private Comparator<Datum> byDay = HasDate.getOnDay();
    final private String ING_INCASSO_NAAM = "ING";
    final private String UPC_INCASSO_NAAM = "UPC";
    final private String WINKEL_UNKNOWN_NAAM = "Niet bekend";

    public void verrekenKookdagen(List<Kookdag> allDagen,
            Map<Persoon, Persoon> kookSchuldDelers,
            LeenRekening r) {
        allDagen.stream().forEach((kookdag) -> {
            verrekenKookdag(kookdag, kookSchuldDelers, r);
        });
    }

    private void verrekenKookdag(Kookdag kookdag,
            Map<Persoon, Persoon> kookSchuldDelers,
            LeenRekening r) {
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
                    //kok
                    verreken = kookdag.getBedrag()
                            - kokPrijs - prijs * (m.getValue() - 1);
                    verantwoordelijk.moetKrijgenVan(r, verreken, kookdag);
                    total += verreken;

                } else if (!persoon.kwijtschelden()) {
                    //meeter
                    verreken = prijs * (m.getValue());
                    r.moetKrijgenVan(verantwoordelijk, verreken, kookdag);
                    //ResultPrintStream.showLastT(kookRekening, verantwoordelijk);
                    total -= verreken;
                }
            }
            if (total != 0) {
                throw new Error();
            }
        }
    }

    public void verrekenBewoonPeriodes(
            Collection<BewoonPeriode> bewoonPeriodes,
            RaafRekening rekening) {
        for (BewoonPeriode bewoonPeriode : bewoonPeriodes) {
            Persoon persoon = bewoonPeriode.getPersoon();
            for (BewoonPeriode.SubPeriode subPeriode : bewoonPeriode) {
                final int bedrag = 2000;
                rekening.krijgtNog(bedrag, persoon, subPeriode);
                //persoon.betaaldNog(verrekMetRekening, bedrag, subPeriode);
            }
        }
    }

    public void verrekenBonnetjes(
            Set<Bonnetje> bonnetjes,
            RaafRekening rekening) {
        for (Bonnetje bonnetje : bonnetjes) {
            rekening.betaaldDoor(bonnetje.getPersoon(), bonnetje.getBedrag(), bonnetje);
        }
    }

    public void verwerkAfschriften(Set<Afschrift> afschriften, Set<Bonnetje> bonnetjes, RaafRekening rekening) {
        ArrayList<Bonnetje> bonnetjes1 = new ArrayList<>(bonnetjes);
        Collections.sort(bonnetjes1, Bonnetje.getByDate());
        for (Afschrift afschrift : afschriften) {
            try {
                verwerkAfschrift(afschrift, bonnetjes1, rekening);
            } catch (Error e) {
                throw new Error("At afschrift: " + afschrift, e);
            }
        }
    }

    private <RL extends List<Bonnetje> & RandomAccess> void verwerkAfschrift(
            Afschrift afschrift,
            RL bonnetjes,
            RaafRekening rekening) {
        int bedrag = afschrift.getBedrag();
        Referentie referentie = afschrift;

        switch (afschrift.getCode()) {
            case "BA": //betaalautomaat
                if (!afschrift.getMutatieSoort().equals("Betaalautomaat")) {
                    throw new Error("Betaalautomaat verwacht ipv: " + afschrift.getMutatieSoort());
                }
                if (!afschrift.getVanRekening().isEmpty()) {
                    throw new Error("Assumed it did");
                }
                if (!afschrift.getVan().toLowerCase().contains("betaalautomaat")) {
                    throw new Error("Assumed it did");
                }
                if (!afschrift.isAf()) {
                    throw new Error();
                }

                Rekening r = null;
                List<Referentie> refs;
                List<Bonnetje> candidates = HasBedrag.searchOn(
                        HasDate.searchOn(bonnetjes, afschrift.getDate(), byDay),
                        afschrift.getBedrag());

                if (candidates.size() == 1) {
                    Bonnetje bon = candidates.get(0);

                    //bonnetje found, kan maar 1 keer gevonden worden
                    bonnetjes.remove(bon);

                    //wel referentie toevoegen
                    List<Transactie> trs = bon.getPersoon().getTransactiesRef(bon.getWinkel());
                    if (trs == null) {
                        throw new UnsupportedOperationException();
                    }
                    List<Transactie> tBonnetje = new ArrayList<>(1);
                    for (Transactie transactie : trs) {//find this bonnetje
                        if (transactie.getReferentie() == bon) {
                            tBonnetje.add(transactie);
                        }
                    }
                    if (tBonnetje.size() != 1) {
                        throw new UnsupportedOperationException("Bonnejte niet verrekend");
                    }
                    Transactie vorrigeT = tBonnetje.get(0);
                    vorrigeT.addReferentie(bon);

                    //hoeft niet dubbel
                    return;
                } else if (candidates.isEmpty()) {
                    refs = new ArrayList<>(1);
                    Logger.getLogger(Policy.class.getName()).log(Level.INFO, "Je moet het bonnetje zoeken van " + afschrift);

                    Winkel w = memory.winkels.getMede(afschrift.getMededeling());
                    if (w == null) {
                        w = new Winkel(afschrift.getMededeling(), null);
                        memory.winkels.putMede(w, afschrift.getMededeling());
                    }

                    r = w;
                } else { // > 1
                    refs = new ArrayList<>(candidates.size() + 1);

                    Bonnetje bon = candidates.get(0);
                    Winkel soiso = bon.getWinkel();
                    for (int i = 1; i < candidates.size(); i++) {
                        bon = candidates.get(i);
                        Winkel w = bon.getWinkel();
                        if (w != soiso) {
                            //ambigious
                            rhi = null;
                            refs.add(afschrift);
                            verrekMetRekening.add(true, rhi, bedrag, new ReferentieMultiple(refs));
                            throw new UnsupportedOperationException("Wat is de tegenrekeninghouder? Undertermind?");
                        }
                    }
                    r = soiso;
                }
                refs.add(afschrift);
                referentie = new ReferentieMultiple(refs);
                rekening.gekocht(r, bedrag, referentie);

                return;
            case "OV":
                if (!afschrift.getMutatieSoort().equals("Overschrijving")) {
                    throw new Error("Overschrijving verwacht ipv: " + afschrift.getMutatieSoort());
                }
                //overboeking
                if (!afschrift.isAf()) {
                    if (isMededelingRaRe(afschrift)) {
                        //zeker
                        Persoon p = memory.personen.findRek(afschrift);
                        rekening.plusContributie(p, bedrag, afschrift);
                        //throw new UnsupportedOperationException("Gebeuren twee dingen tegelijk, moet in Raafrekenng complexe functies maken en in history zetter per handeling, zie Word");
                        //ResultPrintStream.lijstje(p, rekening);
                        return;
                    } else {
                        //niet zeker
                        Persoon p = memory.personen.getRek(afschrift.getVanRekening());
                        if (p != null) {
                            //persoon heeft iets erop geboekt, dus igg toevoegen aan rekening
                            rekening.plusContributie(p, bedrag, referentie);
                        } else {
                            //kan nieuw persoon zijn maar wel rare start dan...
                            p = memory.personen.findRek(afschrift);
                            System.err.println("Niet zeker afschrift van " + p + ": " + afschrift);
                            rekening.plusContributie(p, bedrag, referentie);
                        }
                        return;
                    }
                }
                throw new UnknownError();
                break;
            case "GT":
                if (!afschrift.getMutatieSoort().equals("Internetbankieren")) {
                    throw new Error("Internetbankieren verwacht ipv: " + afschrift.getMutatieSoort());
                }
                //kan incasso zijn => handmatige overboeking

                if (afschrift.isAf()) {

                    Incasso in = memory.incassos.getRek(afschrift.getVanRekening());
                    if (in != null) {
                        //incasso detected
                        rekening.betaald(bedrag, in, referentie);
                        return;
                    } else {
                        Persoon p = memory.personen.findRek(afschrift);

                        Object o = ResultPrintStream.lijst1(p, rekening);

                        //kan voorgeschoten zijn...
                        if (afschrift.getMededeling().toLowerCase().contains("voorgeschoten")) {

                            //wasmachine voorgeschoten
                            rekening.betaaldDoor(p, bedrag, afschrift);

                            //ResultPrintStream.lijst2(o, p, rekening);
                            return;
                        } else if (afschrift.getMededeling().contains(" Correctie Raafrekening")
                                && afschrift.getVan().contains("D.J. van den Brand")) {
                            Logger.getLogger(Policy.class.getName()).log(Level.INFO, "Eva contant gebeure1");
                            rekening.betaaldDoor(p, bedrag, referentie);
                            return;
                        } else if (afschrift.getMededeling().equals(" 20e te veel betaald voor de huis rekening")
                                && afschrift.getVan().equals("M.M.C. Tilburgs                 ")) {
                            rekening.terrugGave(bedrag, p, referentie);
                            //ResultPrintStream.lijst2(o, p, rekening);
                            return;
                        } else if (afschrift.getMededeling().equals(" Ik had 27.17 eigen geld gestort  en heb al 20 gepind, dit is rest")) {
                            Logger.getLogger(Policy.class.getName()).log(Level.INFO, "Eva contant gebeure2 (niet verrekend)");
                            //teruggave zonder de kas, meer een écht lening dus?
                            rekening.terrugGave(bedrag, p, referentie);
                            return;
                        } else if (afschrift.getMededeling().equals(" Toilet papier 11-9-12")) {
                            rekening.betaaldDoor(p, bedrag, afschrift);
                            //ResultPrintStream.lijst2(o, p, rekening);
                            return;
                        }
                    }
                    //zal wss tg incasso zijn?
                    Logger.getLogger(Policy.class.getName()).log(Level.INFO, "GT gaat nog fout");
                    throw new UnsupportedOperationException();
                } else {
                    //bijboeking
                    if (isMededelingRaRe(afschrift)) {
                        rekening.plusContributie(
                                memory.personen.findRek(afschrift), bedrag,
                                referentie);
                        return;
                    } else {
                        throw new UnsupportedOperationException();
                    }
                }

                try {
                    throw new UnsupportedOperationException();
                } catch (UnsupportedOperationException ex) {
                    Logger.getLogger(Policy.class.getName()).log(Level.SEVERE, "GT gaat nog fout", ex);
                }

                /*
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
                }*/
                break;
            case "DV":
                if (!afschrift.getMutatieSoort().equals("Diversen")) {
                    throw new Error("Diversen verwacht ipv: " + afschrift.getMutatieSoort());
                }
                if (afschrift.getMededeling().contains("productrekening")
                        && afschrift.getMededeling().contains("ING")) {
                    //defeniatly ing
                    Incasso ing = memory.incassos.get(ING_INCASSO_NAAM);
                    if (ing == null) {
                        ing = memory.incassos.findRek(ING_INCASSO_NAAM, afschrift.getVanRekening());
                        memory.incassos.put(ing, afschrift.getVan());
                    }

                    rekening.betaald(bedrag, ing, referentie);
                } else {
                    throw new Error();
                }
                return;
            case "IC": //incasso
                if (!afschrift.getMutatieSoort().equals("Incasso")) {
                    throw new Error("Incasso verwacht ipv: " + afschrift.getMutatieSoort());
                }
                //incasso van UPC ofzo
                if (afschrift.getMededeling().trim().substring(0, 3).equalsIgnoreCase("upc")
                        || afschrift.getMededeling().contains("UPC Nederland B.V.")) {
                    Incasso incasso = memory.incassos.findRek(UPC_INCASSO_NAAM, afschrift.getVanRekening());
                    rekening.betaald(bedrag, incasso, referentie);
                    return;
                } else {
                    throw new UnsupportedOperationException();
                }
            case "PO":
                if (!afschrift.getMutatieSoort().equals("Periodieke overschrijving")) {
                    throw new Error("Periodieke overschrijving verwacht ipv: " + afschrift.getMutatieSoort());
                }
                if (isMededelingRaRe(afschrift)) {
                    rekening.plusContributie(memory.personen.findRek(afschrift), bedrag, referentie);
                    return;
                } else {
                    throw new UnsupportedOperationException();
                }
            case "ST":
                if (!afschrift.getMutatieSoort().equals("Storting")) {
                    throw new Error("Storting verwacht ipv: " + afschrift.getMutatieSoort());
                }
                if (afschrift.isAf()) {
                    throw new Error("Rare storting...");
                }
                Logger.getLogger(Policy.class.getName()).log(Level.SEVERE, "Contant nog niet implemented1");
                return;
            case "GM":
                if (!afschrift.getMutatieSoort().equals("Geldautomaat")) {
                    throw new Error("Geldautomaat verwacht ipv: " + afschrift.getMutatieSoort());
                }
                //naar contant
                if (afschrift.getBedrag() % 500 != 0) {
                    throw new Error("bedrag: " + afschrift.getBedrag() + " kan niet gepind zijn...");
                }
                Logger.getLogger(Policy.class.getName()).log(Level.SEVERE, "Contant nog niet implemented2");
                //OLD: verrekMetRekening.verwerk(verrekMetRekening.getContant(), afschrift);
                return;
            default:
                throw new Error("Code: " + afschrift.getCode() + " niet bekend");
        }

        //return van.betaald(naar, bedrag, datum, referentie);
        //return van.moetBetalen(naar, bedrag, datum, referentie);
        //return van.betaal(naar, bedrag, referentie, datum)
        throw new UnsupportedOperationException("Not yet");
    }

    private boolean isMededelingRaRe(Afschrift afschrift) {
        String meld = afschrift.getMededeling().toLowerCase();
        boolean isRaRa = meld.contains("huisrekening")
                || meld.contains("huis rekening")
                || meld.contains("huiskosten")
                || meld.contains("maandkosten de raaf");
        if (!isRaRa) {
            if (afschrift.getVan().equalsIgnoreCase("B.F.W. van Vugt")
                    && (afschrift.getMededeling().equalsIgnoreCase(" HOMO"))
                    || afschrift.getMededeling().toUpperCase().contains("SCHOOIER")) {
                return true;
            }
            return false;
        }
        return isRaRa;
    }

}
