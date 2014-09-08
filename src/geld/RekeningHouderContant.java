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
public interface RekeningHouderContant extends RekeningHouder {
    RekeningHouder getContant();
    public void pin(Transactie t);

    public void stort(Transactie t);

    public int getTotaal();
}
