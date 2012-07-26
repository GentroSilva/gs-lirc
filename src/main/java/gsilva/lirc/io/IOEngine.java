/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gsilva.lirc.io;

import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author bdickie
 */
public interface IOEngine {
    public static final String DEFAULT_DEVICE = null;

    public boolean startIOEngine();
    public void stopIOEngine();
    public InputStream getInputStream();
    public OutputStream getOutputStream();
}
