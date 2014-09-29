/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file.manager;

import file.ExcelCSV;
import file.IngCSV;
import file.StringsData;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Dennis
 */
public class DataManager {

    private final String initBasePath;
    private File baseDir;
    private FormatFactory format;
    private boolean recursive;
    private final FileCollector c = new FileCollector();

    public DataManager(FormatFactory format, boolean recursive) {
        this(EntriesFinder.simpleBasePath(false), format, recursive);
    }

    public DataManager(String basePath, FormatFactory format, boolean recursive) {
        this.initBasePath = basePath;
        this.baseDir = new File(basePath);
        while (!baseDir.isDirectory()) {
            Logger.getLogger(DataManager.class.getName()).log(Level.INFO, "Gaat fout op linux");
            Exception e = new Exception("BasePath "+basePath+" is not Directory");
            baseDir = baseDir.getParentFile();
            if (baseDir == null) {
                throw new RuntimeException(e);
            }else{
                try {
                    throw e;
                } catch (Exception ex) {
                    Logger.getLogger(DataManager.class.getName()).log(Level.INFO, "Specify other basepath", ex);
                }
            }
        }
        this.format = format;
        this.recursive = recursive;
    }

    

    private class FileCollector {

        boolean askCreation = true;
        int askCounter = 0;
        MyFilenameFilter filenameFilter;

        FileCollector() {
        }

        File searchSingleFile() {
            File[] files = listFiles();
            if (files.length == 0) {
                askCreation();
                files = listFiles();
            }
            if (files.length != 1) {
                try {
                    throw new FileNotFoundException(files.length + " " + filenameFilter.name + " found in stead of 1", initBasePath);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(DataManager.class.getName()).log(Level.INFO, null, ex);
                }
            }
            return files[0];
        }

        File[] searchMultipleFiles() {
            File[] files = listFiles();
            if (files.length == 0) {
                askCreation();
                files = listFiles();
            }

            if (files.length > 0) {
                return files;
            }
            throw new FileNotFoundException(filenameFilter.name, initBasePath);
        }

        File[] listFiles() {
            if (recursive) {
                List<File> files = new LinkedList<>();
                listFilesRecursively(files, baseDir);
                return files.toArray(new File[files.size()]);
            } else {
                return baseDir.listFiles(filenameFilter);
            }
        }

        void listFilesRecursively(List<File> empty, File dir) {
            for (File file : dir.listFiles()) {
                if (file.isFile()) {
                    if (filenameFilter.accept(dir, file.getName())) {
                        empty.add(file);
                    }
                } else if (file.isDirectory()) {
                    listFilesRecursively(empty, file);
                }
            }
        }

        void askCreation() {
            if (askCreation) {
                String fileName = filenameFilter.getNextFileName(baseDir.list(filenameFilter));
                if (fileName != null) {
                    if (JOptionPane.showConfirmDialog(null,
                            "Do you want to create "
                            + fileName + " in " + baseDir + "?")
                            == JOptionPane.YES_OPTION) {
                        try {
                            new File(baseDir, fileName).createNewFile();
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "File could not be created: " + ex);
                        }
                    }
                }
            }
        }

        public IngCSV collectING(Format f) {
            filenameFilter = f.filenameFilter;
            return IngCSV.get(searchSingleFile(), f.header);
        }

        public ExcelCSV collect(Format f) {
            filenameFilter = f.filenameFilter;
            return ExcelCSV.get(searchSingleFile(), f.header);
        }

        public ExcelCSV[] collects(Format f) {
            filenameFilter = f.filenameFilter;
            return ExcelCSV.get(searchMultipleFiles(), f.header);
        }
    }

    private StringsData personen;
    private StringsData[] kookdagen;
    private StringsData memory;
    private StringsData bewoonperiode;
    private StringsData bonnetjes;
    private StringsData bierBonnetjes;
    private StringsData kookSchuldDelers;
    private StringsData init;
    private StringsData afschriften;
    private StringsData betaaldVias;
    private StringsData contant;
    private StringsData afrekenings;

    public void read() throws IOException {
        personen.read();
        for (StringsData kookdagCsv : kookdagen) {
            kookdagCsv.read();
        }
        memory.read();
        bewoonperiode.read();
        bonnetjes.read();
        bierBonnetjes.read();
        kookSchuldDelers.read();
        init.read();
        afschriften.read();
        betaaldVias.read();
        contant.read();
        afrekenings.read();
    }/**
     * Check for excistence of crucial files: Personen Kookdagen/**
     * Check for excistence of crucial files: Personen Kookdagen/**
     * Check for excistence of crucial files: Personen Kookdagen/**
     * Check for excistence of crucial files: Personen Kookdagen
     */
    public void collect() {
        personen = c.collect(format.personen);
        kookdagen = c.collects(format.kookdagen);
        memory = c.collect(format.memoryFormat);
        bewoonperiode = c.collect(format.bewoonPeriodes);
        bonnetjes = c.collect(format.bonnetjes);
        bierBonnetjes = c.collect(format.bierBonnetjes);
        kookSchuldDelers = c.collect(format.kookSchuldDelers);
        init = c.collect(format.init);
        afschriften = c.collectING(format.afschriften);
        betaaldVias = c.collect(format.betaaldVias);
        contant = c.collect(format.contantRecords);
        afrekenings = c.collect(format.afrekenings);
    }

    public StringsData getPersonen() {
        return personen;
    }

    public FormatFactory getVersionFormat() {
        return format;
    }

    public StringsData[] getKookdagen() {
        return kookdagen;
    }

    public StringsData getMemory() {
        return memory;
    }

    public StringsData getBewoonperiode() {
        return bewoonperiode;
    }

    public StringsData getBonnetjes() {
        return bonnetjes;
    }

    public StringsData getKookSchuldDelers() {
        return kookSchuldDelers;
    }

    public StringsData getInit() {
        return init;
    }

    public StringsData getBierBonnetjes() {
        return bierBonnetjes;
    }

    public StringsData getAfschriften() {
        return afschriften;
    }

    public StringsData getBetaaldVias() {
        return betaaldVias;
    }

    public StringsData getStortingen() {
        return contant;
    }

    public StringsData getAfrekenings() {
        return afrekenings;
    }
}
