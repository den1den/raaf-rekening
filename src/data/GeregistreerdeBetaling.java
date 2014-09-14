/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import data.types.HasTime;
import geld.Referentie;
import tijd.Time;

/**
 *
 * @author Dennis
 */
public class GeregistreerdeBetaling implements Referentie, HasTime {

    final private Referentie referentie;
    final private Time time;

    public GeregistreerdeBetaling(Referentie referentie, Time time) {
        this.referentie = referentie;
        this.time = time;
    }

    public static GeregistreerdeBetaling getContant(Time time){
        return new GeregistreerdeBetaling(Referentie.CONTANT, time);
    }

    @Override
    public Time getTime() {
        return time;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName()+": "+this.referentie;
    }

    public static enum Referentie{
        CONTANT
    }
}
