/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.rekeningen;

import data.Afschrift;
import data.BetaaldVia;
import data.BewoonPeriode;
import data.Bonnetje;
import data.Incasso;
import data.Kookdag;
import data.Persoon;
import data.ContantRecord;
import data.ContantRecord.DoubleContantRecordSet;
import data.Winkel;
import data.memory.Memory;
import data.types.HasBedrag;
import data.types.HasDatum;
import geld.Referentie;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import tijd.Datum;
import tijd.IntervalDatums;

/**
 *
 * @author Dennis
 */
public class Policy {

    final private int version;
    final private Memory memory;
    private IntervalDatums periode;

    public Policy(int version, Memory memory) {
        this(version, memory, IntervalDatums.TOT_NU);
    }

    public Policy(int version, Memory memory, IntervalDatums periode) {
        if (version < 0 || memory == null || periode == null) {
            throw new IllegalArgumentException();
        }
        this.version = version;
        this.memory = memory;
        this.periode = periode;
    }

    final private String ING_INCASSO_NAAM = "ING";
    final private String UPC_INCASSO_NAAM = "UPC";
    final private String MARKTPLAATS = "Marktplaats";
    final private String WINKEL_UNKNOWN_NAAM = "Niet bekend";

    public void verrekenKookdagen(List<Kookdag> allDagen,
            Map<Persoon, Persoon> kookSchuldDelers,
            RekeningLeen r) {
        if (periode != IntervalDatums.TOT_NU) {
            Logger.getLogger(Policy.class.getName())
                    .log(Level.INFO, "Kookdagen aan het verekenen terwijl er een restricitie is van tot: {0}", periode);
        }
        for (Kookdag kookdag : allDagen) {
            verrekenKookdag(kookdag, kookSchuldDelers, r);
        }
    }

