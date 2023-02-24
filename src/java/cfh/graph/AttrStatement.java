/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

import static java.util.Objects.*;

import cfh.graph.attr.GraphAttr;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
public final class AttrStatement implements Statement<GraphAttr> {

    private final GraphAttr attribute;
    
    public AttrStatement(GraphAttr attribute) {
        this.attribute = requireNonNull(attribute, "null attribute");
    }

    @Override
    public AttrStatement with(String name, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AttrStatement with(GraphAttr attr) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String format(Graph graph) {
        return attribute.format();
    }
}
