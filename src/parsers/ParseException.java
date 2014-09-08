/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package parsers;

/**
 *
 * @author Dennis
 */
class ParseException extends Exception {

        Source source;
        int lineNumber;
        String[] line;
        int index;

        public ParseException(String[] line, int index) {
            this.line = line;
            this.index = index;
        }

        public void setSource(Source source) {
            this.source = source;
        }

        public void setLine(int line) {
            this.lineNumber = line;
        }
    }
