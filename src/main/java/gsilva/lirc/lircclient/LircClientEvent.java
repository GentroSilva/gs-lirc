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
import java.awt.AWTEvent;

/**
 *
 * @author bdickie
 */
public class LircClientEvent extends AWTEvent {

    public static final int LIRC_CLIENT_EVENT_ID = AWTEvent.RESERVED_ID_MAX + 10238;

    private String lircCommand;
    private LircEvent underlyingEvent;

    public LircClientEvent(Object source, String lircCommand, LircEvent underlyingEvent) {
        super(source, LIRC_CLIENT_EVENT_ID);
        this.lircCommand = lircCommand;
        this.underlyingEvent = underlyingEvent;
    }

    public String getLircCommand() {
        return lircCommand;
    }

    public void setLircCommand(String lircCommand) {
        this.lircCommand = lircCommand;
    }

    public LircEvent getUnderlyingEvent() {
        return underlyingEvent;
    }

    public void setUnderlyingEvent(LircEvent underlyingEvent) {
        this.underlyingEvent = underlyingEvent;
    }

    @Override
    public String toString() {
        return String.format("LircClientEvent: %s", lircCommand);
    }


}
