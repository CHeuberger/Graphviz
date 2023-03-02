/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

import static java.util.Objects.*;

import cfh.graph.Attr.GraphAttr;

/**
 * @author Carlos F. Heuberger, 2023-03-01
 *
 */
public final class Subgraph extends StatementList<Subgraph> implements Statement<GraphAttr>, Source, Target {

    final String name;
    
    Subgraph() {
        name = null;
    }
    
    Subgraph(String name) {
        this.name = requireNonNull(name, "null name");
    }
    
    @Override
    public Edge to(Target target) {
        return new Edge(this, target);
    }

    @Override
    public String format(int indent, Graph graph) {
        if (name == null) {
            return super.format(indent+1, graph);
        } else if (name.isEmpty()){
            return "subgraph " + super.format(indent+1, graph);
        } else {
            return "subgraph " + name + " " + super.format(indent+1, graph);
        }
    }
}
