/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

abstract class StringsDataFile extends StringsData {

    File file;
    StringsFilter stringsFilter;

    StringsDataFile(File file, StringsFilter stringsFilter, String[] header) {
        super(header);
        this.file = file;
        this.stringsFilter = stringsFilter;
    }

    /**
     * public StringsDataFile(String[] header, File file) { super(header);
     * if(file == null){ throw new IllegalArgumentException(); } this.file =
     * file; String extension = getFileExtension(file.getName());
     * if(extension.isEmpty()){ throw new IllegalArgumentException("No extension
     * found... "+file); }else if(extension.equalsIgnoreCase("csv")){ reader =
     * new StringsReader.StringsReaderSplitByChar(";", null) } }
     */
    Charset cs = Charset.defaultCharset();
    final private Charset ANSI = Charset.forName("Cp1252");

    @Override
    public void read() throws IOException {
        final List<String[]> entries = new ArrayList(content);
        try {
            read2();
        } catch (CharacterCodingException e) {
            content.clear();
            content.addAll(entries);
            cs = ANSI;
            try {
                read2();
            } catch (CharacterCodingException ex) {
                content.clear();
                content.addAll(entries);
                throw new IOException("Could not decode charset", e);
            }
        }
    }

    public void ExcelFackupCHECK() {
        /*
        
         System.out.println("The great Excel fackup detected in file: " + this.file);
         StringsFilter currentfilter = this.stringsFilter;
         this.stringsFilter = StringsFilter.getSplitExcelFackup();
         if (hasHeader(stringsFilter.read(headerLine))) {
         //read with revert
         while ((line = reader.readLine()) != null) {
         super.content.add(stringsFilter.read(line));
         }
         this.stringsFilter = currentfilter;
         return;
         }*/
        throw new UnsupportedOperationException("Not yet");
    }

    protected void read2() throws CharacterCodingException, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line;
        
        if(hasHeader()){
            line = reader.readLine();
            checkHeader(stringsFilter.read(line));
        }

        while ((line = reader.readLine()) != null) {
            super.content.add(stringsFilter.read(line));
        }
    }

    @Override
    public void write() throws IOException {
        //backup
        if (file.exists()) {
            File backup = new File(file.getAbsolutePath() + "");
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

    }

    static String getFileExtension(String name) {
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }

    @Override
    public String[] readFirstStrings() throws IOException {
        return stringsFilter.read(readFirstString());
    }
    
    public String readFirstString() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        return reader.readLine();
    }
}
