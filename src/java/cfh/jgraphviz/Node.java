/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static java.util.Objects.*;

import static cfh.jgraphviz.Dot.*;

/**
 * @author Carlos F. Heuberger, 2023-03-03
 *
 */
public interface Node {
    //
}

/**
 * @author Carlos F. Heuberger, 2023-03-06
 *
 */
class NodeImpl extends AttributeHolder implements NodeId, SourceTarget {
    
    private final String id;
    private final PortImpl port; 
    
    NodeImpl(String id) {
        this.id = requireNonNull(id, "null id");
        this.port = null;
    }
    
    NodeImpl(String id, Port port) {
        this.id = requireNonNull(id, "null id");
        this.port = requireNonNull((PortImpl) port, "null port");
    }
    
    NodeImpl(NodeImpl org) {
        super(org);
        this.id = org.id;
        this.port = org.port;
    }

    @Override
    public Edge to(Target target) {
        return new EdgeImpl(this, target);
    }
    
    @Override
    public Edge from(Source source) {
        return new EdgeImpl(source, this);
    }

    @Override
    public Node with(Attr.N... attributes) {
        addAll(attributes);
        return this;
    }
    
    @Override
    public String script(GraphImpl graph) {
        return quote(id)
            + (port == null ? "" : ":" + port.value())
            + super.script();
    }
}
