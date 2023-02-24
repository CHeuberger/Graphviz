/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph.attr;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
public sealed abstract class ColorAttribute<T extends ColorAttribute<T>> extends Attribute 
permits Color, BackgroundColor, FillColor, FontColor {

    protected ColorAttribute(String name, String value) {
        super(name, value);
    }
    
    protected abstract T clone(String newValue); 
    
    public T gradient(String to) {
        return clone(value + ":" + to);
    }
    
    public T and(double at, String to) {
        return clone(value + ";" + at + ":" + to);
    }
    
    public T gradient(Color to) {
        return gradient((String) to.value);
    }
    
    public T and(double at, Color to) {
        return and(at, (String) to.value);
    }
}
