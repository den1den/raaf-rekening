/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import data.memory.Memory;
import file.StringsData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Dennis
 */
public class ParserFactory {

    private final Memory memory;
    private int currentParseLevel = 0;

    public ParserFactory() {
        this(new Memory(10));
    }

    public ParserFactory(Memory memory) {
        this.memory = memory;
    }

    public Memory getMemory() {
        return memory;
    }

    /**
     * To parse Strings arrays into objects.
     * @param <P> 
     * @param <To> the output format in Raafrekening
     */
    public abstract class Parser<To> {

        protected To instance;

        final public To parse(StringsData data) {
            parseOrderCheck();

            instance = init(data.size());
            parseData(data);
            return instance;
        }

        final public To parse(StringsData[] datas) {
            parseOrderCheck();

            int size = 0;
            for (StringsData stringsData : datas) {
                size += stringsData.size();
            }
            instance = init(size);
            for (StringsData stringsData : datas) {
                parseData(stringsData);
            }
            return instance;
        }

        abstract protected To init(int size);

        private void parseOrderCheck() {
            if (getParseLevel() < currentParseLevel) {
                throw new RuntimeException("Parsing level error, was:" + currentParseLevel + " and now trying to parse: " + getParseLevel() + " See getParseLevel()");
            } else if (getParseLevel() > currentParseLevel) {
                currentParseLevel = getParseLevel();
            }
        }

        protected int getParseLevel() {
            return Integer.MAX_VALUE;
        }

        abstract protected void parseData(StringsData data);
    }
    
    public abstract class SimpleParser<To> extends Parser<To>{
        @Override
        protected void parseData(StringsData data){
            int lineNumber = 0;
            for (String[] fileLine : data) {
                try {
                    parseLine(fileLine);
                    lineNumber++;
                } catch (MyParseException ex) {
                    ex.setSourceAndNumber(data, lineNumber);
                    throw new RuntimeException(ex);
                }
            }
        }
        abstract protected void parseLine(String[] strings);
    }

    public abstract class SingleParser<P> extends Parser<P> {

        public SingleParser(P instance) {
            this.instance = instance;
        }

        @Override
        final protected P init(int size) {
            return instance;
        }

        @Override
        final protected void parseData(StringsData data) {
            int lineNumber = 0;
            for (String[] fileLine : data) {
                try {
                    parseLine(fileLine);
                    lineNumber++;
                } catch (MyParseException ex) {
                    ex.setSourceAndNumber(data, lineNumber);
                    throw new RuntimeException(ex);
                }
            }
        }

        protected abstract void parseLine(String[] strings);

    }

    abstract class CollectionParser<P, C extends Collection<P>>
            extends Parser<C> {

        @Override
        final protected void parseData(StringsData data) {
            int lineNumber = 0;
            for (String[] fileLine : data) {
                try {
                    instance.add(parseLine(fileLine));
                    lineNumber++;
                } catch (MyParseException ex) {
                    ex.setSourceAndNumber(data, lineNumber);
                    throw new RuntimeException(ex);
                }
            }
        }

        abstract protected P parseLine(String[] strings);
    }

    public abstract class SetParser<P> extends CollectionParser<P, Set<P>> {

        @Override
        protected Set<P> init(int size) {
            return new HashSet<>(size);
        }

    }

    public abstract class ListParser<P> extends CollectionParser<P, List<P>> {

        @Override
        protected List<P> init(int size) {
            return new ArrayList<>(size);
        }
    }
    
    public abstract class MapParser<P> extends SimpleParser<Map<P, P>>{

        @Override
        protected Map<P, P> init(int size) {
            return new HashMap<>(size);
        }
        
    }

    public abstract class VoidParser<P> extends SimpleParser<Void> {

        @Override
        protected Void init(int size) {
            return null;
        }

        @Override
        abstract protected void parseLine(String[] strings);

    }
}
