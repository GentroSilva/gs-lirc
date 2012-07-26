/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gsilva.lirc.lircclient;

import gsilva.lirc.util.MutableObject;

/**
 *
 * @author bdickie
 */
public interface LircConfigValidator {
    /**
     * Check a config string specified in a lircrc file for validity.
     * @param config The config string to valid. You can change the string if you want to make
     *          a clean up to the string, which rejecting it all together.
     * @return An error string if the config should be rejected, null otherwise.
     */
    public String validateLircConfig(MutableObject<String> config);
}
