/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld;

import data.Afschrift;
import data.types.HasNaam;
import java.util.List;

/**
 *
 * @author Dennis
 */
public interface RekeningHouderInterface extends HasNaam {

    @Override
    public String getNaam();

    /**
     * Als een bedrag nog moet worden betaald
     *
     * @param aan aan desbetreffend persoon
     * @param bedrag >= 0
     * @param referentie waarom
     */
    void betaaldNog(RekeningHouder aan, int bedrag, Referentie referentie);

    /**
     * Als een bedrag nog moet worden ontvangen
     *
     * @param aan
     * @param bedrag >= 0
     * @param referentie
     */
    void krijgtNog(RekeningHouder aan, int bedrag, Referentie referentie);

    int getBetaaldNog(RekeningHouderInterface aan);

    /**
     * Een bedrag word betaald
     *
     * @param aan
     * @param bedrag
     * @param referentie
     */
    void betaald(RekeningHouder aan, int bedrag, Referentie referentie);

    void krijgt(RekeningHouder aan, int bedrag, Referentie referentie);

    int getBetaald(RekeningHouderInterface aan);

    /**
     * @param aan
     * @return betaald - moetNogBetalen
     */
    int getSaldo(RekeningHouderInterface aan);

    /**
     * @return sum of getSaldo for all Rekeningen
     */
    int getSaldo();
    
    public List<TransactiesRecord> getTransacties(RekeningHouderInterface rhc);
}
