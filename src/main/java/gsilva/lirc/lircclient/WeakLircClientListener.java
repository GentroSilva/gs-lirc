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
