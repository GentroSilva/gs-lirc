/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gsilva.lirc.core;

import java.util.EventListener;

/**
 *
 * @author bdickie
 */
public interface LircIOListener extends EventListener {
    public void lircEventReceived(LircEvent e);
}
