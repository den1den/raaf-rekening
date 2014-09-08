/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package parsers;

/**
 *
 * @author Dennis
 */
public abstract class StringsParser<C> {
    public abstract C parse(String[] entry);
}
