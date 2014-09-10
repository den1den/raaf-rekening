/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file.manager;

import data.AankoopCat;
import data.Afschrift;
import data.BewoonPeriode;
import data.BierBonnetje;
import data.Bonnetje;
import data.GeregistreerdeBetaling;
import data.Incasso;
import data.Kookdag;
import data.Persoon;
import data.Winkel;
import data.memory.Memory;
import geld.ReferentieSimple;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import parsers.MyParseException;
import parsers.ParserFactory;
import parsers.ParserFactory.ListParser;
import parsers.ParserFactory.Parser;
import parsers.ParserFactory.SetParser;
import parsers.ParserFactory.VoidParser;
import parsers.stringparser.StringParser;
import tijd.Datum;
import tijd.Interval;

/**
 *
 * @author Dennis
 */
public class FormatFactory {

    private final int version;
    private final ParserFactory parserFactory = new ParserFactory();
    private final Memory memoryInstance = parserFactory.getMemory();

    public FormatFactory() {
        this(1);
    }

    public FormatFactory(int version) {
        this.version = version;
        personen = createPersoon();
        afschriften = createAfschrift();
        bonnetjes = createBonnetjes();
        kookdagen = createKookdagen();
        memoryFormat = createMemory();
        bewoonPeriodes = createBewoonPeriodes();
        raafRekening = createRekening();
        kookSchuldDelers = createKookSchuldDelers();
        bierBonnetjes = createBierBonnetjes();
    }

    public final Format<Set<GeregistreerdeBetaling>> personen;
    public final Format<Set<Afschrift>> afschriften;
    public final Format<Set<Bonnetje>> bonnetjes;
    public final Format<List<Kookdag>> kookdagen;
    public final Format<Memory> memoryFormat;
    public final Format<Set<BewoonPeriode>> bewoonPeriodes;
    public final Format<IntegerParsable> raafRekening;
    public final Format<Map<Persoon, Persoon>> kookSchuldDelers;
    public final Format<Set<BierBonnetje>> bierBonnetjes;

    private Format<Set<GeregistreerdeBetaling>> createPersoon() {
        String[] header;
        MyFilenameFilter filenameFilter;
        SetParser<GeregistreerdeBetaling> parser;

        if (version >= 2) {
            header = new String[]{"Naam", "Kwijtschelden", "Betaald"};
        } else {
            header = new String[]{"Naam", "Kwijtschelden"};
        }

        filenameFilter = new MyFilenameFilter("personen");

        if (version >= 2) {
            parser = parserFactory.new SetParser<GeregistreerdeBetaling>() {

                @Override
                protected int getParseLevel() {
                    return 10;
                }

                @Override
                protected GeregistreerdeBetaling parseLine(String[] strings) {
                    String naam;
                    boolean kwijtschelden;
                    int betaald;

                    if (strings.length < 1 || strings.length > 3) {
                        throw new MyParseException(1);
                    }

                    int index = 0;

                    naam = strings[index];

                    if (++index < strings.length) {
                        kwijtschelden = Boolean.parseBoolean(strings[index]);
                    } else {
                        kwijtschelden = false;
                    }

                    if (++index < strings.length) {
                        if (StringParser.INTEGER.has(strings[index])) {
                            betaald = StringParser.INTEGER.get();
                        } else {
                            throw new MyParseException(index);
                        }
                    } else {
                        betaald = 0;
                    }

                    Persoon p = parserFactory.getMemory().personen.find(naam);
                    if (p.getKwijtschelden() == null) {
                        p.setKwijtschelden(kwijtschelden);
                    } else if (p.kwijtschelden() != kwijtschelden) {
                        throw new MyParseException(0);
                    }
                    
                    return GeregistreerdeBetaling.getContant(null);
                }
            };
        } else {
            parser = parserFactory.new SetParser<GeregistreerdeBetaling>() {

                @Override
                protected GeregistreerdeBetaling parseLine(String[] strings) {
                    String naam;
                    boolean kwijtschelden;

                    if (strings.length == 1) {
                        kwijtschelden = false;
                    } else if (strings.length == 2) {
                        kwijtschelden = Boolean.parseBoolean(strings[1]);
                    } else {
                        throw new MyParseException(1);
                    }

                    naam = strings[0];

                    Persoon p = memoryInstance.personen.find(naam);
                    if (p.getKwijtschelden() == null) {
                        p.setKwijtschelden(kwijtschelden);
                    } else if (p.kwijtschelden() != kwijtschelden) {
                        throw new MyParseException(0);
                    }
                    return null;
                }

                @Override
                protected int getParseLevel() {
                    return 10;
                }

            };
        }
        return new Format<>(filenameFilter, header, parser);
    }

