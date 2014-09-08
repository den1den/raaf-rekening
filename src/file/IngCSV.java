/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file;

import java.io.File;
import java.io.IOException;

public class IngCSV extends StringsDataFile {

    private IngCSV(File file, StringsFilter stringsFilter, String[] header) {
        super(file, stringsFilter, header);
    }
    private static final String excelFackupHeader = "\"Datum,\"\"Naam / Omschri"
            + "jving\"\",\"\"Rekening\"\",\"\"Tegenrekening\"\",\"\"Co"
            + "de\"\",\"\"Af Bij\"\",\"\"Bedrag (EUR)\"\",\"\"MutatieS"
            + "oort\"\",\"\"Mededelingen\"\"\"";

    /**
     *
     * @param file
     * @param header
     * @return
     */
    public static IngCSV get(File file, String... header) {
        IngCSV data = new IngCSV(
                file,
                StringsFilter.getSplitByAanhalingsTekens(),
                header);
        if (!data.checkHeader()) {
            String headerLine;
            try {
                headerLine = data.readFirstString();
            } catch (IOException ex) {
                throw new Error(ex);
            }
            if(headerLine.equals(excelFackupHeader)){
                //can be the fuckup
                throw new UnsupportedOperationException("excel fackup detected => change StringsFilter");
            }
        }
        return data;
    }
}
