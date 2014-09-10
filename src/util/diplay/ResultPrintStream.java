/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.diplay;

import data.Afschrift;
import geld.Referentie;
import geld.ReferentieMultiple;
import geld.RekeningHouder;
import geld.RekeningHouderContant;
import geld.RekeningHouderInterface;
import geld.Transactie;
import geld.TransactiesRecord;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.MyTxtTable;
import util.MyTxtTable.MyTxtTableHeader;
import util.SchuldenComparator;
import util.diplay.gui.graph.DataSet;
import util.diplay.gui.graph.Graph;

public class ResultPrintStream extends Result {

    public static void showFastLast10(RekeningHouder rh) {
        showFast(rh, -10, System.out, 4);
    }

    final int version;
    final PrintStream stream;

    public ResultPrintStream(int version) {
        //this(version, new SlowPrintStream(System.out));
        this(version, (System.out));
    }

    public ResultPrintStream(int version, PrintStream stream) {
        this.version = version;
        this.stream = stream;
    }

    @Override
    public <R extends RekeningHouderInterface> void showDetailed(R rekeninghouder) {
        showFast(rekeninghouder, Integer.MAX_VALUE, stream, version);
    }

    static String[][] listTransacties(List<TransactiesRecord> transacties, int saldo) {
        return listTransacties(transacties, new AtomicInteger(saldo));
    }

    static String[][] listTransacties(List<TransactiesRecord> transacties, AtomicInteger saldo) {
        String[][] rows;
        if (transacties.isEmpty()) {
            rows = new String[][]{new String[]{"Geen transactie gevonden..."}};
        } else {
            rows = new String[transacties.size() + 1][];
            int i = 0;

            rows[i] = new String[]{"verschil", "datum", "referentie", "Code", "saldo"};

            for (TransactiesRecord tR : transacties) {
                i++;

                Transactie t = tR.getTransactie();
                String[] row;
                int index = 0;
                row = new String[5];

                if (t.isAf()) {
                    row[index] = "-";
                    saldo.addAndGet(-t.getBedrag());
                } else {
                    row[index] = "+";
                    saldo.addAndGet(+t.getBedrag());
                }
                row[index] += t.getBedrag();

                Referentie referentie = t.getReferentie();
                if (referentie instanceof ReferentieMultiple) {
                    row[++index] = ">1";
                } else {
                    row[++index] = String.valueOf(referentie.getTime());
                }

                row[++index] = String.valueOf(referentie);

                if (t.getReferentie() instanceof Afschrift) {
                    row[++index] = ((Afschrift) t.getReferentie()).getCode();
                } else {
                    row[++index] = "";
                }

                row[++index] = String.valueOf(saldo);

                rows[i] = row;
            }
        }
        return rows;
    }

    static String[][] lastTransacties(List<TransactiesRecord> trs, Integer saldo, int lines) {
        String[][] rows = listTransacties(trs, saldo);
        String[][] result;
        if (lines > 0 && lines < rows.length) {
            //show begin, delete end
            result = new String[lines][];
            System.arraycopy(rows, 0, result, 0, result.length);
        } else if (lines < 0 && -lines < rows.length) {
            //show end, delete begin
            result = new String[-lines][];
            System.arraycopy(rows, rows.length + lines, result, 0, result.length);
        } else {
            //show alles
            return rows;
        }
        return result;
    }

    public static <R extends RekeningHouderInterface> void showFast(R rekeninghouder, int lines, PrintStream printStream, int v) {
        printStream.println("Detailed: " + rekeninghouder + ", afschriften:");

        int saldo = 0;
        String[][] rows = lastTransacties(rekeninghouder.getAllTransacties(), saldo, lines);

        printStream.print(new MyTxtTableHeader(Arrays.asList(rows)));
        if (rekeninghouder instanceof RekeningHouderContant) {
            showFast(((RekeningHouderContant) rekeninghouder).getContant(), lines, printStream, v);
        }

        printStream.println();
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
        showFast(voorRekening, 10, stream, version);
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

    @Override
    public <R extends RekeningHouderInterface> void showDetailedPer(R rekeninghouder, RekeningHouderInterface... rhis) {
        stream.println();
        stream.println("Schulden van " + rekeninghouder + " per " + rhis.length + " rekeningen:");
        AtomicInteger saldo = new AtomicInteger();
        for (int i = 0; i < rhis.length; i++) {
            if (i > 0) {
                stream.println();
            }
            RekeningHouderInterface rekeningHouderInterface = rhis[i];
            stream.println(" " + rekeningHouderInterface + ":");
            List<TransactiesRecord> all = rekeninghouder.getAllTransacties(rekeningHouderInterface);
            MyTxtTable table = new MyTxtTableHeader(Arrays.asList(listTransacties(all, saldo)));
            for (String line : table.toLines()) {
                stream.println("  " + line);
            }
        }
        stream.println("__________");
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
