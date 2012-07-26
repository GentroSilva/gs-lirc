/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
