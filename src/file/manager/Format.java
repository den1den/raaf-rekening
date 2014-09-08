/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file.manager;

import parsers.ParserFactory;
import parsers.ParserFactory.Parser;

/**
 *
 * @author Dennis
 * @param <ParseOutputC>
 */
public class Format<ParseOutputC> {

    public final MyFilenameFilter filenameFilter;
    public final String[] header;
    public final ParserFactory.Parser<ParseOutputC> parser;

    public Format(MyFilenameFilter filenameFilter, String[] header, Parser<ParseOutputC> parser) {
        if (filenameFilter == null || parser == null) {
            throw new IllegalArgumentException();
        }
        this.filenameFilter = filenameFilter;
        this.header = header;
        this.parser = parser;
    }
    
    
}
