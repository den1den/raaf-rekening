/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file;

import java.io.File;

public class ExcelCSV extends StringsDataFile {

    private ExcelCSV(File file, StringsFilter stringsFilter, String[] header) {
        super(file, stringsFilter, header);
    }

    public static ExcelCSV get(File file, String... header) {
        StringsFilter stringsFilter = StringsFilter.getSplitByPuntComma();
        ExcelCSV csv = new ExcelCSV(file, stringsFilter, header);
        csv.checkHeader();
        return csv;
    }

    public static ExcelCSV[] get(File[] file, String... header) {
        StringsFilter stringsFilter = StringsFilter.getSplitByPuntComma();

        ExcelCSV[] csvs = new ExcelCSV[file.length];

        for (int i = 0; i < file.length; i++) {
            csvs[i] = new ExcelCSV(file[i], stringsFilter, header);
            csvs[i].checkHeader();
        }
        return csvs;
    }
}
