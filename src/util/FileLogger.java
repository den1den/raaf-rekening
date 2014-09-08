/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dennis
 */
public class FileLogger {

    public static FileLogger getInstance() {
        return new FileLogger();
    }

    private FileLogger() {
        file = new File("logger("+System.currentTimeMillis()+").txt");
    }

    File file;

    public final void log(String s) {
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(FileLogger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(file))){
            bw.write(s);
            bw.flush();
        } catch (IOException ex) {
            Logger.getLogger(FileLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void println(Object o) {
        log(o.toString());
        log(System.lineSeparator());
    }
}
