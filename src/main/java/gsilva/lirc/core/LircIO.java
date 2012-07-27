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

package gsilva.lirc.core;

import gsilva.lirc.io.IOEngine;
import gsilva.lirc.io.IRWIOEngine;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.event.EventListenerList;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bdickie
 */
public class LircIO {

    private Logger log = LoggerFactory.getLogger(getClass());

    private InputThread inputThread;

    private ShutdownHook shutdownHook;

    public LircIO() {
    }

    private IOEngine ioEngine;
    private boolean demeonReader = true;

    public synchronized IOEngine getIOEngine() {
        if (ioEngine == null)
            ioEngine = new IRWIOEngine();
        
        return ioEngine;
    }

    public synchronized void setIOEngine(IOEngine ioEngine) {
        checkRunning();
        
        this.ioEngine = ioEngine;
    }

    public boolean isDemeonReader() {
        return demeonReader;
    }

    public void setDemeonReader(boolean demeonReader) {
        checkRunning();
        
        this.demeonReader = demeonReader;
    }



    private void checkRunning() {
        if (inputThread != null)
            throw new IllegalStateException("LircIO is already running.");
    }

    public boolean start() {

        IOEngine e = getIOEngine();

        if (!e.startIOEngine())
            return false;

        InputStream is = e.getInputStream();
        inputThread = new InputThread(is);
        inputThread.start();

        shutdownHook = new ShutdownHook();
        Runtime.getRuntime().addShutdownHook(shutdownHook);


        return true;
    }

    public void stop() {
        stop(false);
    }

    private synchronized void stop(boolean shutdownHookLaunched) {

        if (inputThread != null) {
            inputThread.interrupt();
            inputThread = null;

            getIOEngine().stopIOEngine();
        }

        if (!shutdownHookLaunched && shutdownHook != null) {
            try {
                Runtime.getRuntime().removeShutdownHook(shutdownHook);
            } catch (IllegalStateException ignore) {}
        }
    }


    private EventListenerList listeners;

    public void addLircIOListener(LircIOListener l) {
        if (listeners == null)
            listeners = new EventListenerList();
        listeners.add(LircIOListener.class, l);
    }

    public void removeLircIOListener(LircIOListener l) {
        if (listeners == null)
            return;
        listeners.remove(LircIOListener.class, l);
    }

    private void fireLircIOListener(String code, int repeat, String button, String remote) {
        if (listeners == null)
            return;
        
        LircEvent e = null;

        for(LircIOListener l: listeners.getListeners(LircIOListener.class)) {
            if (e == null)
                e = new LircEvent(this, code, repeat, button, remote);
            l.lircEventReceived(e);
        }
    }
    

    private void handleLine(String line) {
        Matcher m = BUTTON_PATTERN.matcher(line);
        if (m.matches()) {
            fireLircIOListener(m.group(1), Integer.parseInt(m.group(2), 16), m.group(3), m.group(4));
        }
    }

    private Pattern BUTTON_PATTERN = Pattern.compile("\\s*(\\S+) ([0-9a-fA-F]+) (\\S+) (\\S+)\\s*");


    private class InputThread extends Thread {

        private InputStream is;

        public InputThread(InputStream is) {
            super("LIRC Input");
            setDaemon(isDemeonReader());

            this.is = is;
        }

        @Override
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while((line = reader.readLine()) != null) {
                    handleLine(line);
                }
            } catch (Exception e) {
                if (!interrupted()) {
                    log.error("Error reading from LIRC.", e);
                }
            } finally {
                IOUtils.closeQuietly(is);
            }
        }
    }

    private class ShutdownHook extends Thread {

        @Override
        public void run() {
            LircIO.this.stop(true);
        }
        
    }
}
