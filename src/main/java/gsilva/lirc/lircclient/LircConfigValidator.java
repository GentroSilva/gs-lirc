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