    private Format<Set<Afschrift>> createAfschrift() {
        String[] header;
        MyFilenameFilter filenameFilter;
        SetParser<Afschrift> parser;

        header = new String[]{"Datum", "Naam / Omschrijving", "Rekening", "Tegenrekening", "Code", "Af Bij", "Bedrag (EUR)", "MutatieSoort", "Mededelingen"};
        filenameFilter = new MyFilenameFilter("ing");
        parser = parserFactory.new SetParser<Afschrift>() {
            final StringParser<Datum> DATUM_PARSER = StringParser.getDayParser(
                    new SimpleDateFormat("dd-MM-yy"));

            @Override
            protected Afschrift parseLine(String[] strings) {
                if (strings.length != 9) {
                    throw new MyParseException.Length();
                }
                Datum datum;
                String van;
                String dezeRekening;
                String vanRekening;
                String code;
                boolean af;
                int bedrag;
                String mutatieSoort;
                String mededeling;

                int index = 0;

                if (!DATUM_PARSER.has(strings[index])) {
                    throw new MyParseException(index);
                }
                datum = DATUM_PARSER.get();

                van = strings[++index];

                dezeRekening = strings[++index];

                vanRekening = strings[++index];

                code = strings[++index];

                switch (strings[++index]) {
                    case "Af":
                        af = true;
                        break;
                    case "Bij":
                        af = false;
                        break;
                    default:
                        throw new MyParseException(index);
                }

                if (!StringParser.INTEGER.has(strings[++index])) {
                    throw new MyParseException(index);
                }
                bedrag = StringParser.INTEGER.get();

                mutatieSoort = strings[++index];

                mededeling = strings[++index];

                return new Afschrift(datum, van, vanRekening, dezeRekening, code, af, bedrag, mutatieSoort, mededeling);
            }
        };

        return new Format<>(filenameFilter, header, parser);
    }

    private Format<Set<Bonnetje>> createBonnetjes() {
        String[] header;
        MyFilenameFilter filenameFilter;
        SetParser<Bonnetje> parser;

        header = new String[]{"CSVID", "Bedrag (centen)", "Gekocht door", "Pas (eindigd op)", "Datum (dmy)", "Winkel", "Spullen", "..."};
        filenameFilter = new MyFilenameFilter("bonnetjes");
        parser = parserFactory.new SetParser<Bonnetje>() {
            final StringParser<Integer> intParser = StringParser.INTEGER;
            final StringParser<Datum> dateParser = StringParser.getFastestDayParser();

            private int id;
            private int bedrag;
            private Persoon persoon;
            private String pasEindigd;
            private Datum date;
            private Winkel winkel;
            private List<AankoopCat> items;

            @Override
            protected Bonnetje parseLine(String[] strings) {
                if (strings.length < 7) {
                    throw new MyParseException.Length();
                }
                int index = 0;

                if (!intParser.has(strings[index])) {
                    throw new MyParseException(index);
                }
                id = intParser.get();

                if (!intParser.has(strings[++index])) {
                    throw new MyParseException(index);
                }
                bedrag = intParser.get();

                String persoonNaam = strings[++index];

                pasEindigd = strings[++index];

                if (!dateParser.has(strings[++index])) {
                    throw new MyParseException(index);
                }
                date = dateParser.get();

                String winkelNaam = strings[++index];

                items = new ArrayList<>(strings.length - index);
                while (++index < strings.length) {
                    AankoopCat ac = memoryInstance.aankoopCats.find(strings[index]);
                    items.add(ac);
                }

                //verfijnen
                if (!winkelNaam.isEmpty()) {
                    winkel = memoryInstance.winkels.get(winkelNaam);
                }
                if (!persoonNaam.isEmpty()) {
                    persoon = memoryInstance.personen.get(persoonNaam);
                }
                if (persoon == null) {
                    persoon = memoryInstance.personen.findLast(persoonNaam, pasEindigd);
                }

                Bonnetje bonnetje = new Bonnetje(id, bedrag, persoon, pasEindigd, date, winkel, items);
                return bonnetje;
            }
        };
        return new Format<>(filenameFilter, header, parser);
    }

