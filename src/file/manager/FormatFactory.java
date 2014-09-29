/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file.manager;

import data.AankoopCat;
import data.Afrekening;
import data.Afschrift;
import data.BetaaldVia;
import data.BewoonPeriode;
import data.BierBonnetje;
import data.Bonnetje;
import data.Incasso;
import data.Kookdag;
import data.Persoon;
import data.ContantRecord;
import data.ContantRecord.DoubleContantRecordSet;
import data.Winkel;
import data.memory.Memory;
import data.types.HasNaam;
import file.StringsData;
import geld.Referentie;
import geld.rekeningen.Event;
import geld.rekeningen.Rekening;
import geld.rekeningen.RekeningLeen;
import geld.rekeningen.RekeningLeenBudget;
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
import parsers.simple.StringParser;
import parsers.simple.StringsParser;
import tijd.Datum;
import tijd.IntervalDatums;

/**
 *
 * @author Dennis
 */
public class FormatFactory {

    private final int version;
    private Memory memoryInstance = null;
    private final ParserFactory parserFactory = new ParserFactory();

    public FormatFactory() {
        this(1);
    }

    public FormatFactory(int version) {
        this.version = version;

        bedragParser = new StringParser.Bedrag();

        personen = createPersoon();
        afschriften = createAfschrift();
        bonnetjes = createBonnetjes();
        kookdagen = createKookdagen();
        memoryFormat = createMemory();
        bewoonPeriodes = createBewoonPeriodes();
        init = createInit();
        kookSchuldDelers = createKookSchuldDelers();
        bierBonnetjes = createBierBonnetjes();
        betaaldVias = createBetaaldVias();
        contantRecords = createContantRecords();
        afrekenings = createAfrekenings();
    }

    public final Format<Set<Object>> personen;
    public final Format<Set<Afschrift>> afschriften;
    public final Format<Set<Bonnetje>> bonnetjes;
    public final Format<List<Kookdag>> kookdagen;
    public final Format<Memory> memoryFormat;
    public final Format<Set<BewoonPeriode>> bewoonPeriodes;
    public final Format<RekeningLeenBudget> init;
    public final Format<Map<Persoon, Persoon>> kookSchuldDelers;
    public final Format<Set<BierBonnetje>> bierBonnetjes;
    public final Format<Set<BetaaldVia>> betaaldVias;
    public final Format<DoubleContantRecordSet> contantRecords;
    public final Format<Set<Afrekening>> afrekenings;

    private final StringParser<Integer> bedragParser;

