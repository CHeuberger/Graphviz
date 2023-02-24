/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph.attr;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
public final class BackgroundColor extends ColorAttribute<BackgroundColor> implements GraphAttr, ClusterAttr {

    protected BackgroundColor(String value) {
        super("bgcolor", value);
    }
    
    @Override
    protected BackgroundColor clone(String newValue) {
        return new BackgroundColor(newValue);
    }
}