    private Format< List<Kookdag>> createKookdagen() {
        String[] header;
        MyFilenameFilter filenameFilter;
        ListParser<Kookdag> parser;

        header = new String[]{"n", "Prijs (in centen)", "Kok", "Meeters", "..."};
        filenameFilter = MyFilenameFilter.getMeervoudCombo(
                "kookdag", "kookdagen", ".csv");
        parser = parserFactory.new ListParser() {

            private static final String REGEX_DIGIT_SPLIT = "(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)";

            @Override
            protected Kookdag parseLine(String[] strings) {
                if (strings.length < 4) {
                    throw new MyParseException.Length();
                }
                int index = 0;

                int ID;
                int bedrag;
                Persoon kok;
                Map<Persoon, Integer> meeters = new HashMap<>(strings.length - 3);

                try {
                    if (!StringParser.INTEGER.has(strings[index])) {
                        throw new MyParseException(index, "No int found");
                    }
                    ID = StringParser.INTEGER.get();
                } catch (NumberFormatException e) {
                    throw new MyParseException(index, e);
                }

                index++;

                try {
                    if (!StringParser.INTEGER.has(strings[index])) {
                        throw new MyParseException(index, "No int found");
                    }
                    bedrag = StringParser.INTEGER.get();
                } catch (NumberFormatException e) {
                    throw new MyParseException(index, e);
                }

                index++;

                String kokString = strings[index];
                String[] kokStringSplit = kokString.split(REGEX_DIGIT_SPLIT);
                if (kokStringSplit.length == 1) {
                    kok = memoryInstance.personen.find(kokStringSplit[0]);
                } else if (kokStringSplit.length == 2) {
                    kok = memoryInstance.personen.find(kokStringSplit[0]);
                    throw new MyParseException(index,
                            "Not valid split for kok yet: "
                            + Arrays.deepToString(kokStringSplit)
                            + " kok = " + kok);
                } else {
                    throw new MyParseException(index, "Could only id: " + Arrays.deepToString(kokStringSplit));
                }

                for (index++; index < strings.length; index++) {
                    String meeterString = strings[index];

                    String[] meeterStringSplit = meeterString.split(REGEX_DIGIT_SPLIT);

                    Persoon meeter;
                    int times;
                    if (meeterStringSplit.length == 1) {
                        meeter = memoryInstance.personen.find(meeterStringSplit[0]);
                        times = 1;

                    } else if (meeterStringSplit.length == 2) {
                        meeter = memoryInstance.personen.find(meeterStringSplit[0]);

                        try {
                            times = Integer.parseInt(meeterStringSplit[1]);
                        } catch (NumberFormatException e) {
                            throw new MyParseException(index, e);
                        }
                    } else {
                        throw new MyParseException(index,
                                "Not valid split: (" + Arrays.deepToString(kokStringSplit) + ")");
                    }
                    Integer prev = meeters.get(meeter);
                    if (prev == null) {
                        //doesnt excist
                        meeters.put(meeter, times);
                    } else {
                        //was al teogevoegd
                        if (kok.equals(meeter)) {
                            times--;
                        }
                        meeters.put(meeter, prev + times);
                    }
                }
                if (!meeters.containsKey(kok)) {
                    meeters.put(kok, 1);
                }
                return new Kookdag(ID, bedrag, kok, meeters);
            }

        };

        return new Format<>(filenameFilter, header, parser);
    }

