/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file;

import java.nio.charset.CharacterCodingException;

/**
 *
 * @author Dennis
 */
abstract class StringsFilter {

    final static int unknownChar = 65533;

    public final String[] read(String notNull) throws CharacterCodingException {
        if (notNull.indexOf(unknownChar) != -1) {
            throw new CharacterCodingException();
        }
        return readImpl(notNull);
    }

    protected abstract String[] readImpl(String notNull);

    public abstract String write(String[] notNull);

    static StringsFilter splitByPuntComma = null;
    static StringsFilter splitByComma = null;
    static StringsFilter splitByAanhalingsTekens = null;
    static StringsFilter splitExcelFackup = null;

    static StringsFilter getSplitByComma() {
        if (splitByComma == null) {
            splitByComma = new SplitByChar(",");
        }
        return splitByComma;
    }

    static StringsFilter getSplitByPuntComma() {
        if (splitByPuntComma == null) {
            splitByPuntComma = new SplitByChar(";");
        }
        return splitByPuntComma;
    }

    static StringsFilter getSplitByAanhalingsTekens() {
        if (splitByAanhalingsTekens == null) {
            splitByAanhalingsTekens = new SplitByStringSE("\",\"", "\"", "\"");
        }
        return splitByAanhalingsTekens;
    }
    
    static StringsFilter getSplitExcelFackup() {
        if (splitExcelFackup == null) {
            splitExcelFackup = new ExcelFuckup();
        }
        return splitExcelFackup;
    }

    private static class SplitByStringSE extends StringsFilter {

        String splittingString;
        String prefix;
        String suffix;

        public SplitByStringSE(String splittingString, String start, String end) {
            this.splittingString = splittingString;
            this.prefix = start;
            this.suffix = end;
        }

        @Override
        protected String[] readImpl(String notNull) {
            if (notNull.startsWith(prefix) && notNull.endsWith(suffix)) {
                String trim = notNull.substring(prefix.length(), notNull.length() - suffix.length());
                return trim.split(splittingString);
            }
            throw new RuntimeException("Cannot read " + notNull);
        }

        @Override
        public String write(String[] notNull) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

    private static class SplitByChar extends StringsFilter {

        private String splitChar;

        public SplitByChar(String splitChar) {
            this.splitChar = splitChar;
        }

        @Override
        public String[] readImpl(String notNull) {
            return notNull.split(splitChar);
        }

        @Override
        public String write(String[] notNull) {
            if (notNull.length == 0) {
                return "";
            }
            StringBuilder sb = new StringBuilder(notNull[0]);
            for (int i = 1; i < notNull.length; i++) {
                sb.append(splitChar).append(notNull[i]);
            }
            return sb.toString();
        }

    }

    private static class ExcelFuckup extends StringsFilter {

        @Override
        protected String[] readImpl(String notNull) {
            notNull = notNull.substring(1);//skip first
            int i = notNull.indexOf(",\"\"");
            String first = notNull.substring(0, i);
            notNull = notNull.substring(i + 3, notNull.length() - 3);
            String[] last = notNull.split("\"\",\"\"");
            String[] result = new String[last.length + 1];
            result[0] = first;
            System.arraycopy(last, 0, result, 1, last.length);
            return result;
        }

        @Override
        public String write(String[] notNull) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }
}
