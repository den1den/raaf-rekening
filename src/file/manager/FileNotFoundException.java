/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package file.manager;

/**
 *
 * @author Dennis
 */
class FileNotFoundException extends RuntimeException {

    public FileNotFoundException(String file, String dir) {
        super(file + " not found in " + dir);
    }

}
