/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.diplay;

import data.Afschrift;
import geld.Referentie;
import geld.RekeningHouder;
import geld.RekeningHouderInterface;
import geld.Transactie;
import geld.Transactie.Record;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.MyTxtTable;
import util.MyTxtTable.MyTxtTableHeader;
import util.SchuldenComparator;
import util.diplay.gui.graph.DataSet;
import util.diplay.gui.graph.Graph;

public class ResultPrintStream extends Result {

    final int version;
    final PrintStream stream;

    public ResultPrintStream(int version) {
        this(version, new SlowPrintStream(System.out));
    }

    public ResultPrintStream(int version, PrintStream stream) {
        this.version = version;
        this.stream = stream;
    }

    @Override
    public <R extends RekeningHouderInterface> void showDetailed(R rekeninghouder) {
        stream.println();
        stream.println("Detailed: " + rekeninghouder + ", afschriften:");
        int saldo = 0;
        List<String[]> rows = new LinkedList<>();
        String[] header;

        if (version <= 3) {
            throw new UnsupportedClassVersionError("This class is to new " + getClass());
        }

        header = new String[]{"bedrag", "datum", "referentie", "Code", "saldo"};

        rows.add(header);
        List<Record> transacties = rekeninghouder.getAllTransacties();
        RecordGraph g;

        if (transacties == null) {
            rows.add(new String[]{"Geen transacties gevonden..."});
            g = new RecordGraph();
        } else {
            List<Integer> histogramSaldo = new ArrayList<>(transacties.size());
            for (Record r : transacties) {
                Transactie t = r.getTransactie();
                String[] row;
                int index = 0;

                if (version < 3) {
                    row = new String[4];
                } else {
                    row = new String[5];
                }
                if (t.isAf()) {
                    row[index] = "-";
                    saldo -= t.getBedrag();
                } else {
                    row[index] = "+";
                    saldo += t.getBedrag();
                }
                row[index] += t.getBedrag();
                Referentie referentie = t.getReferentie();
                row[++index] = String.valueOf(referentie.getTime());
                row[++index] = String.valueOf(referentie);
                if (version < 3) {

                } else if (t.getReferentie() instanceof Afschrift) {
                    Afschrift a = (Afschrift) t.getReferentie();
                    row[++index] = a.getCode();
                } else {
                    row[++index] = "";
                }
                row[++index] = String.valueOf(saldo);
                rows.add(row);

                histogramSaldo.add(saldo);
            }
            g = new RecordGraph(new Graph(DataSet.histogram(Integer.class, histogramSaldo)));
        }
        stream.println(new MyTxtTableHeader(rows).toString());
    }

    @Override
    public void showTransacties(Iterable<Transactie> trs) {
        stream.println();
        stream.println("Transacties: ");
        super.showTransacties(trs); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void showTransactie(Transactie trs) {
        stream.println(trs);
    }

    @Override
    public <R extends RekeningHouderInterface> void showSchuld(Collection<R> van, RekeningHouder voorRekening) {
        showDetailed(voorRekening);
        stream.println();
        stream.println("Schulden van " + voorRekening + ":");

        List<R> rList = new ArrayList<>(van);
        Comparator<RekeningHouderInterface> comparator = new SchuldenComparator(voorRekening);
        Collections.sort(rList, comparator);

        stream.println();
        stream.println("Result:");
        List<String[]> rows = new ArrayList<>(1 + rList.size());
        rows.add(new String[]{"Naam", "Schuld"});
        for (R r : rList) {
            double euros = ((double) r.getSchuld(voorRekening)) / 100;
            rows.add(new String[]{r.getNaam(), " €" + FORMAT.format(euros) + " "});
        }
        stream.print(new MyTxtTable.MyTxtTableHeader(rows));
    }

    static class SlowPrintStream extends PrintStream {

        public SlowPrintStream(final PrintStream mask) {
            super(new OutputStream() {
                final int wait = 1;

                @Override
                public void write(int b) throws IOException {
                    synchronized (this) {
                        try {
                            wait(wait);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ResultPrintStream.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    mask.write(b);
                }
            });
        }
    }
}
