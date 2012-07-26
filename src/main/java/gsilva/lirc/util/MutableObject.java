/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gsilva.lirc.util;

/**
 *
 * @author bdickie
 */
public class MutableObject<T> {
    
    private T value;

    public MutableObject() {
    }

    public MutableObject(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
