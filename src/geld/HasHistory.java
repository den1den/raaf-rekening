/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geld;

import geld.rekeningen.Event;
import java.util.List;

/**
 *
 * @author Dennis
 */
interface HasHistory {

    List<Event> getHistory();
    
}
