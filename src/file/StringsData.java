/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import parsers.Source;

/**
 *
 * @author Dennis
 */
public abstract class StringsData implements Source, Iterable<String[]> {

    private final String[] header;
    final protected List<String[]> content = new LinkedList<>();

    public StringsData(String[] header) {
        this.header = header;
    }

    abstract public void read() throws IOException;

    abstract public void write() throws IOException;

    public void combineContent(StringsData sd) {
        if (!checkHeader(sd.header)) {
            throw new UnsupportedOperationException("Wrong headers");
        }

        List<String[]> after, before;
        //pick first right
        if (content.size() == 0) {
            if (sd.content.size() != 0) {
                content.addAll(sd.content);
            }
        } else if (sd.content.size() == 0) {
        } else {
            if (hasEntry(sd.content.get(0)) > 0) {
                after = sd.content;
                before = new LinkedList<>(content);
            } else if (sd.hasEntry(content.get(0)) > 0) {
                after = content;
                before = new LinkedList<>(sd.content);
            } else {
                throw new UnsupportedOperationException("not yet");
            }

            int beforeI = -1;
            int afterI = 0;
            for (int i = 0; i < before.size() && beforeI == -1; i++) {
                String[] entry = before.get(i);
                if (Arrays.deepEquals(entry, after.get(afterI))) {
                    beforeI = i;
                }
            }

            LinkedList<String[]> newList = new LinkedList<>(before);

            while (afterI < after.size()) {
                if (beforeI < newList.size()) {
                    if (Arrays.deepEquals(newList.get(beforeI), after.get(afterI))) {
                        beforeI++;
                        after.remove(afterI);
                    } else {
                        throw new UnsupportedOperationException();
                    }
                } else {
                    newList.addAll(after);
                    break;
                }
            }

            content.clear();
            content.addAll(newList);
        }
    }

    public int hasEntry(String[] entry) {
        int result = 0;
        for (String[] currEntry : content) {
            boolean same = currEntry.length == entry.length;
            for (int i = 0; same && i < currEntry.length; i++) {
                if (!currEntry[i].equals(entry[i])) {
                    same = false;
                }
            }
            if (same) {
                result++;
            }
        }
        return result;
    }

    public String[] getHeader() {
        return header;
    }

    public boolean hasHeader() {
        return getHeader() != null;
    }
    
    abstract public String[] readFirstStrings()throws IOException;
    
    public boolean checkHeader(){
        if(hasHeader()){
            try {
                return checkHeader(readFirstStrings());
            } catch (IOException ex) {
                throw new Error(ex);
            }
        }
        return true;
    }

    public boolean checkHeader(String[] against) {
        String[] header = getHeader();
        if (header == null) {
            return against == null;
        }
        if (header.length != against.length) {
            return false;
        }
        for (int i = 0; i < header.length; i++) {
            if (!header[i].equals(against[i])) {
                return false;
            }
        }
        return true;
    }

    public int size() {
        return content.size();
    }

    public List<String[]> getContent() {
        return content;
    }

    @Override
    public Iterator<String[]> iterator() {
        return content.iterator();
    }
}
