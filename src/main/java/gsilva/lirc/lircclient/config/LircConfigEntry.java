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

import gsilva.lirc.core.LircEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author bdickie
 */
public class LircConfigEntry {
    private String program;

    private int repeat = 0;
    private int delay = 0;
    private List<LircConfigButton> buttons;
    private List<String> configs;
    private Set<LircConfigFlag> flags;

    public LircConfigEntry() {
        buttons = new ArrayList<LircConfigButton>();
        flags = new HashSet<LircConfigFlag>();
        configs = new ArrayList<String>();
    }


    public List<String> getConfigs() {
        return configs;
    }

    public void addConfig(String config) {
        configs.add(config);
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public Set<LircConfigFlag> getFlags() {
        return flags;
    }

    public void addFlag(LircConfigFlag flag) {
        flags.add(flag);
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }
    
    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public List<LircConfigButton> getButtons() {
        return buttons;
    }

    public void addButton(LircConfigButton button) {
        buttons.add(button);
    }

    public boolean handles(LircEvent e) {
        String remote = e.getRemote();
        String button  = e.getButton();

        for(LircConfigButton cBut: buttons) {
            String cRemote = cBut.getRemote();
            String cButton = cBut.getButton();
            if (!cRemote.equals("*") && !cRemote.equalsIgnoreCase(remote))
                continue;
            if (!cButton.equals("*") && !cButton.equalsIgnoreCase(button))
                continue;
            return true;
        }
        
        return false;
    }
}
