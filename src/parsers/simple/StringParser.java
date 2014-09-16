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
import tijd.Datum;

/**
 *
 * @author Dennis
 * @param <O>
 */
public interface StringParser<O> {

    /**
     * Parse a string to O
     *
     * @param s the string to parse from
     * @return null if nothing found, else the object
     */
    public abstract O parse(String s);

    public static class FastDatum implements StringParser<Datum> {

        @Override
        public Datum parse(String s) {
            if (s.length() >= 4 && s.length() <= 6) {
                for (int i = 0; i < s.length(); i++) {
                    if (!Character.isDigit(s.charAt(i))) {
                        return null;
                    }
                }
                if (s.length() == 5) {
                    s = "0" + s;
                } else if (s.length() == 4) {
                    s = "0" + s.substring(0, 1) + "0" + s.substring(1);
                }

                int year, month, day;
                day = Integer.parseInt(s.substring(0, 2));
                month = Integer.parseInt(s.substring(2, 4));
                year = 2000 + Integer.parseInt(s.substring(4, 6));
                return new Datum(year, month, day);
            }
            return null;
        }
    }

    public static class NormalDatum implements StringParser<Datum> {

        private final DateFormat dateFormat;

        public NormalDatum(DateFormat dateFormat) {
            this.dateFormat = dateFormat;
        }

        public NormalDatum(String simpleFormat) {
            this(new SimpleDateFormat(simpleFormat));
        }

        public NormalDatum() {
            this("d-M-y");
        }

        @Override
        public Datum parse(String s) {
            try {
                Date parsed = dateFormat.parse(s);
                return Datum.from(parsed);
            } catch (ParseException e) {
            }
            return null;
        }
    }

    public static class Bedrag implements StringParser<Integer> {

        @Override
        public Integer parse(String s) {
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                if (s.contains(",")) {
                    return parse(s.replace(",", ""));
                }
                if (s.contains(".")) {
                    return parse(s.replace(".", ""));
                }
            }
            return null;
        }

    }
    /*private static class TijdP extends StringParser<TimeEstimate> {

     @Override
     public boolean has(String s) {
     int l = s.length();
     if (l <= 6 && l >= 4) {
     for (int i = 0; i < l; i++) {
     if (!Character.isDigit(s.charAt(i))) {
     return false;
     }
     }
     return true;
     }
     if (l >= 8 && l <= 10) {
     //could be 1-1-14 || 13-12-11
     int i1 = s.indexOf('-');
     int i2 = s.indexOf('-', i1 + 1);
     if (i1 != -1 && i2 != -1) {
     for (int i = 0; i < l; i++) {
     if (i != i1 && i != i2) {
     if (!Character.isDigit(s.charAt(i))) {
     return false;
     }
     }
     }
     return true;
     } else {
     //continue
     }
     }
     return false;
     }

     @Override
     public TimeEstimate parse(String s) {
     int lenght = s.length();

     //check for two -
     int i1 = s.indexOf('-');
     if (i1 != -1) {
     int i2 = s.indexOf('-', i1 + 1);
     if (i2 == -1) {
     throw new UnsupportedOperationException("Only one -?");
     }
     //has both --
     int day = Integer.parseInt(s.substring(0, i1));
     int month = Integer.parseInt(s.substring(i1 + 1, i2));
     int year;
     String yearString = s.substring(i2 + 1);
     year = Integer.parseInt(yearString);
     if (yearString.length() == 4) {
     //ok
     } else if (yearString.length() == 2) {
     year += 2000;
     } else {
     throw new IllegalArgumentException("weird year " + s);
     }
     return TimeEstimate.calcFromDag(year, month, day);
     } else {
     throw new UnsupportedOperationException("TODO");
     if (lenght == 10) {
     baseYear = Integer.parseInt(s.substring(4, 5)) * 100;
     s = s.substring(0, 1) + s.substring(2, 3) + s.substring(6, 7);
     } else if (lenght == 6) {

     } else if (lenght == 5) {
     //asume day is first one
     if (Integer.parseInt(s.substring(1, 2)) <= 12) {
     s = "0" + s;
     } else {
     s = s.substring(0, 1) + "0" + s.substring(2);
     }
     } else if (lenght == 4) {
     s = "0" + s.substring(0, 1) + "0" + s.substring(2);
     } else {
     throw new UnsupportedOperationException("Cannot parse: " + s);
     }
     }
     int day = Integer.parseInt(s.substring(0, 2));
     int month = Integer.parseInt(s.substring(2, 4));
     int year = Integer.parseInt(s.substring(4));

     return TimeEstimate.calcFromDag(year, month, day);
     }
     }*/
}
