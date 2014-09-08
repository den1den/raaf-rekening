/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package file.manager;

import tijd.Time;

/**
 *
 * @author Dennis
 */
public class PathEntry {
    final String path;
    final Time timestamp;

    public PathEntry(String path) {
        this(path, new Time());
    }

    public PathEntry(String path, Time timestamp) {
        this.path = path;
        this.timestamp = timestamp;
    }
    
}
