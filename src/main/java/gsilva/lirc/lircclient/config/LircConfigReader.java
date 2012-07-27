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

package gsilva.lirc.lircclient.config;

import gsilva.lirc.lircclient.*;
import gsilva.lirc.util.MutableObject;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author bdickie
 */
public class LircConfigReader {
    private Logger log  = LoggerFactory.getLogger(getClass());

    private LircConfigValidator validator;
    private String program;

    private List<LircConfigEntry> configEntries;;

    public LircConfigReader() {
        configEntries = new ArrayList<LircConfigEntry>();
    }

    public List<LircConfigEntry> getConfigEntries() {
        return configEntries;
    }

    public LircConfigValidator getValidator() {
        return validator;
    }

    public void setValidator(LircConfigValidator validator) {
        this.validator = validator;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    

    private int currentLine;


    public void readFile(File file) {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(file));

            String line;
            currentLine = 0;
            while((line = reader.readLine()) != null) {
                line = line.trim();
                currentLine++;
                handleLine(line);
            }
        } catch (Exception e) {
            log.error("Error reading lircrc file: " + file, e);
        } finally {
            IOUtils.closeQuietly(reader);
        }
    }

    private Pattern BEGIN_PATTERN = Pattern.compile("begin(?:\\s+(\\S+))?", Pattern.CASE_INSENSITIVE);
    private Pattern EQUALS_PATTERN = Pattern.compile("(\\S+)\\s*=\\s*(.*)", Pattern.CASE_INSENSITIVE);

    private LircConfigEntry entry;
    private String currentMode;
    private String lastRemote = "*";


    private void handleLine(String line) {
        // Check for comments
        if (line.startsWith("#") || line.length() == 0)
            return;
        
        Matcher m = BEGIN_PATTERN.matcher(line);
        if (m.matches()) {
            String mode = m.group(1);
            if (mode == null) {
                if (entry != null)
                    syntaxError("\"begin\" found before previous entry's end");
                entry = new LircConfigEntry();
                lastRemote = "*";
            } else {
                if (currentMode != null)
                    syntaxError("\"begin\" mode found before previous mode's end");
                currentMode = mode;
            }
        } else if (line.equalsIgnoreCase("end")) {
            if (entry != null) {
                endEntry();
                entry = null;
            } else if (currentMode != null)
                currentMode = null;
            else
                syntaxError("\"end\" found without begin.");
        } else {
            m = EQUALS_PATTERN.matcher(line);
            if (!m.matches()) {
                syntaxError("Unable to parse line: " + line);
                return;
            }



            String option = m.group(1).toLowerCase();
            String value = m.group(2);

            if (entry != null) {
                handleEntryOption(option, value);
            } else if (currentMode != null) {
                
            } else {
                syntaxError("Not in a block.");
            }
        }
    }


    private void handleEntryOption(String option, String value) {
        if (option.equals("prog"))
            entry.setProgram(value);
        else if (option.equals("remote"))
            lastRemote = value;
        else if (option.equals("button")) {
            entry.addButton(new LircConfigButton(lastRemote, value));
        } else if (option.equals("repeat"))
            entry.setRepeat(parseInt(value));
        else if (option.equals("delay"))
            entry.setDelay(parseInt(value));
        else if (option.equals("config"))
            entry.addConfig(lircUnescapeConfig(value));
        else if (option.equals("mode")) {

        } else if (option.equals("flags")) {
            for(String s1: value.split("\\s+"))
                for(String flag: value.split(",")) {
                    LircConfigFlag lFlag = LircConfigFlag.lookup(flag);
                    if (lFlag != null)
                        entry.addFlag(lFlag);
                }
        } else {
            log.warn("Unknown option: " + option);
        }
            
    }

    private int parseInt(String value) {
        if (!value.matches("\\d+"))
            syntaxError(String.format("Expected number (%s)", value));
        return Integer.parseInt(value);
    }

    private void endEntry() {
        if (program == null || entry.getProgram() == null || !program.equalsIgnoreCase(entry.getProgram()))
            return;

        if (validator != null) {
            ListIterator<String> configs = entry.getConfigs().listIterator();
            while(configs.hasNext()) {
                String config = configs.next();

                MutableObject<String> mConfig = new MutableObject<String>(config);
                String error = validator.validateLircConfig(mConfig);
                if (error != null)
                    syntaxError(error);
                configs.set(mConfig.getValue());
            }
        }

        configEntries.add(entry);
    }

    private void syntaxError(String msg) {
        throw new IllegalArgumentException(String.format("Error Parsing lircrc file: %s (Line %d)", msg, currentLine));
    }

    public static String lircUnescapeConfig(String str) {
        return StringEscapeUtils.unescapeJava(str);
    }
}