    private Format<Memory> createMemory() {
        String[] header;
        MyFilenameFilter filenameFilter;
        Parser<Memory> parser;

        header = new String[]{"Type", "various", "(winkel categorie)", "(winkel andere namen)"};
        filenameFilter = new MyFilenameFilter("memory");
        parser = parserFactory.new SingleParser<Memory>( 
             
             
             
             
             
             
             
             
            memoryInstance) {
private static final String typePersoon = "nickname";
            private static final String typeRekening = "rekening";
            private static final String typeIncasso = "incasso";
            private static final String typeWinkel = "winkel";
            private static final String typeAankoopCat = "aankoop-categorie";
            String type;

            @Override
            protected void parseLine(String[] strings) {
                if (strings.length < 3) {
                    throw new MyParseException.Length();
                }
                int index = 0;
                if (!strings[index].isEmpty()) {
                    type = strings[index];
                }
                String subject = strings[++index];
                switch (type) {
                    case typePersoon:
                        Persoon p = memoryInstance.personen.find(subject);
                        while (index < strings.length) {
                            memoryInstance.personen.put(p, strings[index++]);
                        }
                        break;
                    case typeRekening:
                        p = memoryInstance.personen.find(subject);
                        while (++index < strings.length) {
                            memoryInstance.personen.putRek(p, strings[index]);
                        }
                        break;
                    case typeIncasso:
                        String rek = strings[++index];
                        Incasso i = new Incasso(subject, rek);
                        memoryInstance.incassos.put(i, rek);
                        break;
                    case typeWinkel:
                        String categorieNaam = strings[++index];
                        AankoopCat defaultCat = memoryInstance.aankoopCats.find(categorieNaam);

                        String[] medeDelingen
                                = new String[strings.length - 1 - index];

                        System.arraycopy(strings, index, medeDelingen, 0, medeDelingen.length);

                        Winkel w = memoryInstance.winkels.get(subject);
                        if (w == null) {
                            w = new Winkel(subject, defaultCat);
                            memoryInstance.winkels.put(w, subject);
                        } else {
                            w.foundDefaultCat(defaultCat);
                        }
                        for (String mededeling : medeDelingen) {
                            memoryInstance.winkels.putMede(w, mededeling);
                        }
                        break;
                    case typeAankoopCat:
                        AankoopCat cat = memoryInstance.aankoopCats.find(subject);

                        while (++index < strings.length) {
                            memoryInstance.aankoopCats.put(cat, strings[index]);
                        }
                        break;
                    default:
                        throw new MyParseException(index, "Niet gefenieerd");
                }
            }

            @Override
            protected int getParseLevel() {
                return 0;
            }
        };
        return new Format<>(filenameFilter, header, parser);
    }

    private Format<Set<BewoonPeriode>> createBewoonPeriodes() {
        String[] header;
        MyFilenameFilter filenameFilter;
        SetParser<BewoonPeriode> parser;

        header = new String[]{"Naam", "Periode (van)", "Periode (tot)", "..."};
        filenameFilter = new MyFilenameFilter("bewoners");
        parser = parserFactory.new SetParser<BewoonPeriode>() {
            private final StringParser<Datum> dayparser
                    = StringParser.getDayParser(new SimpleDateFormat("d-M-y"));

            @Override
            protected BewoonPeriode parseLine(String[] strings) {
                if (strings.length < 2) {
                    throw new MyParseException.Length();
                }

                int index = 0;

                Persoon subject = memoryInstance.personen.find(strings[index]);
                BewoonPeriode last = null;

                while (++index < strings.length) {
                    Interval interval;

                    if (!dayparser.has(strings[index])) {
                        throw new MyParseException(index, "Could not derive time");
                    }
                    Datum start = dayparser.get();
                    if (++index < strings.length) {
                        if (!dayparser.has(strings[index])) {
                            throw new MyParseException(index, "Could not derive time");
                        }
                        interval = new Interval(start, dayparser.get());
                    } else {
                        interval = new Interval(start);
                    }

                    if (last != null) {
                        instance.add(last);
                    }
                    last = new BewoonPeriode(subject, interval);
                }
                return last;
            }
        };

        return new Format<>(filenameFilter, header, parser);
    }

