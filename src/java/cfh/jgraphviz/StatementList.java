/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static cfh.jgraphviz.Dot.*;
import static java.util.Objects.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Carlos F. Heuberger, 2023-03-03
 *
 */
public interface StatementList<T extends StatementList<T>> {

    public T graphs(GraphAttr first, GraphAttr... defaults);
    public T nodes(NodeAttr first, NodeAttr... defaults);
    public T edges(EdgeAttr first, EdgeAttr... defaults);
    
    public T add(Node node);
    public T add(Edge edge);
    public T add(Subgraph subgraph);
}

@SuppressWarnings("unchecked")
class StatementListImpl<T extends StatementList<T>> implements StatementList<T> {

    private final List<Statement> statements = new ArrayList<>();
    @Override
    public T graphs(GraphAttr first, GraphAttr... defaults) {
        statements.add(new GraphDefaultStatement(first, defaults));
        return (T) this;
    }

    @Override
    public T nodes(NodeAttr first, NodeAttr... defaults) {
        statements.add(new NodeDefaultStatement(first, defaults));
        return (T) this;
    }

    @Override
    public T edges(EdgeAttr first, EdgeAttr... defaults) {
        statements.add(new EdgeDefaultStatement(first, defaults));
        return (T) this;
    }

    @Override
    public T add(Node node) {
        statements.add(new NodeStatement(node));
        return (T) this;
    }

    @Override
    public T add(Edge edge) {
        statements.add(new EdgeStatement(edge));
        return (T) this;
    }

    @Override
    public T add(Subgraph subgraph) {
        statements.add(new SubgraphStatement(subgraph));
        return (T) this;
    }

    protected void with(Attr first, Attr... attributes) {
        statements.add(new AttrStatement(first));
    }

    //----------------------------------------------------------------------------------------------
    
    sealed static interface Statement {
        //
    }
    
    private static final class NodeStatement implements Statement {

        final NodeImpl node;
        
        NodeStatement(Node node) {
            this.node = (NodeImpl) requireNonNull(node, "null node");
        }
    }
    
    private static final class EdgeStatement implements Statement {
        
        final EdgeImpl edge;
        
        EdgeStatement(Edge edge) {
            this.edge = (EdgeImpl) requireNonNull(edge, "null edge");
        }
    }
    
    private static final class SubgraphStatement implements Statement {
        final SubgraphImpl subgraph;
        
        SubgraphStatement(Subgraph subgraph) {
            this.subgraph = (SubgraphImpl) requireNonNull(subgraph, "null subgraph");
        }
    }
    
    private static final class AttrStatement implements Statement {

        final Attr attr; 
        
        AttrStatement(Attr attr) {
            this.attr = requireNonNull(attr, "null attr");
        }
    }

    private sealed static class DefaultStatement implements Statement {

        final String name;
        private final List<Attribute> attributes = new ArrayList<>();
        
        protected DefaultStatement(String name, Attr first, Attr... defaults) {
            this.name = requireNonNull(name, "null name");
            add(first);
            add(defaults);
        }
        
        private void add(Attr... attrs) {
            for (var attr : attrs) {
                attributes.add((Attribute) requireNonNull(attr, "null attribute"));
            }
        }
    }
    
    private final class GraphDefaultStatement extends DefaultStatement {
        protected GraphDefaultStatement(GraphAttr first, GraphAttr... defaults) {
            super("graph", first, defaults);
        }
    }
    
    private final class NodeDefaultStatement extends DefaultStatement {
        protected NodeDefaultStatement(NodeAttr first, NodeAttr... defaults) {
            super("node", first, defaults);
        }
    }
    
    private final class EdgeDefaultStatement extends DefaultStatement {
        protected EdgeDefaultStatement(EdgeAttr first, EdgeAttr... defaults) {
            super("edge", first, defaults);
        }
    }
}