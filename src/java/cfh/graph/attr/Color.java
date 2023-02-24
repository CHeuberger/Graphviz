/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph.attr;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
public final class Color extends Attribute implements GraphAttr, NodeAttr, EdgeAttr {


    public static final Color BLACK = new Color("black");
    public static final Color WHITE = new Color("white");
    public static final Color RED = new Color("red");
    
    public Color(String value) {
        super("color", value);
    }
    
    private Color(String key, String value) {
        super("bgcolor", value);
    }
    
    public Color background() {
        return new Color("bgcolor", (String) value);
    }
}
