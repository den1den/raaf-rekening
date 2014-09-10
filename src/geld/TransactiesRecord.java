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
public class TransactiesRecord {

        final private Transactie transactie;
        final private RekeningHouderInterface van;
        final private RekeningHouderInterface naar;

        public TransactiesRecord(Transactie transactie, RekeningHouderInterface van, RekeningHouderInterface naar) {
            this.transactie = transactie;
            this.van = van;
            this.naar = naar;
        }

        public Transactie getTransactie() {
            return transactie;
        }

    }