/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph.attr;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
public final class FontColor extends ColorAttribute<FontColor> implements GraphAttr, NodeAttr, EdgeAttr, ClusterAttr {

    protected FontColor(String value) {
        super("fontcolor", value);
    }
    
    @Override
    protected FontColor clone(String newValue) {
        return new FontColor(newValue);
    }
}
