/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.diplay;

import data.Afschrift;
import data.Persoon;
import geld.Referentie;
import geld.ReferentieMultiple;
import geld.RekeningHouder;
import geld.RekeningHouderContant;
import geld.RekeningHouderContantInterface;
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

    /**
     * 
     * @param rs rekeningen TOV laatste
     */
    public static void showLastT(RekeningHouder... rs) {
        int max = 2;
        
        List<String[]> TOV_ordered = getTOV_ordered(Arrays.asList(rs), rs[rs.length - 1]);
        
        int keepUpper = 1;
        for (int i = keepUpper; i < TOV_ordered.size()-max; i++) {
            //keep header
            TOV_ordered.remove(keepUpper);
        }
        
        System.out.println(new MyTxtTableHeader(TOV_ordered));
        
        /*
        List<TransactiesRecord> tr1 = r1.getTransacties(r2);
        List<TransactiesRecord> tr2 = r2.getTransacties(r1);
        System.out.println("1: "+r1.toString());
        for (int i = 0; i < tr1.size() && i < max; i++) {
            System.out.println(" "+tr1.get(i).toString());
        }
        System.out.println("2: "+r2.toString());
        for (int i = 0; i < tr2.size() && i < max; i++) {
            System.out.println(" "+tr2.get(i).toString());
        }
        System.out.println();
                */
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

    public static List<String[]> getTOV_ordered(Collection<? extends RekeningHouderInterface> van, RekeningHouderInterface... voorRekening) {
        //stream.println();
        //stream.println("Schulden van " + voorRekening + " ("+voorRekening.getSaldo()+"):");

        List<RekeningHouderInterface> rList = new ArrayList<>(van);
        Comparator<RekeningHouderInterface> comparator = SchuldenComparator.by(voorRekening);
        Collections.sort(rList, comparator);

        return getTOV(rList, voorRekening);
    }
    
    public static List<String[]> getTOV(Collection<RekeningHouderInterface> onderwerps, RekeningHouderInterface... tovs){
        ArrayList<String[]> r = new ArrayList<>(onderwerps.size()+1);
        ArrayList<String> row = new ArrayList<>(2 + 3*tovs.length);
        row.add("Naam"); row.add("Saldo");
        for (int i = 0; i < tovs.length; i++) {
            RekeningHouderInterface tov = tovs[i];
            row.add(i+"betaaldNog ");
            row.add(i+"betaald ");
            row.add(i+"totaal ");
        }
        r.add(row.toArray(new String[row.size()]));
        for (RekeningHouderInterface onderwerp : onderwerps) {
            row.clear();
            row.add(onderwerp.getNaam());
            row.add(String.valueOf(onderwerp.getSaldo()));
            for (int i = 0; i < tovs.length; i++) {
                RekeningHouderInterface tov = tovs[i];
                row.add(String.valueOf(onderwerp.getBetaaldNog(tov)));
                row.add(String.valueOf(onderwerp.getBetaald(tov)));
                row.add(String.valueOf(onderwerp.getSaldo(tov)));
            }
            r.add(row.toArray(new String[row.size()]));
        }
        return r;
    }

    @Override
    public void listResultaat(Collection<? extends RekeningHouderInterface> rhs, RekeningHouder tov) {
        this.stream.println("List: "+rhs.size()+" rekeningen tegen "+tov+ " (€"+((double)tov.getSaldo())/100+")");
        this.stream.println(new MyTxtTableHeader(getTOV_ordered(rhs, tov)));
    }

    @Override
    public <RH extends RekeningHouderInterface> void showDetailledTov(Collection<RH> subject, RH... rhs) {
        stream.println();
        stream.print("Details for: ");
        for (int i = 0; i < rhs.length; i++) {
            stream.print(rhs+" ");
        }
        stream.println();
        stream.print(new MyTxtTableHeader(getTOV_ordered(subject, rhs)));
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
