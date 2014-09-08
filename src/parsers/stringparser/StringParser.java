/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers.stringparser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import tijd.Datum;

/**
 *
 * @author Dennis
 * @param <O>
 */
public abstract class StringParser<O> {

    public static StringParser<Datum> getFastestDayParser() {
        return new StringParser<Datum>() {

            @Override
            public boolean has(String s) {
                if (s.length() >= 4 && s.length() <= 6) {
                    for (int i = 0; i < s.length(); i++) {
                        if (!Character.isDigit(s.charAt(i))) {
                            object = null;
                            return false;
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
                    object = new Datum(year, month, day);
                    return true;
                }
                object = null;
                return false;
            }
        };
    }

    protected O object = null;

    /**
     * see if the string complies with previous format
     *
     * @param s
     * @return
     */
    public abstract boolean has(String s);

    /**
     * @return the object of last has(String s) call
     */
    public final O get() {
        if (object == null) {
            throw new IllegalStateException("Nothing parsed");
        }
        return object;
    }

    public static StringParser<Datum> getDayParser(final SimpleDateFormat format) {
        return new StringParser<Datum>() {

            @Override
            public boolean has(String s) {
                try {
                    object = new Datum(format.parse(s));
                } catch (ParseException e) {
                    object = null;
                    return false;
                }
                return true;
            }
        };
    }

    public static StringParserWInit<Datum> getNewSmartDayParser() {
        final DateFormat[] dateFormats = new DateFormat[]{
            new SimpleDateFormat("d-M-yy"),
            new SimpleDateFormat("dd-MM-yyyy"),
            new SimpleDateFormat("ddMMyyyy"),
            new SimpleDateFormat("dMyy")};
        return new StringParserWInit.StringParserWInitImpl<Datum, DateFormat>(dateFormats) {

            @Override
            public boolean has(String s) {
                try {
                    object = new Datum(helpers[index].parse(s));
                } catch (ParseException e) {
                    object = null;
                    return false;
                }
                return true;
            }
        };
    }

    abstract class StringParserImpl<O, Helper> extends StringParser<O> {

        protected int index = -1; //not set
        protected final Helper[] helpers;

        public StringParserImpl(Helper[] objects) {
            this.helpers = objects;
        }
    }

    public final static StringParser<Integer> INTEGER = new StringParser<Integer>() {

        @Override
        public boolean has(String s) {
            try {
                object = Integer.parseInt(s);
                return true;
            } catch (NumberFormatException e) {
                if (s.contains(",")) {
                    return has(s.replace(",", ""));
                }
                if (s.contains(".")) {
                    return has(s.replace(".", ""));
                }
                object = null;
            }
            return false;
        }
    };
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
