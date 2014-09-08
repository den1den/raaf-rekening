/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util.diplay.gui.graph;

/**
 *
 * @author Dennis
 */
class Axis {
    String name;
    boolean draw0;
    
    long min = 0;
    long max = 0;

    public Axis() {
        this("Geen naam", true);
    }
    
    public Axis(String name, boolean draw0) {
        this.name = name;
        this.draw0 = draw0;
    }

    public void setMinMax(long min, long max) {
        this.min = min;
        this.max = max;
    } 
}
