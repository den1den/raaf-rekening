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
import geld.Policy;
import geld.RekeningHouderContantI;
import geld.TRecord;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import util.diplay.PrintStreamResult;
import util.diplay.Result;

/**
 *
 * @author Dennis
 */
public class RaafBeheer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int version = 3;

        RaafBeheer raafBeheer = new RaafBeheer(version);
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
    }

    private final DataManager files;
    private final Policy policy;
    private final FormatFactory formats;
    private final RekeningHouderContantI raafRekening;
    private final RekeningHouderContantI kookRekening;
    private final Result result;

    RaafBeheer(int version) {
        formats = new FormatFactory(version);
        files = new DataManager(formats, false);
        raafRekening = new RekeningHouderContantI("RaafRekening");
        kookRekening = new RekeningHouderContantI("KookLijstRekening");
        policy = new Policy(version, raafRekening, kookRekening);
        result = new PrintStreamResult(version);
    }

    void testing() {
        files.collect();
        try {
            files.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Memory memory = formats.memoryFormat.parser.parse(files.getMemory());
        formats.personen.parser.parse(files.getPersonen());
        List<Kookdag> kookdagen = formats.kookdagen.parser.parse(files.getKookdagen());
        Set<Afschrift> afschriften = formats.afschriften.parser.parse(files.getAfschriften());
        Set<BewoonPeriode> bewoonPeriodes = formats.bewoonPeriodes.parser.parse(files.getBewoonperiode());
        Set<Bonnetje> bonnetjes = formats.bonnetjes.parser.parse(files.getBonnetjes());
        Set<BierBonnetje> bierBonnetjes = formats.bierBonnetjes.parser.parse(files.getBierBonnetjes());
        Map<Persoon, Persoon> kookSchuldDelers = formats.kookSchuldDelers.parser.parse(files.getKookSchuldDelers());
        
        policy.setMemory(memory);

        policy.verrekenKookdagen(kookdagen, kookSchuldDelers);

        result.showDepts(memory.personen.getAll(), kookRekening);
        System.exit(0);
        
        
        List<TRecord> fs = policy.verrekenBewoonPeriodes(bewoonPeriodes);
        fs.addAll(policy.verrekenBonnetjes(bonnetjes));
        fs.addAll(policy.verwerkAfschriften(afschriften, bonnetjes));

        //result.showFactuurs(fs);
        result.showBalances(memory.personen.getAll());
        result.showDetailed(memory.personen.get("Dennis"));
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
