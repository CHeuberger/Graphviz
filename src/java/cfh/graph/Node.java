/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

import static cfh.graph.Dot.*;
import static java.util.Objects.*;

import java.util.Arrays;

import cfh.graph.Attr.NodeAttr;

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
    
    /** Adds attributes to this node. */
    @Override
    public Statement<NodeAttr> with(NodeAttr... attributes) {
        this.attributes.addAll(Arrays.asList(attributes));
        return this;
    }
    
    @Override
    public Edge to(Target target) {
        return new Edge(this, target);
    }
    
    @Override
    public String format(Graph graph) {
        return "%s %s".formatted(quote(name), attributes.format());
    }
}
