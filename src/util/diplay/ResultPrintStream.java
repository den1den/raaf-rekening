/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.diplay;

import data.Persoon;
import data.types.HasDatum;
import file.ExcelCSV;
import geld.rekeningen.Event;
import geld.rekeningen.RekeningLeen;
import geld.rekeningen.RekeningLeenBudget;
import geld.rekeningen.Som;
import geld.rekeningen.SomMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import raafbeheer.RaafBeheer;
import tijd.Datum;
import util.Arrays;
import util.MyTxtTable;

/**
 *
 * @author Dennis
 */
public class ResultPrintStream {

    private final int version;
    private PrintStream outputStream;

    public ResultPrintStream(int version) {
        this.version = version;
        outputStream = System.out;
    }

    public ResultPrintStream(int version, PrintStream outputStream) {
        this.version = version;
        this.outputStream = outputStream;
    }

    public void listAsSpreadsheet(RekeningLeenBudget raafRekening) {
        Som budget = raafRekening.getBudget();
        SomMap<RekeningLeen> krijgtNogVan = raafRekening.getKrijgtNogVan();
        Som krijgtNogVanTotaal = krijgtNogVan.getTotal();
        Som bankRekening = raafRekening.getBankRekening();
        Som contant = raafRekening.getContant();

        List<Event> events = raafRekening.getAllHistory();
        HasDatum.sort(events, Datum.COMP_BY_DAY);

        List<String[]> listEvents = listEvents(events, false, true, budget, bankRekening, krijgtNogVanTotaal, contant);
        
        outputStream.println(new MyTxtTable.MyTxtTableHeader(listEvents, ""));
    }

    public static List<String[]> listEvents(List<Event> events, boolean allowAdd, final boolean totalCollumn, Som... tovs) {
        if (allowAdd) {
            throw new UnsupportedOperationException();
        }
        ArrayList<Som> tovsArrayList = Arrays.asListNoNull(tovs);
        ArrayList<Integer> tovsTotals = new ArrayList<>(tovsArrayList.size());

        int cols = 2 + tovsArrayList.size();
        if (totalCollumn) {
            cols += tovsArrayList.size();
        }
        List<String[]> result = new ArrayList<>(events.size() + 2);
        int index = 0;

        String[] line = new String[cols];
        line[index] = "Datum";
        line[++index] = "Event";
        for (Som tov : tovsArrayList) {
            line[++index] = tov.beschrijving();
            if (totalCollumn) {
                line[++index] = "Totaal (0)";
                tovsTotals.add(0);
            }
        }
        result.add(line);

        for (Event event : events) {
            line = new String[cols];
            line[0] = event.getDatum().toString();
            line[1] = event.getMessage();
            Set<Map.Entry<Som, List<Integer>>> entrySet = event.entrySet();
            for (Map.Entry<Som, List<Integer>> e : entrySet) {
                Som s = e.getKey();
                int at = tovsArrayList.indexOf(s);
                if (at >= 0) {
                    int diff = s.getTotalDiff(e.getValue());
                    if (totalCollumn) {
                        index = 2 + at * 2;
                        line[index] = bedragToString(diff);
                        int newTotal = tovsTotals.get(at) + diff;
                        line[index + 1] = " " + bedragToString(newTotal) + " ";
                        tovsTotals.set(at, newTotal);
                    } else {
                        index = 2 + at;
                        line[index] = bedragToString(diff);
                    }
                }
            }
            if (totalCollumn) {
                for (int i = 0; i < tovsArrayList.size(); i++) {
                    index = 2 + 2 * i + 1;
                    if (line[index] == null) {
                        line[index] = "\"";
                    }
                }
            }
            result.add(line);
        }

        line = new String[cols];
        index = 0;
        line[index] = "TOTALEN:"; //skip event beschr
        line[++index] = Integer.toString(events.size());
        for (Som tov : tovsArrayList) {
            int total = tov.getTotal();
            if (totalCollumn) {
                int calced = tovsTotals.get((index-1) / 2);
                index++;
                if (calced != total) {
                    line[index] = "CALC_FAIL{" + bedragToString(calced) + "}";
                    Logger.getLogger(ResultPrintStream.class.getName()).log(Level.SEVERE, "Calc failed! => not correct");
                }
            }
            line[++index] = bedragToString(total);
        }
        result.add(line);
        return result;
    }

    public static String bedragToString(int centen) {
        StringBuilder sb = new StringBuilder(7);
        if (centen < 0) {
            sb.append('-');
            centen = -centen;
        }
        sb.append('€');
        sb.append(centen / 100);
        sb.append('.');
        sb.append(centen % 100);
        return sb.toString();
    }
    int fileC = 0;

    public void toFile() {
        File output = new File("output " + System.currentTimeMillis() + " " + hashCode() + " " + (fileC++) + ".txt");
        try {
            outputStream = new PrintStream(output);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ResultPrintStream.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void listPersoon(Persoon p) {
        String line = "Overzicht van: "+p.getNaam();
        if(p.kwijtschelden()){
            line += " word kwijtgescholden voor eten";
        }
        
        
        Som bank = p.getBankRekening();
        SomMap<RekeningLeen> krijgtNogVan = p.getKrijgtNogVan();
        
        line += " krijgtNog totaal: "+krijgtNogVan.getTotal();
        
        outputStream.println(line);
    }
    
    public void listPersoonDetailed(Persoon p){
        String line = "Overzicht van: "+p.getNaam() + System.lineSeparator();
        line += " bank: "+p.getBankRekening() + System.lineSeparator();
        for (Som ss : p.getKrijgtNogVan()) {
            line += "  som:"+ss.beschrijving() + System.lineSeparator();
            int totaal = ss.getTotal(0);
            for (Som.Record r : ss) {
                totaal += r.diff;
                line += "  "+(totaal)+" "+r.history + System.lineSeparator();
            }
            line += "  TOTAAL: "+totaal+"ct" + System.lineSeparator();
        }
        outputStream.print(line);
    }

    public void toOut() {
        this.outputStream = System.out;
    }
}
