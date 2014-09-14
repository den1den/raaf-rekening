/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.diplay;

import data.Afschrift;
import data.Persoon;
import data.memory.Memory;
import geld.Referentie;
import geld.geldImpl.HasSchulden;
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

public class ResultPrintStream {

    /**
     *
     * @param rs rekeningen TOV laatste
     */
    public static void showLastT(Object... rs) {
        int max = 2;

        List<String[]> TOV_ordered = getTOV_ordered(Arrays.asList(rs), rs[rs.length - 1]);

        int keepUpper = 1;
        for (int i = keepUpper; i < TOV_ordered.size() - max; i++) {
            //keep header
            TOV_ordered.remove(keepUpper);
        }

        System.out.println("Show " + (rs.length - 1) + " against: " + rs[rs.length - 1].toString());
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

    static String[][] listTransacties(List<Object> transacties, int saldo) {
        return listTransacties(transacties, new AtomicInteger(saldo));
    }

    static String[][] listTransacties(List<Object> transacties, AtomicInteger saldo) {
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

    static String[][] lastTransacties(List<Object> trs, Integer saldo, int lines) {
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

    public static List<String[]> getTOV_ordered(Collection<? extends HasSchulden> van, HasSchulden... tov) {
        //stream.println();
        //stream.println("Schulden van " + voorRekening + " ("+voorRekening.getSaldo()+"):");

        List<HasSchulden> rList = new ArrayList<>(van);
        Comparator<HasSchulden> comparator = SchuldenComparator.by(tov);
        Collections.sort(rList, comparator);

        return getTOV(rList, tov);
    }

    public static List<String[]> getTOV(Collection<HasSchulden> onderwerps, HasSchulden... tovs) {
        List<String[]> r = new ArrayList<>(onderwerps.size() + 1);
        ArrayList<String> row = new ArrayList<>(2 + 3 * tovs.length);
        row.add("Naam");
        row.add("Krijgt");
        row.add(" ");
        for (int i = 0; i < tovs.length; i++) {
            HasSchulden tov = tovs[i];
            row.add("van "+tov.getNaam());
        }
        r.add(row.toArray(new String[row.size()]));
        for (HasSchulden onderwerp : onderwerps) {
            row.clear();
            row.add(onderwerp.getNaam());
            row.add(onderwerp.getKrijgtNogVan().show());
            row.add(" ");
            for (int i = 0; i < tovs.length; i++) {
                HasSchulden tov = tovs[i];
                row.add(onderwerp.getKrijgtNogVan().show(tov));
            }
            r.add(row.toArray(new String[row.size()]));
        }
        return r;
    }
    public static List<String[]> getTOV_ordered(List<? extends HasSchulden> onderwerps, HasSchulden... tovs) {
        List<HasSchulden> rList = new ArrayList<>(onderwerps);
        Comparator<HasSchulden> comparator = SchuldenComparator.by(tovs);
        Collections.sort(rList, comparator);
        return getTOV(rList, tovs);
    }
    
    public static Object lijst1(HasSchulden hs, HasSchulden... tov){
        return getTOV_ordered(Arrays.asList(hs), tov);
    }
    
    public static void lijst2(Object o, HasSchulden hs, HasSchulden... tov){
        if(!(o instanceof List)){
            throw new IllegalArgumentException();
        }
        List<String[]> old = (List<String[]>)o;
        List<HasSchulden> hsList = Arrays.asList(hs);
        
        System.out.println(getHeader(hsList, tov));
        old.add(new String[]{});
        
        List<String[]> new_ = getTOV_ordered(hsList, tov);
        new_.remove(0);//remove header
        old.addAll(new_);
        System.out.println(new MyTxtTableHeader(old));
    }
    
    public static void lijstje(HasSchulden hs, HasSchulden... tov){
        showTOV(Arrays.asList(hs), tov);
    }
    
    public static void lijstje(Memory memory, HasSchulden... tov){
        showTOV(memory.personen.getAll(), tov);
    }
    
    public static void showTOV(Collection<? extends HasSchulden> items, HasSchulden... tov){
        String header = getHeader(items, tov);
        System.out.println(header);
        System.out.println(new MyTxtTableHeader(getTOV_ordered(items, tov)));
    }
    
    private static String getHeader(Collection<? extends HasSchulden> items, HasSchulden... tov){
        String header;
        if(tov.length > 0){
            header = items.size() + " ten opzichte van ("+tov.length+"): ";
            header += tov[0].getNaam();
            for (int i = 1; i < tov.length; i++) {
                header += ", " + tov[i].getNaam();
            }
        }else{
            header = items.size() + " schulden";
        }
        return header;
    }

    public void listResultaat(Collection<? extends HasSchulden> rhs, HasSchulden tov) {
        this.stream.println(rhs.size() + " rekeningen tegen " + tov + " (" + tov.getKrijgtNogVan().show() + ")");
        this.stream.println(new MyTxtTableHeader(ResultPrintStream.getTOV_ordered(rhs, tov)));
    }

    public <RH extends HasSchulden> void showDetailledTov(Collection<RH> subject, RH... rhs) {
        stream.println();
        stream.print("Details for: ");
        for (int i = 0; i < rhs.length; i++) {
            stream.print(rhs + " ");
        }
        stream.println();
        stream.print(new MyTxtTableHeader(getTOV_ordered(subject, rhs)));
    }

    public void listResultaat(Collection<? extends Object> rhs, Object tov, boolean dummy) {
        this.stream.println("List: " + rhs.size() + " rekeningen tegen " + tov + " (€" + ((double) tov.getSchulden().getSaldo()) / 100 + ")");

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
