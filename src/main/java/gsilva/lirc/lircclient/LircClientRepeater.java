/**
 * Copyright 2012 Blake Dickie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gsilva.lirc.lircclient;

import java.beans.Beans;
import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import javax.swing.event.EventListenerList;

/**
 *
 * @author bdickie
 */
public class LircClientRepeater implements LircClientListener {
    private static LircClient globalClient;

    public synchronized static LircClient getGlobalClient() {
        return globalClient;
    }

    public synchronized static void setGlobalClient(LircClient globalClient) {
        

        synchronized(repeaters) {
            ListIterator<WeakReference<LircClientRepeater>> i = repeaters.listIterator();
            while(i.hasNext()) {
                LircClientRepeater c = i.next().get();
                if (c == null)
                    i.remove();
                else {
                    c.removeListener();
                }
            }

            LircClientRepeater.globalClient = globalClient;

            i = repeaters.listIterator();
            while(i.hasNext()) {
                LircClientRepeater c = i.next().get();
                if (c == null)
                    i.remove();
                else {
                    c.setupListener();
                }
            }
        }
    }

    private static final List<WeakReference<LircClientRepeater>> repeaters = new LinkedList<WeakReference<LircClientRepeater>>();

    private WeakLircClientListener weakListener;

    public LircClientRepeater() {
        weakListener = new WeakLircClientListener(this);

        synchronized(repeaters) {
            repeaters.add(new WeakReference<LircClientRepeater>(this));

            setupListener();
        }
    }

    private void setupListener() {
        if (globalClient != null && !Beans.isDesignTime())
            globalClient.addLircClientListener(weakListener);
    }

    private void removeListener() {
        if (globalClient != null && !Beans.isDesignTime())
            globalClient.removeLircClientListener(weakListener);
    }

    public void lircCommandRecieved(LircClientEvent e) {
        if (eventListeners == null)
            return;
        
        for(LircClientListener l: eventListeners.getListeners(LircClientListener.class)) {
            l.lircCommandRecieved(e);
        }
    }


    private EventListenerList eventListeners;

    public void addLircClientListener(LircClientListener l) {
        if (eventListeners == null)
            eventListeners = new EventListenerList();
        eventListeners.add(LircClientListener.class, l);
    }

    public void removeLircClientListener(LircClientListener l) {
        if (eventListeners == null)
            return;
        eventListeners.remove(LircClientListener.class, l);
    }


    
}
