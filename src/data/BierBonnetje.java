/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package data;

import data.types.HasDate;
import tijd.Datum;

/**
 *
 * @author Dennis
 */
public class BierBonnetje implements HasDate{
    String merk;
    int kratten;
    int totaalPrijs;
    Datum datum;
    Winkel winkel;

    public BierBonnetje(String merk, int kratten, int totaalPrijs, Datum datum, Winkel winkel) {
        this.merk = merk;
        this.kratten = kratten;
        this.totaalPrijs = totaalPrijs;
        this.datum = datum;
        this.winkel = winkel;
    }
    
    @Override
    public Datum getDate() {
        return datum;
    }
}
