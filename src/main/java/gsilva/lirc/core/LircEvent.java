/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
