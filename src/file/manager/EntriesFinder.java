/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package file.manager;

import java.nio.file.Paths;

/**
 *
 * @author Dennis
 */
public class EntriesFinder {
    static String simpleBasePath(){
        
        String currPath = Paths.get("").toAbsolutePath().toString();
        
        if(!currPath.isEmpty()){
            return currPath + "\\files\\RaafEntries";
        }
        
        String usrDomain = System.getenv("USERDOMAIN");
        if (usrDomain != null && usrDomain.equalsIgnoreCase("LaptopDennis")) {
            return "C:\\Users\\Dennis\\Dropbox\\RaafEntries";
        } else {
            return "E:\\Dropbox\\RaafEntries\\";
        }
    }
}
