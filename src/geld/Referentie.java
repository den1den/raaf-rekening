/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld;

import file.StringsData;
import java.io.File;
import tijd.Datum;

/**
 *
 * @author Dennis
 */
public abstract class Referentie {

    public String getRefString() {
        return toString();
    }

    /**
     * De tijd van de referentie.
     *
     * @return de datum en null als deze er niet is.
     */
    public abstract Datum getDatum();

    @Override
    abstract public String toString();

    public static class RefVanFile extends Referentie {

        final StringsData file;
        final int line;

        public RefVanFile(StringsData file, int line) {
            this.file = file;
            this.line = line;
        }

        @Override
        public Datum getDatum() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String toString() {
            return file.toString()+" at line "+line;
        }

    }
}
