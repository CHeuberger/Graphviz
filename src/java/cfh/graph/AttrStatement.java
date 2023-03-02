/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

import static java.util.Objects.*;

import cfh.graph.Attr.GraphAttr;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
final class AttrStatement implements Statement<GraphAttr> {

    private final Attribute attribute;
    
    AttrStatement(GraphAttr attribute) {
        this.attribute = (Attribute) requireNonNull(attribute, "null attribute");
    }

    /** Unsupported. */
    @Override
    public AttrStatement with(GraphAttr... attr) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String format(int indent, Graph graph) {
        return attribute.format();
    }
}
