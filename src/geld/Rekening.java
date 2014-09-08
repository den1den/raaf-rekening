/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld;

import java.util.List;
import tijd.Datum;

/**
 *
 * @author Dennis
 */
public interface Rekening {

    void putSchuld(RekeningHouder van, Transactie t);

    void bij(Datum datum, int bedrag, Referentie referentie);

    void bij(Transactie t);

    void af(Transactie t);

    void af(Datum datum, int bedrag, Referentie referentie);

    int getSaldo();

    List<Transactie> getTransacties();

    List<Transactie> getTransactiesEnSchulden();
}
