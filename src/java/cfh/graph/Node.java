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
    final String port;
    
    private final AttrList<NodeAttr> attributes = new AttrList<>();
    
    Node(String name) {
        this.name = requireNonNull(name, "null name");
        this.port = null;
    }
    
    Node(String name, Port port) {
        this.name = requireNonNull(name, "null name");
        this.port = requireNonNull(port, "null port").format();
    }
    
    Node(String name, String portId) {
        this.name = requireNonNull(name, "null name");
        this.port = requireNonNull(portId, "null portId");
    }
    
    Node(String name, String portId, Port port) {
        this.name = requireNonNull(name, "null name");
        this.port = requireNonNull(portId, "null portId") + ":" + requireNonNull(port, "null port").format();
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
    public String format(int indent, Graph graph) {
        if (port == null) {
            return "%s %s".formatted(quote(name), attributes.format());
        } else {
            return "%s:%s %s".formatted(quote(name), port, attributes.format());
        }
    }
}
