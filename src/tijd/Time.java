/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tijd;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import static java.util.Calendar.MONTH;

/**
 *
 * @author Dennis
 */
public class Time implements Comparable<Time>{

    static final DateFormat timeFormat = new SimpleDateFormat("dd-MM-yyyy H:ss");
    static final Time now = new Time();

    protected Calendar cal;

    public Time() {
        cal = Calendar.getInstance();
        cal.setLenient(false);
    }

    /**
     * 
     * @param year
     * @param month [1,12]
     * @param day
     * @param hours
     * @param minutes
     * @param sec 
     */
    public Time(int year, int month, int day, int hours, int minutes, int sec) {
        this();
        cal.set(year, month - 1, day, hours, minutes, sec);
    }

    /**
     * 
     * @param year
     * @param month [1,12]
     * @param day
     * @param hours
     * @param minutes 
     */
    public Time(int year, int month, int day, int hours, int minutes) {
        this();
        cal.set(year, month - 1, day, hours, minutes);
    }

    /**
     * 
     * @param year
     * @param month [1,12]
     * @param day 
     */
    public Time(int year, int month, int day) {
        this();
        cal.set(year, month - 1, day);
    }

    public Time(int year, int month) {
        this();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
    }

    public Time(java.util.Date utilDate) {
        this();
        cal.setTime(utilDate);
    }

    public Time(Time time) {
        cal = (Calendar) time.cal.clone();
    }

    public boolean before(Time time) {
        return cal.before(time.cal);
    }

    public Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(cal.getTimeInMillis());
        return calendar;
    }

    @Override
    public String toString() {
        return timeFormat.format(cal.getTime());
    }

    int wholeMonths(Time end) {
        if (before(end)) {
            int months = 0;
            Calendar c = getCalendar();
            do {
                c.add(Calendar.MONTH, 1);
                months++;
            } while (c.before(cal));
            return months;
        }
        return -1;
    }

    public Time nextMonth() {
        Time t = new Time(this);
        t.cal.add(MONTH, 1);
        return t;
    }

    public Datum getDatum() {
        return new Datum(this.cal.getTime());
    }

    public Datum getStartOfDay() {
     Datum datum = new Datum(this.getDatum());
        for (int type : new int[]{
            Calendar.HOUR_OF_DAY,
            Calendar.MINUTE,
            Calendar.SECOND,
            Calendar.MILLISECOND
        }) {
            cal.set(type, cal.getActualMinimum(type));
        }
        return datum;
    }

    @Override
    public int compareTo(Time o) {
        return cal.compareTo(o.cal);
    }
}