    private Format<IntegerParsable> createRekening() {
        String[] header;
        MyFilenameFilter filenameFilter;
        Parser<IntegerParsable> parser;

        header = null;
        filenameFilter = new MyFilenameFilter("bewoners");
        parser = parserFactory.new SingleParser<IntegerParsable>(
                 
            new IntegerParsable()) {@Override
            protected void parseLine(String[] strings) {
                if (strings.length != 2) {
                    throw new MyParseException.Length();
                }
                int i = 0;
                if (!strings[i].equals("start bedrag")) {
                    throw new MyParseException(i);
                }
                int startBedrag;
                if (!StringParser.INTEGER.has(strings[++i])) {
                    throw new MyParseException(i);
                }
                this.instance.setInteger(StringParser.INTEGER.get());
            }
        };

        return new Format<>(filenameFilter, header, parser);
    }

    private Format<Map<Persoon, Persoon>> createKookSchuldDelers() {
        String[] header;
        MyFilenameFilter filenameFilter;
        ParserFactory.MapParser<Persoon> parser;

        header = new String[]{"Betaalt voor", "Persoon", "..."};
        filenameFilter = new MyFilenameFilter("kookSchuldDelen");
        parser = parserFactory.new MapParser<Persoon>() {

            @Override
            protected void parseLine(String[] strings) {
                if (strings.length < 2) {
                    throw new MyParseException.Length();
                }
                int i = 0;
                Persoon value = memoryInstance.personen.find(strings[i]);
                while (++i < strings.length) {
                    instance.put(memoryInstance.personen.find(strings[i]), value);
                }
                //return instance.new Entry<Object, Object>() {};
            }
        };

        return new Format<>(filenameFilter, header, parser);
    }

    private Format<Set<BierBonnetje>> createBierBonnetjes() {
        String[] header;
        MyFilenameFilter filenameFilter;
        SetParser<BierBonnetje> parser;

        header = null;
        filenameFilter = new MyFilenameFilter("bierBonnetjes");
        parser = parserFactory.new SetParser<BierBonnetje>() {
            final StringParser<Datum> DATEPARSER = StringParser.getFastestDayParser();

            @Override
            protected BierBonnetje parseLine(String[] strings) {
                String merk;
                int kratten;
                int totaalPrijs;
                Datum datum;
                Winkel winkel;

                if (strings.length < 4 || strings.length > 5) {
                    throw new MyParseException.Length();
                }
                int i = 0;
                merk = strings[i];

                if (!StringParser.INTEGER.has(strings[++i])) {
                    throw new MyParseException(i);
                }
                kratten = StringParser.INTEGER.get();

                if (!StringParser.INTEGER.has(strings[++i])) {
                    throw new MyParseException(i);
                }
                totaalPrijs = StringParser.INTEGER.get();

                if (!DATEPARSER.has(strings[++i])) {
                    throw new MyParseException(i);
                }
                datum = DATEPARSER.get();

                winkel = memoryInstance.winkels.get(strings[++i]);
                if (winkel == null) {
                    winkel = new Winkel(
                            strings[i],
                            memoryInstance.aankoopCats.find("Bier"));
                }

                return new BierBonnetje(merk, kratten, totaalPrijs, datum, winkel);
            }
        };
        return new Format<>(filenameFilter, header, parser);
    }
}
