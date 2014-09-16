/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tijd;

import data.types.HasDate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import static java.util.Calendar.*;
import java.util.Comparator;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dennis
 */
public class Datum implements Comparable<Datum>, Cloneable {

    static final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    static final public Datum nu = new Datum(null);
    static final public Datum now = nu;

    /**
     * must be defenced against
     */
    private Calendar calendar;

    /**
     * Constructor
     * @param c 
     */
    private Datum(Calendar c) {
        this.calendar = c;
    }
    
    /**
     * Default constructor 
     */
    private Datum(){
        this(getInstance());
    }

    /**
     * Create date at given time
     * @param millis time in milliseconds
     */
    Datum(long millis) {
        this();
        this.calendar.setTimeInMillis(millis);
    }

    Calendar getCalendar() {
        TimeZone zone = calendar.getTimeZone();
        Calendar c = Calendar.getInstance(zone);
        c.setTimeInMillis(calendar.getTimeInMillis());
        return c;
    }
    
    /**
     * Copy
     * @return 
     */
    @Override
    protected Datum clone(){
        try {
            super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Datum.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Datum(getCalendar());
    }

    /**
     *
     * @param year
     * @param month
     * @param day
     */
    public Datum(int year, int month, int day) {
        this.calendar.setLenient(false);
        this.calendar.set(year, month - 1, day);
        this.calendar.getTime(); //calculate
    }
    
    public static Datum from(java.util.Date date){
        return new Datum(date.getTime());
    }

    static void startOfDay(Calendar c) {
        for (int field : new int[]{HOUR_OF_DAY, MINUTE, SECOND, MILLISECOND}) {
            c.set(field, c.getMinimum(field));
        }
    }

    static void endOfDay(Calendar c) {
        for (int field : new int[]{HOUR_OF_DAY, MINUTE, SECOND, MILLISECOND}) {
            c.set(field, c.getMaximum(field));
        }
    }

    public boolean before(Datum datum) {
        return compareTo(datum) < 0;
    }

    public boolean after(Datum datum) {
        return compareTo(datum) > 0;
    }

    public boolean equals(Datum datum) {
        return compareTo(datum) == 0;
    }

    @Override
    public int compareTo(Datum o) {
        //return calendar.compareTo(o.calendar);
        return COMP_BY_DAY.compare(this, o);
    }

    @Override
    public String toString() {
        return dateFormat.format(calendar.getTime());
    }

    public Datum getNextMonth() {
        Datum n = clone();
        n.calendar.add(MONTH, 1);
        return n;
    }
    
    public static Comparator<Datum> COMP_BY_DAY = new Comparator<Datum>() {

        @Override
        public int compare(Datum o1, Datum o2) {
            int cmp = Integer.compare(o1.jaar(), o2.jaar());
        if(cmp == 0){
            cmp = Integer.compare(o1.maand(), o2.maand());
            if(cmp == 0){
                cmp = Integer.compare(o1.dag(), o2.dag());
            }
        }
        return cmp;
        }
    };
    
    int jaar(){
        return calendar.get(YEAR);
    }
    
    int maand(){
        return calendar.get(MONTH) + 1;
    }
    
    int dag(){
        return calendar.get(DAY_OF_MONTH);
    }
}
