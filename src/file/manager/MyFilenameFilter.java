/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file.manager;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author Dennis
 */
public class MyFilenameFilter implements FilenameFilter {

    String name;
    String[] extensies;
    boolean genummerd;

    public MyFilenameFilter(String name) {
        this(name, true, ".csv", "-read.csv");
    }

    public MyFilenameFilter(String name, boolean genummerd, String... extensies) {
        this.name = name;
        if (extensies.length == 0) {
            throw new IllegalArgumentException();
        }
        this.extensies = extensies;
        this.genummerd = genummerd;
    }

    private int getSeqNumber(String name) {
        if (!name.startsWith(this.name)) {
            return -1;
        }
        String extension = null;
        for (String ext : extensies) {
            if (name.endsWith(ext)) {
                extension = ext;
            }
        }
        if (extension == null) {
            return -1;
        }
        String middle = name.substring(this.name.length(), name.length() - extension.length());
        if (middle.isEmpty()) {
            return 0;
        } else {
            try {
                return Integer.parseInt(middle);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
    }

    private String genFileName(int seq) {
        String result;
        if (seq == 0) {
            result = "";
        } else {
            result = String.valueOf(seq);
        }
        return name + result + extensies[0];
    }

    @Override
    public boolean accept(File dir, String name) {
        return getSeqNumber(name) >= 0;
    }

    String getNextFileName(String[] files) {
        int largest = -1;
        for (String file : files) {
            int seqI;
            if ((seqI = getSeqNumber(file)) > largest) {
                largest = seqI;
            }
        }
        if (largest == -1) {
            return genFileName(0);
        } else if (genummerd) {
            return genFileName(largest + 1);
        } else {
            return null;
        }
    }
public static MyFilenameFilter getMeervoudCombo(String naam, String naam2, String... ext){
            return new MyFilenameFilterCombo(new MyFilenameFilter(naam, false, ext), naam2, true, ext);
        }
    static class MyFilenameFilterCombo extends MyFilenameFilter {

        private MyFilenameFilter extra;

        public MyFilenameFilterCombo(MyFilenameFilter extra, String name, boolean genummerd, String... extensies) {
            super(name, genummerd, extensies);
            this.extra = extra;
        }
        
        

        @Override
        public boolean accept(File dir, String name) {
            return super.accept(dir, name) || extra.accept(dir, name);
        }

    }
}
