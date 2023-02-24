/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph.attr;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
public final class FillColor extends ColorAttribute<FillColor> implements NodeAttr, EdgeAttr, ClusterAttr {

    protected FillColor(String value) {
        super("fillcolor", value);
    }
    
    @Override
    protected FillColor clone(String newValue) {
        return new FillColor(newValue);
    }
}
