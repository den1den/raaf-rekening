/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

/**
 *
 * @author Dennis
 */
public abstract class Arrays {

    private Arrays() {
    }
    
    public static int[] combine(int[]... ints){
        int n = 0;
        for (int[] in : ints) {
            n += in.length;
        }
        int[] result = new int[n];
        int index = 0;
        for (int[] in : ints) {
            System.arraycopy(in, 0, result, index, in.length);
            index += in.length;
        }
        return result;
    }
}
