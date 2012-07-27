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

import java.util.EventObject;

/**
 *
 * @author bdickie
 */
public class LircEvent extends EventObject {

    private String code;
    private int repeat;
    private String button;
    private String remote;

    public LircEvent(Object source) {
        super(source);
    }

    public LircEvent(Object source, String code, int repeat, String button, String remote) {
        super(source);
        
        this.code = code;
        this.repeat = repeat;
        this.button = button;
        this.remote = remote;
    }

    

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRemote() {
        return remote;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    @Override
    public String toString() {
        return String.format("%s %s %03d", getRemote(), getButton(), getRepeat());
    }


}
