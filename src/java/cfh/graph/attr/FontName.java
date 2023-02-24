/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph.attr;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
public final class FontName extends Attribute implements GraphAttr, NodeAttr, EdgeAttr, ClusterAttr {

    public FontName(String value) {
        super("fontname", value);
    }
}
