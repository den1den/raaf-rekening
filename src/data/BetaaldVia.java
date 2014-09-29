/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import geld.Referentie;
import geld.rekeningen.Rekening;
import geld.rekeningen.RekeningLeen;
import tijd.Datum;

/**
 *
 * @author Dennis
 */
public class BetaaldVia extends Referentie {

    private final Datum datum;
    private final RekeningLeen onderwerp;
    private final int bedrag;
    private final Persoon via;
    private final String rede;

    public BetaaldVia(Datum datum, RekeningLeen onderwerp, int bedrag, Persoon via, String rede) {
        if (datum == null || onderwerp == null || bedrag < 0 || via == null || rede == null || rede.isEmpty() || rede.equals("\"")) {
            throw new IllegalArgumentException();
        }
        this.datum = datum;
        this.onderwerp = onderwerp;
        this.bedrag = bedrag;
        this.via = via;
        this.rede = rede;
    }

    @Override
    public Datum getDatum() {
        return datum;
    }

    @Override
    public String toString() {
        return getDatum() + " heeft " + getOnderwerp() + " €" + getBedrag() / 100 + "." + getBedrag() % 100 + " betaald via " + getVia() + " omdat:" + getRede();
    }

    /**
     * @return the onderwerp
     */
    public RekeningLeen getOnderwerp() {
        return onderwerp;
    }

    /**
     * @return the bedrag
     */
    public int getBedrag() {
        return bedrag;
    }

    /**
     * @return the via
     */
    public Persoon getVia() {
        return via;
    }

    /**
     * @return the rede
     */
    public String getRede() {
        return rede;
    }

}
