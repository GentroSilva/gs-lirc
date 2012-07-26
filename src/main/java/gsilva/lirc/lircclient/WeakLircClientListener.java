/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gsilva.lirc.lircclient;

import java.lang.ref.WeakReference;

/**
 *
 * @author bdickie
 */
public class WeakLircClientListener implements LircClientListener {

    private WeakReference<LircClientListener> underlyingListener;

    public WeakLircClientListener(LircClientListener l) {
        underlyingListener = new WeakReference<LircClientListener>(l);
    }



    public void lircCommandRecieved(LircClientEvent e) {
        LircClientListener l = underlyingListener.get();
        if (l == null) {
            LircClient c = LircClientRepeater.getGlobalClient();
            if (c != null)
                c.removeLircClientListener(this);
        } else
            l.lircCommandRecieved(e);
    }

}
