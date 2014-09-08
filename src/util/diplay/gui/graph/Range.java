/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util.diplay.gui.graph;

import java.awt.Rectangle;

/**
 *
 * @author Dennis
 */
class Range {
    double xMin, xMax, yMin, yMax;

    Range() {
        xMin = Double.POSITIVE_INFINITY;
        yMin = Double.POSITIVE_INFINITY;
        xMax = Double.NEGATIVE_INFINITY;
        yMax = Double.NEGATIVE_INFINITY;
    }

    Range(double xMin, double xMax, double yMin, double yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }
    
    void reset(){
        xMin = Double.POSITIVE_INFINITY;
        yMin = Double.POSITIVE_INFINITY;
        xMax = Double.NEGATIVE_INFINITY;
        yMax = Double.NEGATIVE_INFINITY;
    }
    
    boolean maximize(Range r){
        boolean change = false;
        if(r.xMin < xMin){
            change = true;
            xMin = r.xMin;
        }
        if(r.yMin < yMin){
            change = true;
            yMin = r.yMin;
        }
        if(r.xMax > xMax){
            change = true;
            xMax = r.xMax;
        }
        if(r.yMax > yMax){
            change = true;
            yMax = r.yMax;
        }
        return change;
    }
    
    void miximizaPoint(double x, double y){
        if(x < xMin){
            xMin = x;
        }
        if(y < yMin){
            yMin = y;
        }
        if(x > xMax){
            xMax = x;
        }
        if(y > yMax){
            yMax = y;
        }
    }
    
    boolean isSet(){
        return valid(xMin) && valid(xMax) && valid(yMin) && valid(yMax);
    }
    
    static private boolean valid(double l){
        return Double.isFinite(l);
    }
    
    void set01(){
        xMin = 0;
        xMax = 1;
        yMin = 0;
        yMax = 1;
    }

    double range() {
        return yMax - yMin;
    }

    double domain() {
        return xMax -xMin;
    }

    @Override
    public String toString() {
        return new Rectangle.Double(
                xMin,
                yMin,
                xMax-xMin,
                yMax-yMin).toString();
    }
}
