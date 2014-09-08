/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.diplay;

import data.Afschrift;
import geld.Referentie;
import geld.RekeningHouder;
import geld.Transactie;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import util.MyTxtTable;
import util.SchuldenComparator;

/**
 *
 * @author Dennis
 */
public class PrintStreamResult extends Result {

    final int version;
    final PrintStream stream;

    public PrintStreamResult(int version) {
        this(version, System.out);
    }

    public PrintStreamResult(int version, PrintStream stream) {
        this.version = version;
        this.stream = stream;
    }

    public <R extends RekeningHouder> void showDepts(Collection<R> collection, RekeningHouder van) {

        showDetailed(van);
        stream.println();
        stream.println("Schulden van " + van + ":");

        List<R> rList = new ArrayList<>(collection);
        Collections.sort(rList, new SchuldenComparator(van));

        stream.println();
        stream.println("Result:");
        List<String[]> rows = new ArrayList<>(1 + rList.size());
        rows.add(new String[]{"Naam", "Schuld"});
        for (R r : rList) {
            double euros = ((double) r.getSchuld(van)) / 100;
            rows.add(new String[]{r.getNaam(), " €" + FORMAT.format(euros) + " "});
        }
        stream.print(new MyTxtTable.MyTxtTableHeader(rows));
    }

    @Override
    public void showTransacties(Collection<Transactie> factuurs) {
        stream.println();
        stream.println("Facturen:");
        for (Transactie factuur : factuurs) {
            stream.println(factuur);
        }
    }

    @Override
    public <R extends RekeningHouder> void showDetailed(R rekeninghouder) {
        stream.println();
        stream.println("Detailed: " + rekeninghouder + ", afschriften:");
        int saldo = 0;
        List<String[]> rows = new LinkedList<>();
        String[] header;

        if (version < 3) {
            header = new String[]{"bedrag", "datum", "referentie", "saldo"};
        } else {
            header = new String[]{"bedrag", "datum", "referentie", "Code", "saldo"};
        }

        rows.add(header);
        for (Transactie t : rekeninghouder.getTransacties(rekeninghouder)) {
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
            }else{
                row[++index] = "";
            }
            row[++index] = String.valueOf(saldo);
            rows.add(row);
        }
        stream.print(new MyTxtTable.MyTxtTableHeader(rows));
    }
}
