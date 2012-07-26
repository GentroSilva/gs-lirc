/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gsilva.lirc.lircclient;

import java.util.EventListener;

/**
 *
 * @author bdickie
 */
public interface LircClientListener extends EventListener {
    public void lircCommandRecieved(LircClientEvent e);
}
