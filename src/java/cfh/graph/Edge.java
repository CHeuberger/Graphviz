/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

import static cfh.graph.Dot.*;
import static java.util.Objects.*;

import cfh.graph.attr.EdgeAttr;
import cfh.graph.attr.EdgeAttribute;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
public final class Edge implements Statement<EdgeAttr> {

    final Source source;
    final Target target;
    
    private final AttrList<EdgeAttr> attrList = new AttrList<>();
    
    Edge(Source source, Target target) {
        this.source = requireNonNull(source, "null source");
        this.target = requireNonNull(target, "null target");
    }
    
    @Override
    public Statement<EdgeAttr> with(String name, Object value) {
        attrList.add(new EdgeAttribute(name, value.toString()));
        return this;
    }
    
    @Override
    public Statement<EdgeAttr> with(EdgeAttr attr) {
        attrList.add(attr);
        return this;
    }
    
    @Override
    public String format(Graph graph) {
        return "%s %s %s %s;".formatted(
            quote(source.name()),
            graph.directed() ? "->" : "--",
            quote(target.name()),
            attrList.format()
            );
    }
}
