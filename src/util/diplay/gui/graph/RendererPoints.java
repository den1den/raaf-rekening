/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.diplay.gui.graph;

import java.awt.Graphics;

public class RendererPoints extends Renderer {

    public RendererPoints() {
        super(0);
    }

    /**
     * @param radius 
     */
    public RendererPoints(int radius) {
        super(radius);
        
    }

    @Override
    void paint(Graphics g, int x, int y) {
        g.drawOval(x - size, y - size, size * 2, size * 2);
    }

    @Override
    void setSize(int size) {
        size /= 2;
        if(size <= 0){
            size = 1;
        }
        this.size = size;
    }
}
