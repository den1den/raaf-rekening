/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld;

import data.Bonnetje;
import data.types.HasNaam;
import tijd.Datum;

/**
 *
 * @author Dennis
 */
public interface RekeningHouder extends HasNaam, Rekening {

    @Override
    public String getNaam();

    public TRecord moetBetalenAan(Bonnetje bonnetje);

    public TRecord moetBetalen(RekeningHouder rekeningHouder, Bonnetje bonnetje);

    public TRecord moetBetalen(RekeningHouder rekeningHouder, int bedrag, Datum date, Referentie referentie);

    public TRecord krijgtVan(RekeningHouder rekeningHouder, int bedrag, Datum date, Referentie referentie);

    public TRecord betaald(RekeningHouder naar, int bedrag, Datum datum, Referentie referentie);
}
