/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers.stringparser;

/**
 *
 * @author Dennis
 */
public abstract class StringParserWInit<O> extends StringParser<O> {

    /**
     * Check the format and keep it that format
     *
     * @param s
     * @return
     */
    public abstract boolean init(String s);

    abstract static class StringParserWInitImpl<O, Helper> extends StringParserWInit<O> {

        protected int index = -1; //not set
        protected final Helper[] helpers;

        public StringParserWInitImpl(Helper[] objects) {
            this.helpers = objects;
        }

        @Override
        public boolean init(String s) {
            for (index = 0; index < helpers.length; index++) {
                if (has(s)) {
                    return true;
                }
            }
            return false;
        }
    }
}
