/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers.simple;

import parsers.MyParseException;
import tijd.Datum;
import tijd.IntervalDatums;

/**
 *
 * @author Dennis
 */
public abstract class StringsParser<C> {

    public C parseSepa(String... entry) {
        return parse(entry);
    }

    public abstract C parse(String[] entry);

    public static class IntervalParser extends StringsParser<IntervalDatums> {

        StringParser<Datum> dateParser;

        public IntervalParser() {
            this(new StringParser.NormalDatum());
        }

        public IntervalParser(StringParser<Datum> dateParser) {
            this.dateParser = dateParser;
        }

        @Override
        public IntervalDatums parse(String... entry) {
            if (entry.length < 1 || entry.length > 2) {
                throw new MyParseException.Length();
            }
            //igg 1
            Datum eerst = dateParser.parse(entry[0]);

            if (entry.length == 2) {
                Datum tweede = dateParser.parse(entry[1]);

                return new IntervalDatums(eerst, tweede);
            } else {
                return IntervalDatums.vanaf(eerst);
            }
        }

    }
}
