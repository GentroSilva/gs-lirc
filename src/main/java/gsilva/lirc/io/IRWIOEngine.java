/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gsilva.lirc.io;

/**
 *
 * @author bdickie
 */
public class IRWIOEngine extends CommandLineIOEngine {

    private static final String IRW_CMD = "irw";

    public IRWIOEngine() {
        super(IRW_CMD);
    }

    public IRWIOEngine(String device) {
        super(IRW_CMD, device);
    }

}
