/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.diplay.gui.graph;

import java.awt.Graphics;

/**
 *
 * @author Dennis
 */
public abstract class Renderer {

    int size;

    public Renderer(int size) {
        this.size = size;
    }

    void setSize(int size) {
        this.size = size;
    }

    void initPaint(){
    }

    abstract void paint(Graphics g, int x, int y);

}
