/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gsilva.lirc.lircclient.config;

/**
 *
 * @author bdickie
 */
public class LircConfigButton {
    private String remote;
    private String button;

    public LircConfigButton() {
    }

    public LircConfigButton(String remote, String button) {
        this.remote = remote;
        this.button = button;
    }

    public String getButton() {
        return button;
    }

    public void setButton(String button) {
        this.button = button;
    }

    public String getRemote() {
        return remote;
    }

    public void setRemote(String remote) {
        this.remote = remote;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", getRemote(), getButton());
    }


    
}
