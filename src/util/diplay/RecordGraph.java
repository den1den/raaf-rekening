/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.diplay;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import util.diplay.gui.graph.DataSet;
import util.diplay.gui.graph.Graph;
import util.diplay.gui.graph.GraphPanel;

/**
 *
 * @author Dennis
 */
public class RecordGraph extends MouseAdapter {

    final private JFrame frame;

    public RecordGraph(Graph... g) {
        String title = getClass().getSimpleName() + " of " + g.length;
        frame = new JFrame(title);
        frame.setLayout(new GridLayout());

        for (int i = 0; i < g.length; i++) {
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new BorderLayout());
            jPanel.add(new GraphPanel(g[i]));
            frame.getContentPane().add(jPanel);
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);
        frame.addMouseListener(this);
        frame.setVisible(true);
    }

    synchronized void wait4M() {
        try {
            wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(RecordGraph.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        frame.setVisible(false);
        synchronized (this) {
            notify();
        }
    }
}
