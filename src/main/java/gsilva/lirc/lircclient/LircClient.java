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

import gsilva.lirc.core.LircEvent;
import gsilva.lirc.core.LircIO;
import gsilva.lirc.core.LircIOListener;
import gsilva.lirc.io.IOEngine;
import gsilva.lirc.lircclient.config.LircConfigEntry;
import gsilva.lirc.lircclient.config.LircConfigFlag;
import gsilva.lirc.lircclient.config.LircConfigReader;
import java.io.File;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bdickie
 */
public class LircClient {
    private Logger log = LoggerFactory.getLogger(getClass());

    private IOEngine ioEngine;
    private String programName;
    private File lircrcFile;
    private boolean useAWTThread = true;
    private LircConfigValidator validator;

    private LircIO io;

    private Map<LircConfigEntry,LircButtonState> configs;

    public LircClient(IOEngine ioEngine, String programName, File lircrcFile) {
        this.ioEngine = ioEngine;
        this.programName = programName;
        this.lircrcFile = lircrcFile;

        if (programName == null)
            throw new IllegalArgumentException("Program name must not be null.");
    }

    public LircClient(String programName, File lircrcFile) {
        this(null, programName, lircrcFile);
    }

    public LircClient(String programName) {
        this(programName, null);
    }

    public boolean isUseAWTThread() {
        return useAWTThread;
    }

    public void setUseAWTThread(boolean useAWTThread) {
        this.useAWTThread = useAWTThread;
    }

    public LircConfigValidator getValidator() {
        return validator;
    }

    public void setValidator(LircConfigValidator validator) {
        this.validator = validator;
    }

    

    private void readConfig() {
        File srcFile = lircrcFile;
        if (srcFile == null) {
            srcFile = new File(System.getProperty("user.home"), ".lircrc");
            if (!srcFile.isFile())
                srcFile = new File("/etc/lircrc");
        }

        if (!srcFile.isFile() || !srcFile.canRead()) {
            log.error("Unable to read lircrc file: " + srcFile);
            configs = Collections.EMPTY_MAP;
            return;
        }

        LircConfigReader reader = new LircConfigReader();
        reader.setProgram(programName);
        reader.setValidator(getValidator());
        reader.readFile(srcFile);

        configs = new LinkedHashMap<LircConfigEntry, LircButtonState>();
        for(LircConfigEntry entry: reader.getConfigEntries()) {
            configs.put(entry, new LircButtonState());
        }
        
    }

    public String getProgramName() {
        return programName;
    }

    

    public void start() {
        readConfig();

        io = new LircIO();
        if (ioEngine != null)
            io.setIOEngine(ioEngine);
        io.setDemeonReader(false);
        io.addLircIOListener(new LircEventWatcher());
        io.start();
    }

    public void stop() {
        io.stop();
        io = null;
    }

    private void fireEvent(LircEvent underLyingEvent, String config) {
        if (eventListeners == null)
            return;

        LircFireEvent runnable = new LircFireEvent(underLyingEvent, config);
        if (SwingUtilities.isEventDispatchThread())
            runnable.run();
        else
            SwingUtilities.invokeLater(runnable);
    }

    private void lircEvent(LircEvent e) {
        boolean handled = false;
        for(Map.Entry<LircConfigEntry,LircButtonState> entry: configs.entrySet()) {
            LircConfigEntry config = entry.getKey();
            LircButtonState state = entry.getValue();
            boolean shouldHandle = !handled && config.handles(e);
            if (shouldHandle) {

                int repeat = e.getRepeat();

                boolean repeatSuppressed = repeat > 0;

                if (config.getRepeat() > 0 && repeatSuppressed) {
                    if (((repeat - config.getDelay()) % config.getRepeat()) == 0)
                        repeatSuppressed = false;
                }

                

                if (config.getFlags().contains(LircConfigFlag.Quit))
                    handled = true;

                if (repeatSuppressed)
                    continue;

                List<String> configStrs = config.getConfigs();
                if (configStrs.isEmpty())
                    continue;

                fireEvent(e, configStrs.get(state.getToggleState()));
                state.setToggleState((state.getToggleState() + 1) % configStrs.size());

            } else {
                if (config.getFlags().contains(LircConfigFlag.ToggleReset))
                    state.setToggleState(0);
            }
            
        }

    }


    private class LircEventWatcher implements LircIOListener {

        public void lircEventReceived(LircEvent e) {
            lircEvent(e);
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

    private class LircFireEvent implements Runnable {
        private LircEvent underLyingEvent;
        private String config;

        public LircFireEvent(LircEvent underLyingEvent, String config) {
            this.underLyingEvent = underLyingEvent;
            this.config = config;
        }

        public void run() {
            if (eventListeners == null)
                return;

            LircClientEvent e = null;
            for(LircClientListener l: eventListeners.getListeners(LircClientListener.class)) {
                if (e == null)
                    e = new LircClientEvent(LircClient.this, config, underLyingEvent);
                l.lircCommandRecieved(e);
            }
        }


    }

}
