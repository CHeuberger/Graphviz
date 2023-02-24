/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

import static cfh.graph.Dot.*;
import static java.util.Objects.*;

import cfh.graph.attr.NodeAttr;
import cfh.graph.attr.NodeAttribute;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
public final class Node implements Statement, Source, Target {

    final String name;
    
    private final AttrList<NodeAttr> attrList = new AttrList<>();
    
    Node(String name) {
        this.name = requireNonNull(name, "null name");
    }
    
    @Override
    public String name() {
        return name;
    }
    
    @Override
    public Statement with(String name, Object value) {
        attrList.add(new NodeAttribute(name, value.toString()));
        return this;
    }
    
    @Override
    public Link to(Target target) {
        return new Link(this, target);
    }
    
    @Override
    public String format(Graph graph) {
        return "%s %s;".formatted(quote(name), attrList.format());
    }
}
