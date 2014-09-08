/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tijd;

/**
 *
 * @author Dennis
 */
public class Interval {

    Time begin;
    Time end;

    /**
     * tot nu
     */
    public Interval(final Time time) {
        this(time,  Time.now);
    }

    public Interval(Time begin, Time end) {
        if(begin == null || end == null){
            throw new IllegalArgumentException();
        }
        this.begin = begin;
        this.end = end;
    }

    public int wholeMonths() {
        return begin.wholeMonths(end);
    }

    public Time getEnd() {
        return end;
    }

    public Time getBegin() {
        return begin;
    }

    @Override
    public String toString() {
        return "[" + begin + ", " + end + "]";
    }

}
