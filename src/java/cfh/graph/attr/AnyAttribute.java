/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph.attr;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
public final class AnyAttribute extends Attribute implements GraphAttr, NodeAttr, EdgeAttr, ClusterAttr {

    public AnyAttribute(String name, Object value) {
        super(name, value);
    }
}
