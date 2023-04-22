/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static cfh.jgraphviz.Dot.quote;
import static java.util.Objects.requireNonNull;

import cfh.jgraphviz.StatementListImpl.Statement;

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
class NodeImpl implements NodeId {
    
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
    public Node with(NodeAttr first, NodeAttr... attributes) {
        // TODO Auto-generated method stub
        return null;
    }
}
