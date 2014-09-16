/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers.simple;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public interface StringParserSmart<O> extends StringParser<O> {

    public O find(String s);

    public static class Datum extends StringParserSmartImpl<tijd.Datum, DateFormat> {

        public Datum() {
            super(new DateFormat[]{
                new SimpleDateFormat("d-M-yy"),
                new SimpleDateFormat("dd-MM-yyyy"),
                new SimpleDateFormat("ddMMyyyy"),
                new SimpleDateFormat("dMyy")});
        }

        public Datum(DateFormat[] formatters) {
            super(formatters);
        }

        @Override
        protected tijd.Datum parse(String s, DateFormat formatter) {
            try {
                Date d = formatter.parse(s);
                return tijd.Datum.from(d);
            } catch (ParseException ex) {
            }
            return null;
        }

    }
}
