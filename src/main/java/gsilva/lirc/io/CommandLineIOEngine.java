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

package gsilva.lirc.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bdickie
 */
public class CommandLineIOEngine implements IOEngine {

    protected Logger log = LoggerFactory.getLogger(getClass());

    private String command;
    private String device;

    private Process process;

    private InputStream stdout;
    private InputStream stderr;
    private OutputStream stdin;

    public CommandLineIOEngine(String command, String device) {
        this.command = command;
        this.device = device;
    }

    public CommandLineIOEngine(String command) {
        this(command, DEFAULT_DEVICE);
    }

    public boolean startIOEngine() {
        try {
            List<String> cmd = new ArrayList<String>();
            cmd.add(command);
            if (device != null)
                cmd.add(device);

            ProcessBuilder pb = new ProcessBuilder(cmd);
            process = pb.start();

            stdout = process.getInputStream();
            stderr = process.getErrorStream();
            stdin = process.getOutputStream();

            new ErrorThread().start();

            return true;
        } catch (Exception e) {
            log.error("Error starting command: " + command, e);
            return false;
        }
    }

    public void stopIOEngine() {
        try {
            process.destroy();

            stdout = null;
            stdin = null;

        } catch (Exception e) {
            
        }
    }

    public InputStream getInputStream() {
        return stdout;
    }

    public OutputStream getOutputStream() {
        return stdin;
    }


    private class ErrorThread extends Thread {

        public ErrorThread() {
            super("LIRC Cmd Error Monitor");
            setDaemon(true);
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(stderr));
                String line;
                while((line = in.readLine()) != null) {
                    log.error(String.format("Error In Command (%s): %s", command, line));
                }
            } catch (Exception ignore) {
                
            } finally {
                IOUtils.closeQuietly(stderr);
                stderr = null;
            }
        }


    }
}
