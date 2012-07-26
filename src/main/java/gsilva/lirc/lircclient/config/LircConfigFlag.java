/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gsilva.lirc.lircclient.config;

/**
 *
 * @author bdickie
 */
public enum LircConfigFlag {
    Quit("quit"),
    ToggleReset("toggle_reset"),
    
    Once("once"),
    Mode("mode"),
    StartupMode("startup_mode");

    private String fileDesc;

    private LircConfigFlag(String fileDesc) {
        this.fileDesc = fileDesc;
    }

    public String getFileDesc() {
        return fileDesc;
    }

    public static LircConfigFlag lookup(String fileDesc) {
        for(LircConfigFlag flag: values())
            if (flag.fileDesc.equalsIgnoreCase(fileDesc))
                return flag;
        return null;
    }


}
