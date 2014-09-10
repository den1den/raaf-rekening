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
public interface RekeningHouderContantInterface extends RekeningHouderInterface{
    RekeningHouder getContant();

    public void pin(int bedrag);

    public void stort(int bedrag);
    
    public int getRekeningSaldo(RekeningHouderInterface rh);
    public int getRekeningSaldo();
    public int getContantSaldo(RekeningHouderInterface rh);
    public int getContantSaldo();
    
}
