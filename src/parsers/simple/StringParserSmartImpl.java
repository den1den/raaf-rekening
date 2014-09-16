/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package parsers.simple;


abstract class StringParserSmartImpl<O, Fs> implements StringParserSmart<O> {

        private int index = -1;
        private final Fs[] formatters;

        protected StringParserSmartImpl(Fs[] formatters) {
            this.index = formatters.length;
            this.formatters = formatters;
        }

        public O find(String s) {
            O object = null;
            for (this.index = 0; this.index < formatters.length; this.index++) {
                object = parse(s, formatters[index]);
                if (object != null) {
                    return object;
                }
            }
            this.index = -1;
            return null;
        }

        @Override
        public O parse(String s) {
            if (index == -1) {
                return find(s);
            }
            return parse(s, formatters[index]);
        }

        protected abstract O parse(String s, Fs formatter);
    }
