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
public final class Node implements Statement<NodeAttr>, Source, Target {

    final String name;
    
    private final AttrList<NodeAttr> attributes = new AttrList<>();
    
    Node(String name) {
        this.name = requireNonNull(name, "null name");
    }
    
    @Override
    public String name() {
        return name;
    }
    
    @Override
    public Statement<NodeAttr> with(String name, Object value) {
        attributes.add(new NodeAttribute(name, value.toString()));
        return this;
    }
    
    @Override
    public Statement<NodeAttr> with(NodeAttr attr) {
        attributes.add(attr);
        return this;
    }
    
    @Override
    public Edge to(Target target) {
        return new Edge(this, target);
    }
    
    @Override
    public String format(Graph graph) {
        return "%s %s;".formatted(quote(name), attributes.format());
    }
}
