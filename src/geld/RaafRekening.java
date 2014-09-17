/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld;

/**
 *
 * @author Dennis
 */
public class RaafRekening extends LeenRekening implements HasContant {

    private final String naam;
    private final Sum opRekening = new Kas();
    private final Sum contant = new Contant();

    public RaafRekening(final String naam) {
        this(naam, 10);
    }

    public RaafRekening(final String naam, final int capacity) {
        this.naam = naam;
    }

    @Override
    public String getNaam() {
        return naam;
    }

    /**
     * Doneer een bedrag aan deze rekening. opRekening++
     *
     * @param r kan null zijn
     * @param bedrag
     * @param referentie
     */
    public void donatie(Rekening r, int bedrag, Referentie referentie) {
        if (bedrag < 0) {
            throw new IllegalArgumentException();
        }
        String message = r + " heeft " + bedrag + " gedoneerd";
        Event e = newE(r, referentie, message);
        doDonatie(r, bedrag, e);
    }

    protected void doDonatie(Rekening r, int bedrag, Event e) {
        opRekening.bij(bedrag, e);
        if (r != null) {
            System.err.println(r + " heeft gedoneerd maar merkt er zelf niets "
                    + "van");
        }
    }

    /**
     * Iets gekocht bij. opRekening-- en BINNEKORT:(bij degene waar het is
     * gekocht gaat iets bij). Geen verandering in schuld
     *
     * @param bij
     * @param bedrag
     * @param referentie
     */
    public void besteed(Rekening bij, int bedrag, Referentie referentie) {
        if (bedrag < 0) {
            throw new IllegalArgumentException();
        }
        String message = bij + " heeft gratis geld gekregen";
        Event e = newE(bij, referentie, message);
        doBesteed(bij, bedrag, e);
    }

    protected void doBesteed(Rekening r, int bedrag, Event e) {
        opRekening.af(bedrag, e);
        if (r != null) {
            System.err.println(r + " heeft geld gekregen maar merkt hier zelf "
                    + "niets van");
        }
    }

    /**
     * Iemand geeft geld aan <B>this</B> zodat de schuld minder word.
     * opRekening++. <b>van</b> hoeft minder te betalen aan <b>this</b>.
     * <b>this</b> moet meer betalen aan <b>van</b>.
     *
     * @param van
     * @param bedrag
     * @param referentie
     */
    public void krijgtAfbetaling(LeenRekening van, int bedrag, Referentie referentie) {
        if (bedrag < 0) {
            throw new IllegalArgumentException();
        }
        String message = van.getNaam() + " betaald zijn schuld af";
        Event e = newE(van, referentie, message);
        doKrijgtAfbetaling(van, bedrag, e);
    }

    protected void doKrijgtAfbetaling(LeenRekening r, int bedrag, Event e) {
        doDonatie(r, bedrag, e);
        doMoetBetalenAan(r, bedrag, e);
    }

    /**
     * <b>this</b> betaald uit. opRekening--. <b>aan</b> moet meer terugbetalen
     * aan <b>this</b>. <b>this</b> hoeft minder te betalen aan <b>van</b>.
     *
     * @param aan
     * @param bedrag
     * @param referentie
     */
    public void betaaldUit(LeenRekening aan, int bedrag, Referentie referentie) {
        if (bedrag < 0) {
            throw new IllegalArgumentException();
        }
        String message = aan.getNaam() + " krijgt geld terug";
        Event e = newE(aan, referentie, message);
        doBetaaldUit(aan, bedrag, e);
    }

    protected void doBetaaldUit(LeenRekening r, int bedrag, Event e) {
        doBesteed(r, bedrag, e);
        doMoetKrijgenVan(r, bedrag, e);
    }

    {
        System.out.println("tot hier");
    }

    /**
     * this krijgt nog wat geld van iemand budget++ this moetKrijgenVan
     * gebruiker
     *
     * @param bedrag >= 0
     * @param gebruiker degene die THIS wat veerschuldigd is
     * @param referentie
     */
    @Deprecated
    public void krijgtNog(int bedrag, LeenRekening gebruiker, Referentie referentie) {
        String message = getNaam() + " krijgt nog van " + gebruiker.getNaam();

        Event e = newE(gebruiker, referentie, message);

        doKrijgtNog(gebruiker, bedrag, e);
    }

