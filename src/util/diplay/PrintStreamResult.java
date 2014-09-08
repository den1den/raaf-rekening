/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.diplay;

import data.Afschrift;
import geld.RekeningHouder;
import geld.Transactie;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import util.MyTxtTable;
import util.RekeningHouderComparator;

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

    static final RekeningHouderComparator rekeningHouderComparator = new RekeningHouderComparator();

    @Override
    public <R extends RekeningHouder> void showBalances(Collection<R> collection) {
        List<R> rList = new ArrayList<>(collection);
        Collections.sort(rList, rekeningHouderComparator);

        stream.println();
        stream.println("Result:");
        List<String[]> rows = new ArrayList<>(1 + rList.size());
        rows.add(new String[]{"Naam", "Schuld"});
        for (R r : rList) {
            double euros = ((double) r.getSaldo()) / 100;
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
        int bedrag = 0;
        List<String[]> rows = new LinkedList<>();
        String[] header;

        if (version < 3) {
            header = new String[]{"bedrag", "datum", "referentie", "saldo"};
        } else {
            header = new String[]{"bedrag", "datum", "referentie", "Code", "saldo"};
        }

        rows.add(header);
        for (Transactie af : rekeninghouder.getTransactiesEnSchulden()) {
            String[] row;
            int index = 0;
            
            if(version < 3){
                row = new String[4];
            }else{
                row = new String[5];
            }
            if (af.isAf()) {
                row[index] = "-";
                bedrag -= af.getBedrag();
            } else {
                row[index] = "+";
                bedrag += af.getBedrag();
            }
            row[index] += af.getBedrag();
            row[++index] = String.valueOf(af.getDatum());
            row[++index] = String.valueOf(af.getReferentie());
            if(version<3){
                
            }else if (af.getReferentie() instanceof Afschrift){
                Afschrift a = (Afschrift)af.getReferentie();
                row[++index] = a.getCode();
            }
            row[++index] = String.valueOf(bedrag);
            rows.add(row);
        }
        stream.print(new MyTxtTable.MyTxtTableHeader(rows));
    }
}