    private Format<Set<Object>> createPersoon() {
        String[] header;
        MyFilenameFilter filenameFilter;
        SetParser<Object> parser;

        if (version >= 2) {
            header = new String[]{"Naam", "Kwijtschelden", "Betaald"};
        } else {
            header = new String[]{"Naam", "Kwijtschelden"};
        }

        filenameFilter = new MyFilenameFilter("personen");

        if (version >= 2) {
            parser = parserFactory.new SetParser<Object>() {

                @Override
                protected int getParseLevel() {
                    return 10;
                }

                @Override
                protected Object parseLine(String[] strings) {
                    String naam;
                    boolean kwijtschelden;
                    Integer betaald;

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
                        betaald = bedragParser.parse(strings[index]);
                        if (betaald == null) {
                            throw new MyParseException(index);
                        }
                    } else {
                        betaald = 0;
                    }
                    Persoon p = memoryInstance.personen.find(naam);
                    if (p.getKwijtschelden() == null) {
                        p.setKwijtschelden(kwijtschelden);
                    } else if (p.kwijtschelden() != kwijtschelden) {
                        throw new MyParseException(0);
                    }
                    if (betaald > 0) {
                        System.out.println("TODO: " + p + " zijn betaling  van " + betaald + " verwerken ergens, als het bv contant was");
                    }
                    return null;
                }
            };
        } else {
            parser = parserFactory.new SetParser<Object>() {

                @Override
                protected Object parseLine(String[] strings) {
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
            final StringParser<Datum> DATUM_PARSER
                    = new StringParser.NormalDatum("dd-MM-yy");

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
                Integer bedrag;
                String mutatieSoort;
                String mededeling;

                int index = 0;

                if ((datum = DATUM_PARSER.parse(strings[index])) == null) {
                    throw new MyParseException(index);
                }

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

                if ((bedrag = bedragParser.parse(strings[++index])) == null) {
                    throw new MyParseException(index);
                }

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

            final StringParser<Datum> dateParser = new StringParser.FastDatum();

            private int id;
            private Integer bedrag;
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

                try {
                    id = Integer.parseInt(strings[index]);
                } catch (NumberFormatException e) {
                    throw new MyParseException(index, e);
                }

                if ((bedrag = bedragParser.parse(strings[++index])) == null) {
                    throw new MyParseException(index);
                }

                String persoonNaam = strings[++index];

                pasEindigd = strings[++index];

                if ((date = dateParser.parse(strings[++index])) == null) {
                    throw new MyParseException(index);
                }

                String winkelNaam = strings[++index];

                if (winkelNaam.isEmpty()) {
                    if (winkel == null) {
                        throw new MyParseException(index);
                    }
                } else {
                    winkel = memoryInstance.winkels.get(winkelNaam);
                    if (winkel == null) {
                        winkel = new Winkel(winkelNaam);
                        memoryInstance.winkels.put(winkel);
                    }
                }

                items = new ArrayList<>(strings.length - index);
                while (++index < strings.length) {
                    AankoopCat ac = memoryInstance.aankoopCats.find(strings[index]);
                    items.add(ac);
                }

                //verfijnen
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
                Integer bedrag;
                Persoon kok;
                Map<Persoon, Integer> meeters = new HashMap<>(strings.length - 3);

                try {
                    ID = Integer.parseInt(strings[index]);
                } catch (NumberFormatException e) {
                    throw new MyParseException(index, e);
                }

                index++;

                if ((bedrag = bedragParser.parse(strings[index])) == null) {
                    throw new MyParseException(index);
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
        final String[] header;
        MyFilenameFilter filenameFilter;
        Parser<Memory> parser;

        header = new String[]{"Type", "various", "(winkel categorie)", "(winkel andere namen)"};
        filenameFilter = new MyFilenameFilter("memory");

        int size = 20 / header.length;

        this.memoryInstance = new Memory(size);

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
                        Persoon p = instance.personen.find(subject);
                        while (index < strings.length) {
                            instance.personen.put(p, strings[index++]);
                        }
                        break;
                    case typeRekening:
                        p = instance.personen.find(subject);
                        while (++index < strings.length) {
                            String s = strings[index];
                            double digits = 0;
                            for (char chaR : s.toCharArray()) {
                                if (Character.isDigit(chaR)) {
                                    digits++;
                                }
                            }
                            digits /= s.length();
                            if (digits >= .95) {
                                instance.personen.putRek(p, s);
                            } else {
                                instance.personen.put(p, s);
                            }
                        }
                        break;
                    case typeIncasso:
                        String rek = strings[++index];
                        Incasso i = new Incasso(subject, rek);
                        instance.incassos.put(i);
                        instance.incassos.putRek(i, rek);
                        break;
                    case typeWinkel:
                        String categorieNaam = strings[++index];
                        AankoopCat defaultCat = instance.aankoopCats.find(categorieNaam);

                        String[] medeDelingen
                                = new String[strings.length - 1 - index];

                        System.arraycopy(strings, index, medeDelingen, 0, medeDelingen.length);

                        Winkel w = instance.winkels.get(subject);
                        if (w == null) {
                            w = new Winkel(subject, defaultCat);
                            instance.winkels.put(w, subject);
                        } else {
                            w.foundDefaultCat(defaultCat);
                        }
                        for (String mededeling : medeDelingen) {
                            instance.winkels.putMede(w, mededeling);
                        }
                        break;
                    case typeAankoopCat:
                        AankoopCat cat = instance.aankoopCats.find(subject);

                        while (++index < strings.length) {
                            instance.aankoopCats.put(cat, strings[index]);
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

        header = new String[]{"Naam", "Periode (van)", "Periode (tot)", "bedrag", "..."};
        filenameFilter = new MyFilenameFilter("bewoners");
        if (version >= 5) {
            parser = parserFactory.new SetParser<BewoonPeriode>() {
                private final StringsParser<IntervalDatums> perParser
                        = new StringsParser.IntervalParser(
                                new StringParser.NormalDatum());
                //private final StringParser<Integer> bedragParser = new StringParser.Bedrag();
                private Integer bedrag = null;

                @Override
                protected BewoonPeriode parseLine(String[] strings) {
                    if (strings.length < 2) {
                        throw new MyParseException.Length();
                    }
                    int index = 0;

                    //find subject for this line
                    Persoon subject = memoryInstance.personen.find(strings[index]);
                    BewoonPeriode last = null;

                    while (++index < strings.length) {
                        IntervalDatums interval;

                        String start = strings[index];

                        if (++index < strings.length) {
                            String end = strings[index];
                            interval = perParser.parseSepa(start, end);
                        } else {
                            interval = perParser.parseSepa(start);
                        }
                        if (interval == null) {
                            throw new MyParseException(index, "Interval onleesbaar");
                        }
                        if (interval.isNegative()) {
                            throw new MyParseException(index, "Interval is negatief");
                        }

                        if (++index < strings.length) {
                            //nieuw bedrag
                            this.bedrag = bedragParser.parse(strings[index]);
                            if (bedrag == null) {
                                throw new MyParseException(index, "Bedrag onleesbaar");
                            }
                        } else if (bedrag == null) {
                            throw new MyParseException(index, "Geen bedrag opgegeven");
                        }

                        if (last != null) {
                            instance.add(last);
                        }

                        last = new BewoonPeriode(subject, interval, bedrag);
                    }
                    return last;
                }
            };
        } else {
            throw new UnsupportedOperationException("Wrong version (must be >= 5): " + version);
        }

        return new Format<>(filenameFilter, header, parser);
    }

    private Format<RekeningLeenBudget> createInit() {
        String[] header;
        MyFilenameFilter filenameFilter;
        Parser<RekeningLeenBudget> parser;

        header = new String[]{"initialization file"};
        filenameFilter = new MyFilenameFilter("init");
        parser = parserFactory.new SimpleParserRef<RekeningLeenBudget>() {

            @Override
            protected void parseLine(String[] strings) {
                if (strings.length != 2) {
                    throw new MyParseException.Length();
                }

                String subject = strings[0];
                Integer bedrag = bedragParser.parse(strings[1]);
                if (bedrag == null) {
                    throw new MyParseException(1, "Geen bedrag gevonden");
                }
                if (subject.startsWith(".")) {
                    subject = subject.substring(1);
                    if (subject.equals("budget")) {
                        instance.initBudget(getRef(), bedrag);
                        return;
                    } else if (subject.equals("rekening")) {
                        instance.initBank(getRef(), bedrag);
                        return;
                    }
                } else {
                    Persoon p = memoryInstance.personen.get(subject);
                    if (p != null) {
                        instance.initSchuld(p, getRef(), bedrag);
                        return;
                    }
                }
                throw new MyParseException(0, "?");
            }

            @Override
            protected RekeningLeenBudget init(int size) {
                return new RekeningLeenBudget("Raafrekening");
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
            final StringParser<Datum> DATEPARSER = new StringParser.FastDatum();

            @Override
            protected BierBonnetje parseLine(String[] strings) {
                String merk;
                int kratten;
                Integer totaalPrijs;
                Datum datum;
                Winkel winkel;

                if (strings.length < 4 || strings.length > 5) {
                    throw new MyParseException.Length();
                }
                int index = 0;
                merk = strings[index];

                try {
                    kratten = Integer.parseInt(strings[++index]);
                } catch (NumberFormatException e) {
                    throw new MyParseException(index, e);
                }

                if ((totaalPrijs = bedragParser.parse(strings[++index])) == null) {
                    throw new MyParseException(index);
                }

                if ((datum = DATEPARSER.parse(strings[++index])) == null) {
                    throw new MyParseException(index);
                }

                winkel = memoryInstance.winkels.get(strings[++index]);
                if (winkel == null) {
                    winkel = new Winkel(
                            strings[index],
                            memoryInstance.aankoopCats.find("Bier"));
                }

                return new BierBonnetje(merk, kratten, totaalPrijs, datum, winkel);
            }
        };
        return new Format<>(filenameFilter, header, parser);
    }

    private Format<Set<BetaaldVia>> createBetaaldVias() {
        String[] header;
        MyFilenameFilter filenameFilter;
        SetParser<BetaaldVia> parser;

        header = new String[]{"datum", "onderwerp", "betaald", "via", "rede", "(Is een contante transactie via iemand naar de Raaf)"};
        filenameFilter = new MyFilenameFilter("af-betaald-via");
        parser = parserFactory.new SetParser<BetaaldVia>() {
            final StringParser<Datum> DATEPARSER = new StringParser.NormalDatum("dd-MM-yyyy");

            final private String SAME_STRING = "idem";

            private RekeningLeen onderwerp = null;
            private Persoon via = null;
            private String rede = null;

            @Override
            protected BetaaldVia parseLine(String[] strings) {
                if (strings.length < 5) {
                    throw new MyParseException.Length();
                }
                Datum datum;
                Integer bedrag;

                int index = 0;

                datum = DATEPARSER.parse(strings[index]);
                if (datum == null) {
                    throw new MyParseException(index);
                }

                String onderwerpString = strings[++index];
                if (onderwerpString.isEmpty() || onderwerpString.equals(SAME_STRING)) {
                    if (onderwerp == null) {
                        throw new MyParseException(index, "Vorrige onderwerp niet gevonden");
                    }
                } else {
                    onderwerp = memoryInstance.personen.get(onderwerpString);
                    if (onderwerp == null) {
                        onderwerp = memoryInstance.incassos.get(onderwerpString);
                        if (onderwerp == null) {
                            throw new MyParseException(index, "Onderwerp niet bekend: " + onderwerpString);
                        }
                    }
                }

                bedrag = bedragParser.parse(strings[++index]);
                if (bedrag == null) {
                    throw new MyParseException(index, "Bedrag niet gevonden");
                }

                String viaString = strings[++index];
                if (viaString.isEmpty() || viaString.equals(SAME_STRING)) {
                    if (via == null) {
                        throw new MyParseException(index, "Vorrige via niet gevonden");
                    }
                } else {
                    via = memoryInstance.personen.find(viaString);
                }

                String redeString = strings[++index];
                if (redeString.isEmpty()) {
                    throw new MyParseException(index, "rede is empty");
                }
                if (redeString.equals(SAME_STRING)) {
                    if (rede == null) {
                        throw new MyParseException(index, "Laatste rede niet gevonden");
                    }
                } else {
                    rede = redeString;
                }

                return new BetaaldVia(datum, onderwerp, bedrag, via, rede);
            }
        };

        return new Format<>(filenameFilter, header, parser);
    }

    private Format<DoubleContantRecordSet> createContantRecords() {
        String[] header;
        MyFilenameFilter filenameFilter;
        ParserFactory.SimpleParser<DoubleContantRecordSet> parser;

        header = new String[]{"datum", "bedrag (-isPin, +Storting)", "eigenaar (of GEVONDEN)", "opmerking"};
        filenameFilter = new MyFilenameFilter("contant");
        parser = parserFactory.new SimpleParser<DoubleContantRecordSet>() {
            final StringParser<Datum> DATEPARSER = new StringParser.NormalDatum("dd-MM-yyyy");
            final private String NONE_RECORD = "GEVONDEN";

            @Override
            protected void parseLine(String[] strings) {
                if (strings.length != 4) {
                    throw new MyParseException.Length();
                }
                Datum datum;
                Integer bedrag;
                Persoon eigenaar;
                String opmerking;

                int index = 0;

                datum = DATEPARSER.parse(strings[index]);
                if (datum == null) {
                    throw new MyParseException(index, "Datum niet gevonden");
                }

                bedrag = bedragParser.parse(strings[++index]);
                if (bedrag == null) {
                    throw new MyParseException(index, "Bedrag niet gevonden");
                }
                if (bedrag == 0) {
                    throw new MyParseException(index, "Rare storing/pin met bedrag: " + bedrag);
                }

                String eigenaarString = strings[++index];
                if (eigenaarString.equals(NONE_RECORD)) {
                    eigenaar = null;
                } else {
                    eigenaar = memoryInstance.personen.find(eigenaarString);
                }

                opmerking = strings[++index];
                if (opmerking == null || opmerking.isEmpty()) {
                    throw new MyParseException(index, "Geen opmerking");
                }

                instance.add(new ContantRecord(datum, bedrag, eigenaar, opmerking));
            }

            @Override
            protected DoubleContantRecordSet init(int size) {
                return new DoubleContantRecordSet(size);
            }
        };

        return new Format<>(filenameFilter, header, parser);
    }

    private Format<Set<Afrekening>> createAfrekenings() {
        String[] header;
        MyFilenameFilter filenameFilter;
        ParserFactory.SetParser<Afrekening> parser;

        header = new String[]{"persoon", "datum"};
        filenameFilter = new MyFilenameFilter("afrekening");
        parser = parserFactory.new SetParser<Afrekening>() {
            final StringParser<Datum> DATEPARSER = new StringParser.NormalDatum("dd-MM-yyyy");
            final String pick_eind = "eind";

            @Override
            protected Afrekening parseLine(String[] strings) {
                if (strings.length != 2) {
                    throw new MyParseException.Length();
                }

                int index = 0;

                Persoon a = memoryInstance.personen.get(strings[index]);
                if (a == null) {
                    throw new MyParseException(index, "Persoon niet gevonden");
                }

                Datum d;
                String dateString = strings[++index];
                if (dateString.equals(pick_eind)) {
                    d = null;
                } else {
                    d = DATEPARSER.parse(dateString);
                    if (d == null) {
                        throw new MyParseException(index, "Datum niet gevonden");
                    }
                }

                return new Afrekening(a, d);
            }
        };

        return new Format<>(filenameFilter, header, parser);
    }
}
