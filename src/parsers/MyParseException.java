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
public class MyParseException extends RuntimeException {

    String[] lineContents = null;
    final int index; //can be < 0

    int lineNumber = 0; //sometimes
    private Source source = null;

    private MyParseException() {
        this(-1);
    }

    public MyParseException(int index) {
        this.index = index;
    }

    public MyParseException(int index, Throwable cause) {
        super(cause);
        this.index = index;
    }

    public MyParseException(int index, String message) {
        super(message);
        this.index = index;
    }

    public void setSourceAndNumber(Source source, int lineNumber) {
        this.source = source;
        this.lineNumber = lineNumber;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        if (lineContents != null) {
            message += " " + explicit(lineContents, index);
        }
        if(source != null){
            message += " on line "+lineNumber + " in file "+source;
        }
        return message;
    }

    private static String explicit(String[] lineContents, int index) {
        String s = "[";
        for (int i = 0; i < lineContents.length; i++) {
            if (i == index) {
                s += ">";
            }
            s += lineContents[i];
            if (i == index) {
                s += "<";
            }
            s += ", ";
        }
        return s.substring(0, s.length() - 2) + "]";
    }

    public static class Length extends MyParseException {

        public Length() {
        }

        @Override
        public String getMessage() {
            return "Verkeerde lengte: "+super.getMessage();
        }
        
    }
}
