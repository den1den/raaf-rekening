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
public class IntegerParsable{
    private Integer integer;

    public IntegerParsable() {
        this(null);
    }

    public IntegerParsable(Integer integer) {
        this.integer = integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    public Integer getInteger() {
        return integer;
    }public int getInt(){
        if(integer == null){
            throw new IllegalStateException();
        }
        return integer;
    }
}
