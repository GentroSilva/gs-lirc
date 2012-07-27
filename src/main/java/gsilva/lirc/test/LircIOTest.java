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
