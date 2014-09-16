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

    final String naam;
    private final Sum kas = new Kas();
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
     * this krijgt nog wat geld van iemand budget++ this moetKrijgenVan
     * gebruiker
     *
     * @param bedrag >= 0
     * @param gebruiker degene die THIS wat veerschuldigd is
     * @param referentie
     */
    public void krijgtNog(int bedrag, LeenRekening gebruiker, Referentie referentie) {
        String message = getNaam() + " krijgt nog van " + gebruiker.getNaam();

        Event e = newE(gebruiker, referentie, message);

        doKrijgtNog(gebruiker, bedrag, e);
    }

    void doKrijgtNog(LeenRekening lr, int bedrag, Event e) {
        kas.bij(bedrag, e);
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
    public void betaaldDoor(LeenRekening voorschieter, int bedrag, Referentie referentie) {
        String message = voorschieter.getNaam() + " heeft iets betaald wat eigenelijk " + getNaam() + " zou moeten";
        Event e = newE(voorschieter, referentie, message);
        doBetaaldDoor(voorschieter, bedrag, e);
    }

    protected void doBetaaldDoor(LeenRekening lr, int bedrag, Event e) {
        kas.af(bedrag, e);
        lr.doMoetKrijgenVan(this, bedrag, e);
    }

    /**
     * betaald schuld af. kas komt iets bij. {van} krijgt minder schuld
     *
     * @param van
     * @param bedrag
     * @param referentie
     */
    public void plusContributie(LeenRekening van, int bedrag, Referentie referentie) {
        if (bedrag < 0) {
            throw new IllegalArgumentException();
        }
        String message = van.getNaam() + " betaald zijn schuld af aan " + getNaam();
        Event e = newE(van, referentie, message);
        doPlusContri(van, bedrag, e);
    }

    protected void doPlusContri(LeenRekening lr, int bedrag, Event e) {
//budget +
        this.kas.bij(bedrag, e);
        System.err.println("TODO2: r.af() zodat je ziet heveel er word betaald. Persoon>RaafRekening");
//van komt in de plus te staan bij this
//this moet nog betalen aan van
        lr.doMoetKrijgenVan(this, bedrag, e);
    }

    /**
     * Iets gekocht bij. Er gaat iets uit de kas en bij degene waar het is
     * gekocht gaat iets bij.
     *
     * @param bij
     * @param bedrag
     * @param referentie
     */
    public void gekocht(Rekening bij, int bedrag, Referentie referentie) {
        if (bedrag < 0) {
            throw new IllegalArgumentException();
        }
        String message = getNaam() + " heeft iets gekocht bij " + bij.getNaam();
        Event e = newE(bij, referentie, message);
        doGekocht(bij, bedrag, e);
    }

    protected void doGekocht(Rekening bij, int bedrag, Event e) {
        this.kas.af(bedrag, e);
        System.err.println("TODO3: bij rekening 'bij' moet er geld bij");
    }

    /**
     * Betaald voor een dienst aan (Door automatische incasso b.v.). Er gaat
     * iets uit de kas en bij degene waar het is gekocht gaat iets bij.
     *
     * @param bedrag >0
     * @param aan
     * @param referentie
     */
    public void betaald(int bedrag, Rekening aan, Referentie referentie) {
        String message = getNaam() + " heeft betaald voor een dienst bij " + aan.getNaam();
        Event e = newE(aan, referentie, message);
        doGekocht(aan, bedrag, e);
    }

    /**
     * De raaf betaald uit. De kas gaat iets uit. De schuld word groter.
     *
     * @param bedrag
     * @param aan
     * @param referentie
     */
    public void terrugGave(int bedrag, LeenRekening aan, Referentie referentie) {
        String message = getNaam() + " betaald uit aan " + aan.getNaam();
        Event e = newE(aan, referentie, message);

    }

    protected void doTerrugGave(LeenRekening lr, int bedrag, Event e) {
        //er gaat wat uit de kas
        kas.af(bedrag, e);
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