    @Deprecated
    protected void doKrijgtNog(LeenRekening lr, int bedrag, Event e) {
        //opRekening.bij(bedrag, e);, krijgt nog dus heeft nog niet
        doMoetKrijgenVan(lr, bedrag, e);
    }

    /**
     * Als iets voorgeschoten is. (budget--) en (voorschieter moetKrijgenVan
     * this)
     *
     * @param voorschieter
     * @param bedrag
     * @param referentie
     */
    @Deprecated
    public void betaaldDoor(LeenRekening voorschieter, int bedrag, Referentie referentie) {
        String message = voorschieter.getNaam() + " heeft iets betaald wat eigenelijk " + getNaam() + " zou moeten";
        Event e = newE(voorschieter, referentie, message);
        doBetaaldDoor(voorschieter, bedrag, e);
    }

    @Deprecated
    protected void doBetaaldDoor(LeenRekening lr, int bedrag, Event e) {
        opRekening.af(bedrag, e);
        lr.doMoetKrijgenVan(this, bedrag, e);
    }

    /**
     * betaald schuld af. kas komt iets bij. {van} krijgt minder schuld
     *
     * @param van
     * @param bedrag
     * @param referentie
     */
    @Deprecated
    public void plusContributie(LeenRekening van, int bedrag, Referentie referentie) {
        if (bedrag < 0) {
            throw new IllegalArgumentException();
        }
        String message = van.getNaam() + " betaald zijn schuld af aan " + getNaam();
        Event e = newE(van, referentie, message);
        doPlusContri(van, bedrag, e);
    }

    @Deprecated
    protected void doPlusContri(LeenRekening lr, int bedrag, Event e) {
//budget +
        this.opRekening.bij(bedrag, e);
        System.err.println("TODO2: r.af() zodat je ziet heveel er word betaald. Persoon>RaafRekening");
//van komt in de plus te staan bij this
//this moet nog betalen aan van
        lr.doMoetKrijgenVan(this, bedrag, e);
    }

    @Deprecated
    protected void doGeefAan(Rekening bij, int bedrag, Event e) {
        this.opRekening.af(bedrag, e);
        if (bij != null) {
            System.err.println("TODO3: " + bij + " heeft geld gerkegen maar merkt er niets van");
        }
    }

    /**
     * Betaald voor een dienst aan (Door automatische incasso b.v.). Er gaat
     * iets uit de kas en bij degene waar het is gekocht gaat iets bij.
     *
     * @param bedrag >0
     * @param aan
     * @param referentie
     */
    @Deprecated
    public void betaald(int bedrag, Rekening aan, Referentie referentie) {
        String message = getNaam() + " heeft betaald voor een dienst bij " + aan.getNaam();
        Event e = newE(aan, referentie, message);
        doGeefAan(aan, bedrag, e);
    }

    /**
     * De raaf betaald uit. opRekekning--, Raaf krijgt geld van b, B moet terug
     * betalen aan Raaf
     *
     * @param bedrag
     * @param b
     * @param referentie
     */
    @Deprecated
    public void terrugGave(int bedrag, LeenRekening b, Referentie referentie) {
        String message = getNaam() + " betaald uit aan " + b.getNaam();
        Event e = newE(b, referentie, message);
        doTerrugGave(b, bedrag, e);
    }

    @Deprecated
    protected void doTerrugGave(LeenRekening lr, int bedrag, Event e) {
        //er gaat wat uit de kas
        doGeefAan(lr, bedrag, e);
        //aan krijgt minder schuld 

        //aan -
        //this +
        doMoetKrijgenVan(lr, bedrag, e);
    }

    private class Kas extends Sum {

        @Override
        public String naam() {
            return "kas van " + RaafRekening.this.getNaam();
        }

    }

    private class Contant extends Sum {

        @Override
        public String naam() {
            return "contant van " + RaafRekening.this.getNaam();
        }
    }
}
