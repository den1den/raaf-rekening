/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import parsers.stringparser.StringParserWInit;
import tijd.Datum;

/**
 *
 * @author Dennis
 */
class tweedeRegelDatumComparator implements Comparator<File> {

    private final StringParserWInit<Datum> parser =
            StringParserWInit.getNewSmartDayParser();

    @Override
    public int compare(File o1, File o2) {
        Datum d1 = getDate(o1);
        Datum d2 = getDate(o2);
        if (d1 == null || d2 == null) {
            return 0;
        } else {
            return d1.compareTo(d2);
        }
    }

    private Datum getDate(File f1) {
        try (BufferedReader br = new BufferedReader(new FileReader(f1))) {
            br.readLine();
            String line = br.readLine();
            if (line == null) {
                return null;
            }
            if (line.charAt(0) == '"') {
                line = line.substring(1);
            }

            int comma = line.indexOf(',');
            int aanhal = line.indexOf('"');
            line = line.substring(0, Math.min(comma, aanhal));

            if (parser.init(line)) {
                return parser.get();
            }
        } catch (IOException e) {
        }
        return null;
    }
}
