/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld.rekeningen;

public class RekeningLeen extends Rekening {

    private final SomMap<RekeningLeen> krijgtNogVan;

    public RekeningLeen(String naam) {
        this(naam, false);
    }

    public RekeningLeen(String naam, boolean contant) {
        super(naam, contant);
        krijgtNogVan = new SomMap<>(10, new VerschuldDigdParams());
    }
    
    protected void doKrijgtNogVanSingle(RekeningLeen lr, int bedrag, Event e) {
        this.krijgtNogVan.add(lr, bedrag, e);
    }
    
    protected void doKrijgtNogVanDuo(RekeningLeen lr, int bedrag, Event e) {
        this.doKrijgtNogVanSingle(lr, bedrag, e);
        lr.doKrijgtNogVanSingle(this, -bedrag, e);
    }

    private class VerschuldDigdParams extends SomMap.SomMapParams<RekeningLeen> {

        @Override
        String getBeschrijvingTotaal() {
            return "Totaal verschuldigd aan " + RekeningLeen.this.getNaam();
        }

        @Override
        String getBeschrijvingSubSom(RekeningLeen van) {
            return van.getNaam() + " verschuldigd aan " + RekeningLeen.this.getNaam();
        }

    }
}