    private void verrekenKookdag(Kookdag kookdag,
            Map<Persoon, Persoon> kookSchuldDelers,
            RekeningLeen r) {
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

                Event e;
                String message;
                if (persoon.equals(kok)) {
                    //kok
                    verreken = kookdag.getBedrag()
                            - kokPrijs - prijs * (m.getValue() - 1);
                    message = "{1} heeft gekookt";
                    e = r.newE(verantwoordelijk, kookdag, message);

                    verantwoordelijk.doKrijgtNogVanDuo(r, verreken, e);
                    r.doRekeningBijSingle(-verreken, e);

                    total += verreken;
                } else if (!persoon.kwijtschelden()) {
                    //meeter
                    verreken = prijs * (m.getValue());
                    message = "{1} heeft meegegeten";
                    e = r.newE(verantwoordelijk, kookdag, message);

                    verantwoordelijk.doKrijgtNogVanDuo(r, -verreken, e);
                    r.doRekeningBijSingle(verreken, e);

                    total += verreken;
                }
            }
            if (total != 0) {
                throw new Error();
            }
        }
    }

    public void verrekenBewoonPeriodes(
            Collection<BewoonPeriode> bewoonPeriodes,
            RekeningLeenBudget rekening) {
        for (BewoonPeriode bewoonPeriode : bewoonPeriodes) {
            Persoon persoon = bewoonPeriode.getPersoon();
            final int bedrag = bewoonPeriode.getBedrag();
            for (BewoonPeriode.SubPeriode subPeriode : bewoonPeriode) {
                if (subPeriode.getEind().isIn(periode)) {

                    String message = "{1} moet raafrekening betalen aan {0}";
                    Event e = rekening.newE(persoon, subPeriode, message);

                    rekening.doBudgetBijSingle(bedrag, e);
                    rekening.doKrijgtNogVanDuo(persoon, bedrag, e);

                } else {
                    //System.out.println("discarding bewoon: " + subPeriode.getEind());
                }
            }
        }
    }

    public void verrekenBonnetjes(
            Set<Bonnetje> bonnetjes,
            RekeningLeenBudget rekening) {
        for (Bonnetje bonnetje : bonnetjes) {
            if (bonnetje.getDatum().isIn(periode)) {

                RekeningLeen betlaadDoor = bonnetje.getPersoon();
                Rekening betaaldBij = bonnetje.getWinkel();
                int bedrag = bonnetje.getBedrag();

                String message = "{1} heeft iets gekocht bij {2} voor {0}";
                Event e = rekening.newE(betlaadDoor, betaaldBij, bonnetje, message);

                betaaldBij.doRekeningBijDuo(betlaadDoor, bedrag, e);
                betlaadDoor.doKrijgtNogVanDuo(rekening, bedrag, e);
                rekening.doBudgetBijSingle(-bedrag, e);
            }
        }
    }

    private static Event besteedVia(RekeningLeenBudget r, RekeningLeen via, Rekening bij, int bedrag, Referentie referentie, String message) {
        Event e = r.newE(via, bij, referentie, message);
        besteedVia(r, via, bij, bedrag, e);
        return e;
    }

    private static void besteedVia(RekeningLeenBudget r, RekeningLeen via, Rekening bij, int bedrag, Event e) {
        if (bedrag < 0) {
            throw new IllegalArgumentException();
        }
        //betaal
        bij.doRekeningBijDuo(via, bedrag, e);
        via.doKrijgtNogVanDuo(r, bedrag, e);

        //betaal terug
        via.doRekeningBijDuo(r, bedrag, e);
        r.doKrijgtNogVanDuo(via, bedrag, e);
    }

    public List<Event> verwerkAfschriften(Set<Afschrift> afschriftenSet, Set<Bonnetje> bonnetjes, RekeningLeenBudget rekening) {
        ArrayList<Bonnetje> bonnetjes1 = new ArrayList<>(bonnetjes);
        Collections.sort(bonnetjes1, Bonnetje.getByDate());

        ArrayList<Afschrift> afschriften = new ArrayList<>(afschriftenSet);
        Collections.sort(afschriften, new Afschrift.CompByDate());
        
        List<Event> result = new LinkedList<>();

        for (Afschrift afschrift : afschriften) {
            if (afschrift.getDatum().isIn(periode)) {
                try {
                    Event e = verwerkAfschrift(afschrift, bonnetjes1, rekening);
                    result.add(e);
                } catch (Error e) {
                    throw new Error("At afschrift: " + afschrift, e);
                }
            }
        }
        return result;
    }

    private <RL extends List<Bonnetje> & RandomAccess> Event verwerkAfschrift(
            Afschrift afschrift,
            RL bonnetjes,
            RekeningLeenBudget rekening) {
        int bedrag = afschrift.getBedrag();
        Referentie referentie = afschrift;
        String message;
        Event e;

        switch (afschrift.getCode()) {
            case "BA": //betaalautomaat, gepind
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

                List<Bonnetje> candidates = HasBedrag.searchOn(
                        HasDatum.searchOn(bonnetjes,
                                afschrift.getDatum(),
                                Datum.COMP_BY_DAY),
                        afschrift.getBedrag());

                if (candidates.size() == 1) { //1 bonnetje gevonden die dag
                    Bonnetje bon = candidates.get(0);

                    //bonnetje found, kan maar 1 keer gevonden worden
                    bonnetjes.remove(bon);

                    //wel referentie toevoegen
                    throw new UnsupportedOperationException("Not yet...");
                    /*
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
                     return;*/
                } else if (candidates.isEmpty()) {

                    //geen bonnetje gevonden, gaan er wel vanuit dat het vooor de raaf was...
                    Winkel w = memory.winkels.getMede(afschrift.getMededeling());
                    if (w == null) {
                        w = new Winkel(afschrift.getMededeling());
                        memory.winkels.putMede(w, afschrift.getMededeling());
                    }

                    e = rekening.newE(w, referentie, "{0} kocht iets bij {1}");

                    rekening.doBudgetBijSingle(-bedrag, e);
                    rekening.doRekeningBijDuo(w, -bedrag, e);

                    //Logger.getLogger(Policy.class.getName()).log(Level.INFO, "Je moet het bonnetje zoeken van {0}", afschrift);
                    return e;
                } else { // > 1

                }
                throw new UnsupportedOperationException("Kan nog niets met meerde bonnetje sop dezelfde dag");
            case "OV":
                if (!afschrift.getMutatieSoort().equals("Overschrijving")) {
                    throw new Error("Overschrijving verwacht ipv: " + afschrift.getMutatieSoort());
                }
                //overboeking
                if (!afschrift.isAf()) {
                    RekeningLeen rl;

                    if (isMededelingRaRe(afschrift)) {
                        //zeker
                        rl = memory.personen.findRek(afschrift);
                        //throw new UnsupportedOperationException("Gebeuren twee dingen tegelijk, moet in Raafrekenng complexe functies maken en in history zetter per handeling, zie Word");
                        //ResultPrintStream.lijstje(p, rekening);
                    } else {
                        //niet zeker
                        rl = memory.personen.getRek(afschrift.getVanRekening());
                        if (rl != null) {
                            //persoon heeft iets erop geboekt, dus igg toevoegen aan rekening
                        } else {
                            //kan nieuw persoon zijn maar wel rare start dan...
                            rl = memory.personen.findRek(afschrift);
                            System.err.println("Niet zeker afschrift van " + rl + ": " + afschrift);
                        }
                    }

                    e = rekening.newE(rl, afschrift,
                            "{1} betaald schuld af aan {0}");
                    rekening.doKrijgtNogVanDuo(rl, -bedrag, e);
                    rekening.doRekeningBijDuo(rl, bedrag, e);

                    return e;
                }
                throw new UnknownError();
            case "GT":
                if (!afschrift.getMutatieSoort().equals("Internetbankieren")) {
                    throw new Error("Internetbankieren verwacht ipv: " + afschrift.getMutatieSoort());
                }
                //kan incasso zijn => handmatige overboeking

                if (afschrift.isAf()) {

                    Incasso incasso = memory.incassos.getRek(afschrift.getVanRekening());
                    if (incasso != null) {
                        //incasso detected

                        message = "{1} incassert bij {0}";
                        e = rekening.newE(incasso, referentie, message);

                        rekening.doBudgetBijSingle(-bedrag, e);
                        incasso.doRekeningBijDuo(rekening, bedrag, e);

                        return e;
                    }
                    Persoon p = memory.personen.findRek(afschrift);

                    //Object o = ResultPrintStream.lijst1(p, rekening);
                    //kan voorgeschoten zijn...
                    message = "{1} heeft voorgeschoten voor {0} bij een aankoop bij {2}";
                    if (afschrift.getMededeling().toLowerCase().contains("voorgeschoten")) {

                        //wasmachine voorgeschoten
                        Winkel marktplaats = memory.winkels.get(MARKTPLAATS);
                        if (marktplaats == null) {
                            marktplaats = new Winkel(MARKTPLAATS);
                        }

                        //ResultPrintStream.lijst2(o, p, rekening);
                        return besteedVia(rekening, p, marktplaats, bedrag,
                                referentie, message);
                    } else if (afschrift.getMededeling().contains(" Correctie Raafrekening")
                            && afschrift.getVan().contains("D.J. van den Brand")) {
                        message = "Eva gebeure: {0} geeft geld terug aan {1}";
                        e = rekening.newE(p, referentie, message);
                        p.doRekeningBijDuo(p, bedrag, e);
                        rekening.doKrijgtNogVanDuo(p, bedrag, e);
                        Logger.getLogger(Policy.class.getName()).log(Level.FINE, "Eva contant gebeure1");
                        return e;
                    } else if (afschrift.getMededeling().equals(" 20e te veel betaald voor de huis rekening")
                            && afschrift.getVan().equals("M.M.C. Tilburgs                 ")) {
                        message = "{0} betaald uit aan {1}";
                        e = rekening.newE(p, referentie, message);
                        p.doRekeningBijDuo(rekening, bedrag, e);
                        rekening.doKrijgtNogVanDuo(p, bedrag, e);
                        return e;
                    } else if (afschrift.getMededeling().equals(" Ik had 27.17 eigen geld gestort  en heb al 20 gepind, dit is rest")) {
                        Logger.getLogger(Policy.class.getName()).log(Level.FINE, "Eva contant gebeure2 (niet verrekend)");
                        //teruggave zonder de kas, meer een écht lening dus?
                        
                        message = "{0} betaald uit aan {1}: eva gebeuren";
                        e = rekening.newE(p, referentie, message);
                        
                        p.doRekeningBijDuo(rekening, bedrag, e);
                        rekening.doKrijgtNogVanDuo(p, bedrag, e);
                        return e;
                    } else if (afschrift.getMededeling().equals(" Toilet papier 11-9-12")) {
                        Winkel onbekendeWinkel = memory.winkels.get(WINKEL_UNKNOWN_NAAM);
                        if (onbekendeWinkel == null) {
                            onbekendeWinkel = new Winkel(WINKEL_UNKNOWN_NAAM);
                        }
                        message = "{0} betaald uit aan {1} die iets heeft gekocht bij {2}";

                        return besteedVia(rekening, p, onbekendeWinkel, bedrag, referentie, message);
                    }

                    //zal wss tg incasso zijn? unzeker
                    Logger.getLogger(Policy.class.getName()).log(Level.INFO, "GT niet 100% zeker");
                    incasso = memory.incassos.findRek(afschrift);
                    throw new UnknownError("Waar komt dit vandaan? " + incasso);
                } else {
                    //bijboeking
                    if (isMededelingRaRe(afschrift)) {
                        Persoon p = memory.personen.findRek(afschrift);
                        message = "{1} betaald handmatig schuld af aan {0}";
                        e = rekening.newE(p, referentie, message);

                        p.doKrijgtNogVanDuo(rekening, bedrag, e);

                        return e;
                    } else {
                        throw new UnsupportedOperationException();
                    }
                }
            case "DV":
                if (!afschrift.getMutatieSoort().equals("Diversen")) {
                    throw new Error("Diversen verwacht ipv: " + afschrift.getMutatieSoort());
                }
                if (afschrift.getMededeling().contains("productrekening")
                        && afschrift.getMededeling().contains("ING")) {
                    //defeniatly ing
                    Incasso ing = memory.incassos.get(ING_INCASSO_NAAM);
                    if (ing == null) {
                        String vanRek = afschrift.getVanRekening();
                        if(!vanRek.isEmpty()){
                            ing = new Incasso(ING_INCASSO_NAAM, vanRek);
                            memory.incassos.putRek(ing, vanRek);
                        }else{
                            ing = new Incasso(ING_INCASSO_NAAM, "");
                        }
                        memory.incassos.put(ing);
                    }

                    message = "{0} moet bankosten betalen aan {1}";
                    e = rekening.newE(ing, referentie, message);

                    rekening.doBudgetBijSingle(-bedrag, e);
                    ing.doRekeningBijDuo(rekening, bedrag, e);

                } else {
                    throw new Error();
                }
                return e;
            case "IC": //incasso
                if (!afschrift.getMutatieSoort().equals("Incasso")) {
                    throw new Error("Incasso verwacht ipv: " + afschrift.getMutatieSoort());
                }
                //incasso van UPC ofzo
                if (afschrift.getMededeling().trim().substring(0, 3)
                        .equalsIgnoreCase("upc")
                        || afschrift.getMededeling()
                        .contains("UPC Nederland B.V.")) {
                    Incasso incasso = memory.incassos.
                            findRek(UPC_INCASSO_NAAM, afschrift.getVanRekening());
                    message = "{0} krijgt een incasso van {1}";
                    e = rekening.newE(incasso, referentie, message);

                    rekening.doBudgetBijSingle(-bedrag, e);
                    incasso.doRekeningBijDuo(rekening, bedrag, e);

                    return e;
                } else {
                    throw new UnsupportedOperationException();
                }
            case "PO":
                if (!afschrift.getMutatieSoort().equals("Periodieke overschrijving")) {
                    throw new Error("Periodieke overschrijving verwacht ipv: " + afschrift.getMutatieSoort());
                }
                if (isMededelingRaRe(afschrift)) {
                    Persoon p = memory.personen.findRek(afschrift);
                    message = "{0} krijgt periodieke overschrijving van {1}";
                    e = rekening.newE(p, referentie, message);

                    rekening.doBudgetBijSingle(bedrag, e);
                    p.doKrijgtNogVanDuo(rekening, bedrag, e);

                    return e;
                } else {
                    throw new UnsupportedOperationException();
                }
            case "GM":
                if (!afschrift.getMutatieSoort().equals("Geldautomaat")) {
                    throw new Error("Geldautomaat verwacht ipv: " + afschrift.getMutatieSoort());
                }
                //naar contant
                if (afschrift.getBedrag() % 500 != 0) {
                    throw new Error("bedrag: " + afschrift.getBedrag() + " kan niet gepind zijn...");
                }
                //OLD: verrekMetRekening.verwerk(verrekMetRekening.getContant(), afschrift);
                throw new Error("Contant nog niet implemented2");
            default:
                throw new Error("Code: " + afschrift.getCode() + " niet bekend");
        }
        //if(true)
        //throw new UnknownError();
    }

    private boolean isMededelingRaRe(Afschrift afschrift) {
        String meld = afschrift.getMededeling().toLowerCase();
        boolean isRaRa = meld.contains("huisrekening")
                || meld.contains("huis rekening")
                || meld.contains("huiskosten")
                || meld.contains("maandkosten de raaf");
        if (!isRaRa) {
            return afschrift.getVan().equalsIgnoreCase("B.F.W. van Vugt")
                    && (afschrift.getMededeling().equalsIgnoreCase(" HOMO"))
                    || afschrift.getMededeling().toUpperCase().contains("SCHOOIER");
        }
        return isRaRa;
    }

    public void verrekenAfBetaaldVias(Set<BetaaldVia> betaaldVias, RekeningLeenBudget raafRekening) {
        for (BetaaldVia betaaldVia : betaaldVias) {
            if (betaaldVia.getDatum().isIn(periode)) {
                verrekenAfBetaaldVia(betaaldVia, raafRekening);
            }
        }
    }

    private void verrekenAfBetaaldVia(BetaaldVia betaaldVia, RekeningLeenBudget raafRekening) {
        RekeningLeen onderwerp = betaaldVia.getOnderwerp();
        Persoon via = betaaldVia.getVia();
        int bedrag = betaaldVia.getBedrag();
        if (bedrag < 0) {
            throw new UnsupportedOperationException("Bedrag is kleiner dan 0 :(");
        }
        String message = "{1} wil bedrag afbetalen aan {0} maar geeft geld aan {2}: " + betaaldVia.getRede();
        Event e = raafRekening.newE(onderwerp, via, betaaldVia, message);

        via.doRekeningBijDuo(onderwerp, bedrag, e);
        raafRekening.doKrijgtNogVanDuo(onderwerp, -bedrag, e);
        raafRekening.doKrijgtNogVanDuo(via, bedrag, e);
    }

    public void verwerkContants(Set<Afschrift> afschriften, DoubleContantRecordSet contantRecords, RekeningLeenBudget raafRekening) {
        for (Afschrift afschrift : new ArrayList<>(afschriften)) {
            if (afschrift.getDatum().isIn(periode)) {
                if (afschrift.getCode().equals("ST")) {
                    if (!afschrift.getMutatieSoort().equals("Storting")) {
                        throw new Error("Storting verwacht ipv: " + afschrift.getMutatieSoort());
                    }
                    if (afschrift.isAf()) {
                        throw new Error("Assert: storting is altijd geld erbij");
                    }
                    verwerkStorting(afschrift, contantRecords.stortingen(), raafRekening);
                    afschriften.remove(afschrift);
                } else if (afschrift.getCode().equals("GM")) {
                    if (!afschrift.getMutatieSoort().equals("Geldautomaat")) {
                        throw new Error("Geldautomaat verwacht ipv: " + afschrift.getMutatieSoort());
                    }
                    if (!afschrift.isAf()) {
                        throw new Error("Assert: pinnen is altijd geld eraf");
                    }
                    verwerkPin(afschrift, contantRecords.pins(), raafRekening);
                    afschriften.remove(afschrift);
                }
            }
        }
    }

    private static List<ContantRecord> search(Set<ContantRecord> records, Afschrift a) {
        Datum d = a.getDatum();
        List<ContantRecord> candidates = HasDatum.search(records, d, Datum.COMP_BY_DAY);

        int bedrag = a.getBedrag();
        candidates = HasBedrag.search(candidates, bedrag);

        if (a.isAf()) {
            //van rekening af dus:
            for (ContantRecord cr : new ArrayList<>(candidates)) {
                if(!cr.isPin()){
                    candidates.remove(cr);
                }
            }
        }else{
            //bij rekening erbij dus:
            for (ContantRecord cr : new ArrayList<>(candidates)) {
                if(!cr.isStorting()){
                    candidates.remove(cr);
                }
            }
        }
        return candidates;
    }

    private void verwerkStorting(Afschrift afschrift, Set<ContantRecord> contantRecords, RekeningLeenBudget raafRekening) {
        int bedrag = afschrift.getBedrag();

        List<ContantRecord> candidates = search(contantRecords, afschrift);

        if (candidates.size() != 1) {
            throw new Error("Storting: " + afschrift + " niet gevonden!");
        }
        ContantRecord cr = candidates.get(0);
        cr.setAfschrift(afschrift);
        Persoon eigenaar = cr.getEigenaar();

        Event e = raafRekening.newE(eigenaar, cr, "{1} heeft geld gestort bij {0}");
        contantRecords.remove(cr);

        eigenaar.doKrijgtNogVanDuo(raafRekening, bedrag, e);
        raafRekening.doRekeningBijDuo(eigenaar, bedrag, e);
    }

    private void verwerkPin(Afschrift afschrift, Set<ContantRecord> contantRecords, RekeningLeenBudget raafRekening) {
        int bedrag = afschrift.getBedrag();
        
        List<ContantRecord> candidates = search(contantRecords, afschrift);
        
        if(candidates.size() != 1){
            throw new Error("Pin: "+afschrift+" niet gevonden!");
        }
        ContantRecord cr = candidates.get(0);
        cr.setAfschrift(afschrift);
        Persoon eigenaar = cr.getEigenaar();
        
        Event e = raafRekening.newE(eigenaar, cr, "{1} heeft geld gepind bij {0}");
        contantRecords.remove(cr);
        
        raafRekening.doKrijgtNogVanDuo(eigenaar, bedrag, e);
        eigenaar.doRekeningBijDuo(raafRekening, bedrag, e);
    }
}
