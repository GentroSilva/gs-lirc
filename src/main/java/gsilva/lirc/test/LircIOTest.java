/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gsilva.lirc.test;

import gsilva.lirc.core.LircEvent;
import gsilva.lirc.core.LircIOListener;
import gsilva.lirc.lircclient.LircClient;

/**
 *
 * @author bdickie
 */
public class LircIOTest implements LircIOListener {

    public void lircEventReceived(LircEvent e) {
        System.out.println(e);

        if (e.getButton().equalsIgnoreCase("Stop"))
            System.exit(0);
    }

    public static void main(String[] args) {
        LircClient client = new LircClient("mplayer");
        client.start();

    }

}
