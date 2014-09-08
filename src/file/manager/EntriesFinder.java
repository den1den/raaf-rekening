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
        String usrDomain = System.getenv("USERDOMAIN");
        String curr = Paths.get("").toAbsolutePath().toString();
        
        if(!curr.isEmpty()){
            return curr + "\\files\\RaafEntries";
        }
        if (usrDomain != null && usrDomain.equalsIgnoreCase("LaptopDennis")) {
            return "C:\\Users\\Dennis\\Dropbox\\RaafEntries";
        } else {
            return "E:\\Dropbox\\RaafEntries\\";
        }
    }
}
