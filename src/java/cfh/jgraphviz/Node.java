/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static cfh.jgraphviz.Dot.*;
import static java.util.Objects.*;

/**
 * @author Carlos F. Heuberger, 2023-03-03
 *
 */
public interface Node {

}

/**
 * @author Carlos F. Heuberger, 2023-03-06
 *
 */
class NodeImpl extends AttributeHolder implements NodeId, SourceTarget {
    
    final String id;
    
    NodeImpl(String id) {
        this.id = requireNonNull(id, "null id");
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
    public Node with(NodeAttr... attributes) {
        addAll(attributes);
        return this;
    }
    
    @Override
    public String script(GraphImpl graph) {
        return quote(id) + super.script();
    }
}
