/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.diplay.gui.graph;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import javax.swing.JComponent;

/**
 * x and y are in pixels to current frame
 *
 * @author Dennis
 */
public class Graph extends JComponent implements MouseMotionListener {

    private String titel;
    private boolean legenda = false;

    private Axis xAxis;
    private Axis yAxis;

    private DataSet[] dataSets;

    //ordered point[dataset][id]
    private final Range range = new Range();
    private double[][] pointX = null;
    private double[][] pointY = null;

    private Color backgroundColor = Color.white;
    private Color AxisColor = Color.black;
    private Color AccentColor = Color.gray;

    private int margin = 2; //in pixels

    public Graph(DataSet dataSet) {
        this(
                new DataSet[]{dataSet}
        );
    }

    public Graph(DataSet[] dataSets) {
        this(
                "Geen titel",
                new Axis(),
                new Axis(),
                dataSets
        );
    }

    public Graph(String titel, String xAsTitel, String yAsTitel, DataSet[] dataSets) {
        this(
                titel,
                new Axis(xAsTitel, true),
                new Axis(yAsTitel, true),
                dataSets
        );
    }

    private Graph(String titel, Axis xAxis, Axis yAxis, DataSet[] dataSets) {
        this.titel = titel;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.dataSets = dataSets;
        calcRange();
    }

    @Override
    public void paint(Graphics g) {
        Rectangle clip = g.getClipBounds();
        FontMetrics fm = g.getFontMetrics();

        //draw background
        g.setColor(backgroundColor);
        g.fillRect(clip.x, clip.y, clip.width - 1, clip.height - 1);

        //draw canvas
        int width = clip.width - 1 - margin * 2 - 2;
        int height = clip.height - 1 - margin * 2 - 2;
        g.translate(clip.x + margin + 1, clip.y + margin + 1);
        
        g.setColor(AccentColor);
        g.drawRect(-1, -1, width + 1, height + 1);
        
        //calculate scale
        double pixelPerX = (double)(width) / range.domain();
        double pixelPerY = (double)(height) / range.range();
        
        //draw axis
        if(xAxis.draw0){
        final int XaxisHeight;
        if (range.yMin >= 0) {
            XaxisHeight = height;
        } else if (range.yMax <= 0) {
            XaxisHeight = 0;
        } else {
            XaxisHeight = (int) (range.yMax * pixelPerY);
        }
        g.drawLine(0, XaxisHeight, width, XaxisHeight);
        }

        if(yAxis.draw0){
        final int YaxisWidth;
        if (range.xMin >= 0) {
            YaxisWidth = 0;
        } else if (range.xMax <= 0) {
            YaxisWidth = width;
        } else {
            YaxisWidth = (int) (-range.xMin * pixelPerX);
        }
        g.drawLine(YaxisWidth, 0, YaxisWidth, height);
        }
        
        //draw Title
        g.drawString(titel, width - fm.stringWidth(titel), fm.getHeight());

        //draw datasets
        for (DataSet dataSet : dataSets) {
            g.setColor(dataSet.color);
            dataSet.renderer.initPaint();
            
            for (int i = 0; i < dataSet.points.length; i++) {
                double x = (dataSet.x[i] - dataSet.range.xMin);
                double y = (dataSet.y[i] - dataSet.range.yMin);
                dataSet.renderer.paint(g, (int) (x * pixelPerX), (int) (y * pixelPerY));
            }
        }
    }

    private void initpointXY() {
        pointX = new double[dataSets.length][];
        pointY = new double[dataSets.length][];

        double xStart = range.xMin;
        double xSize = range.xMax - range.xMin;
        double yStart = range.yMin;
        double ySize = range.yMax - range.yMin;
        int i = 0;
        for (DataSet dataSet : dataSets) {
            double[] pointXi = new double[dataSet.size()];
            double[] pointYi = new double[dataSet.size()];
            for (int j = 0; j < dataSet.size(); j++) {
                double x = dataSet.x[j];
                double y = dataSet.y[j];
                pointXi[j] = (x - xStart) / xSize;
                pointYi[j] = (y - yStart) / ySize;
            }
            this.pointX[i] = pointXi;
            this.pointY[i] = pointYi;
            i++;
        }
    }

    private void calcRange() {
        boolean change = false;
        for (DataSet dataSet : dataSets) {
            change = range.maximize(dataSet.range);
        }
        if (!range.isSet()) {
            range.set01();
        }
        if (change) {
            initpointXY();
        }
    }

    public void setLegenda(boolean legenda) {
        this.legenda = legenda;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
