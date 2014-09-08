/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tijd;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Datum extends Time {

    static final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    /**
     * 
     * @param year
     * @param month [1,12]
     * @param day 
     */
    public Datum(int year, int month, int day) {
        super(year, month, day);
    }

    /**
     * 
     * @param year
     * @param month [1,12]
     */
    public Datum(int year, int month) {
        super(year, month);
    }

    public Datum(Datum datum) {
        super(datum);
    }

    public Datum(java.util.Date utilDate) {
        super(utilDate);
    }
}
