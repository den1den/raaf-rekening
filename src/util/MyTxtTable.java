/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author Dennis
 */
public class MyTxtTable {

    List<String[]> rows;
    List<Integer> colWidth;
    int totalWidth;
    char c = '*';
    private String NULL = "null";

    public MyTxtTable(List<String[]> rows) {
        this.rows = rows;
        colWidth = new LinkedList<>();
        for (String[] row : rows) {
            int currWidth = 1;
            for (int i = 0; i < row.length; i++) {
                String field = row[i];
                if (field == null) {
                    row[i] = NULL;
                    field = NULL;
                }
                    if (i >= colWidth.size()) {
                        colWidth.add(field.length());
                    } else {
                        int oldWidth = colWidth.get(i);
                        if (field.length() > oldWidth) {
                            colWidth.set(i, field.length());
                        }
                    }
            }
        }
        totalWidth = 1;
        for (Integer i : colWidth) {
            totalWidth += i + 1;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder((totalWidth + 2) * (rows.size() + 2));
        line(sb);
        Iterator<String[]> it = rows.iterator();
        if (it.hasNext()) {
            do {
                row(sb, it.next());
            } while (it.hasNext());
            line(sb);
        }
        return sb.toString();
    }
    
    public String[] toLines(){
        LinkedList<String> lines = new LinkedList<>();
        for (StringTokenizer st = new StringTokenizer(toString(), System.lineSeparator()); st.hasMoreTokens();) {
            String line = st.nextToken();
            lines.add(line);
        }
        return lines.toArray(new String[lines.size()]);
    }

    protected void line(StringBuilder sb) {
        for (int i = 0; i < totalWidth; i++) {
            sb.append(c);
        }
        sb.append(System.lineSeparator());
    }

    protected void row(StringBuilder sb, String[] row) {
        sb.append(c);
        for (int i = 0; i < row.length; i++) {
            sb.append(row[i]);
            for (int j = row[i].length(); j < colWidth.get(i); j++) {
                sb.append(' ');
            }
            sb.append(c);
        }
        sb.append(System.lineSeparator());
    }

    public static MyTxtTable createWithHeader(List<String[]> rows, String[] header) {
        List<String[]> rows2 = new ArrayList<>(rows.size() + 1);
        rows2.add(header);
        rows2.addAll(rows);
        return new MyTxtTableHeader(rows);
    }

    public static class MyTxtTableHeader extends MyTxtTable {

        public MyTxtTableHeader(List<String[]> rows) {
            super(rows);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder((totalWidth + 2) * (rows.size() + 2));
            line(sb);
            Iterator<String[]> it = rows.iterator();
            if (it.hasNext()) {
                row(sb, it.next());
                line(sb);
                if (it.hasNext()) {
                    do {
                        row(sb, it.next());
                    } while (it.hasNext());
                    line(sb);
                }
            }
            return sb.toString();
        }
    }
}
