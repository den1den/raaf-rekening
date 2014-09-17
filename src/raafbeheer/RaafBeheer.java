/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raafbeheer;

import data.Afschrift;
import data.BewoonPeriode;
import data.BierBonnetje;
import data.Bonnetje;
import data.Kookdag;
import data.Persoon;
import data.memory.Memory;
import file.manager.DataManager;
import file.manager.FormatFactory;
import geld.policy.Policy;
import geld.RaafRekening;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import tijd.Datum;
import tijd.IntervalDatums;
import util.diplay.ResultPrintStream;

/**
 *
 * @author Dennis
 */
public class RaafBeheer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        File f = new File("ERROR");
        f.createNewFile();
        
        int version = 4;
        RaafBeheer raafBeheer;
        try {
            raafBeheer = new RaafBeheer(version);
        } catch (ExceptionInInitializerError e) {
            System.out.println("Big mistake!");
            throw e;
        }
        if (args.length > 0 && args[0].equalsIgnoreCase("testing")) {
            System.out.println();
            System.out.println("Testing");
            System.out.println();
            raafBeheer.testing();
        } else if (args.length > 0 && args[0].equalsIgnoreCase("combining")) {
            System.out.println();
            System.out.println("Combining");
            System.out.println();
            raafBeheer.combining();
        } else {
            System.out.println();
            System.out.println("For real");
            System.out.println();
            raafBeheer.forReal();
        }
        
        Toolkit.getDefaultToolkit().beep();
        f.delete();
    }

    private final int version;
    private final DataManager files;

    private final FormatFactory formats;
    private final ResultPrintStream result;

    private Policy policy = null;
    private Memory memory = null;

    RaafBeheer(int version) {
        this.version = version;
        this.formats = new FormatFactory(version);
        this.files = new DataManager(formats, false);
        this.result = new ResultPrintStream(version);
    }

    void testing() {
        files.collect();
        try {
            files.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        memory = formats.memoryFormat.parser.parse(files.getMemory());
        formats.personen.parser.parse(files.getPersonen());
        List<Kookdag> kookdagen = formats.kookdagen.parser.parse(files.getKookdagen());
        Set<Afschrift> afschriften = formats.afschriften.parser.parse(files.getAfschriften());
        Set<BewoonPeriode> bewoonPeriodes = formats.bewoonPeriodes.parser.parse(files.getBewoonperiode());
        Set<Bonnetje> bonnetjes = formats.bonnetjes.parser.parse(files.getBonnetjes());
        Set<BierBonnetje> bierBonnetjes = formats.bierBonnetjes.parser.parse(files.getBierBonnetjes());
        Map<Persoon, Persoon> kookSchuldDelers = formats.kookSchuldDelers.parser.parse(files.getKookSchuldDelers());

        IntervalDatums oude_spreadsheet = IntervalDatums.tot(new Datum(2013, 8, 6));
        System.out.println("periode: "+oude_spreadsheet);
        
        policy = new Policy(version, memory, oude_spreadsheet);
        //LeenRekening kookR = new LeenRekening.Easy("Kook Rekening");
        //policy.verrekenKookdagen(kookdagen, kookSchuldDelers, kookR);
        //result.listResultaat(memory.personen.getAll(), kookR);
        //result.listEnkel(memory.personen.get("Mark"), kookR);

         RaafRekening raafRekening = new RaafRekening("Raaf Rekening");
         policy.verrekenBewoonPeriodes(bewoonPeriodes, raafRekening);

         policy.verrekenBonnetjes(bonnetjes, raafRekening);

         policy.verwerkAfschriften(afschriften, bonnetjes, raafRekening);
         
         result.listEnkel(raafRekening);
        //result.showFactuurs(fs);
        //result.showDetailledTov(memory.personen.get("Dennis"), perR, bonR, afR);
    }

    void forReal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Deprecated
    void combining() {
        if (true) {
            throw new UnsupportedOperationException("Deptricated, use: E:\\Dropbox\\RaafEntries");
        }
        JFileChooser jFileChooser = new JFileChooser();
        File directory;
        boolean recursive;

        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        while ((jFileChooser.showOpenDialog(null)) != JFileChooser.APPROVE_OPTION) {
        }
        directory = jFileChooser.getSelectedFile();
        int result = JOptionPane.showConfirmDialog(null, "Recursive?");

        if (result == JOptionPane.YES_OPTION) {
            recursive = true;
        } else if (result == JOptionPane.NO_OPTION) {
            recursive = false;
        } else {
            return;
        }

        //Collector collector = new Collector(directory, recursive);
        //collector.run();
    }

}
