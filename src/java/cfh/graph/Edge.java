/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

import static cfh.graph.Dot.*;
import static java.util.Objects.*;

import java.util.Arrays;

import cfh.graph.Attr.EdgeAttr;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
public final class Edge implements Statement<EdgeAttr> {

    final Source source;
    final Target target;
    
    private final AttrList<EdgeAttr> attributes = new AttrList<>();
    
    Edge(Source source, Target target) {
        this.source = requireNonNull(source, "null source");
        this.target = requireNonNull(target, "null target");
    }
    
    /** Adds attributes to this edge. */
    @Override
    public Statement<EdgeAttr> with(EdgeAttr... attributes) {
        this.attributes.addAll(Arrays.asList(attributes));
        return this;
    }
    
    @Override
    public String format(int indent, Graph graph) {
        return "%s%s %s %s %s".formatted(
            INDENT.repeat(indent),
            source.format(0, graph),
            graph.isDirected() ? "->" : "--",
            target.format(0, graph),
            attributes.format()
            );
    }
}
