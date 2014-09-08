/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.diplay.gui.graph;

import java.awt.Graphics;

public class RendererLines extends Renderer {

    int xLast;
    int yLast;
    boolean isSet;

    public RendererLines() {
        super(1);
    }

    @Override
    void paint(Graphics g, int x, int y) {
        if (isSet) {
            g.drawLine(xLast, yLast, x, y);
        } else {
            isSet = true;
        }
            xLast = x;
            yLast = y;
        
    }

    @Override
    void initPaint() {
        isSet = false;
    }
}
