/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file.manager;

import file.StringsData;

/**
 *
 * @author Dennis
 */
public abstract class Combiner {

    /**
     *
     * @param <P>
     * @param data1
     * @param data2
     * @return true if success, false => data1, data2 are untouched
     */
    abstract <P extends StringsData> boolean combineTo1(P data1, P data2);

    static Combiner SeqByDate(int dateField) {
        return new Combiner() {

            @Override
            <P extends StringsData> boolean combineTo1(P data1, P data2) {
                throw new UnsupportedOperationException();
                /*
                 if(!data1.checkHeader(data2.getHeader())){
                 return false;
                 }
                 List<TimeEstimate> firstDates;
                 List<TimeEstimate> secondDates;
                 return false;*/
            }
        };
    }
}
