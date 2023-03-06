/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graphviz;

import static java.util.Objects.*;
import static cfh.graphviz.Dot.*;

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
        statements.add(new CompStatement<>(subgraph));
        return (T) this;
    }
    
    //----------------------------------------------------------------------------------------------
    
    sealed static abstract class Statement {
        //
    }
    
    private static final class NodeStatement extends Statement {

        final NodeImpl node;
        
        NodeStatement(Node node) {
            this.node = (NodeImpl) requireNonNull(node, "null node");
        }
    }
    
    private static final class EdgeStatement extends Statement {
        
        final EdgeImpl edge;
        
        EdgeStatement(Edge edge) {
            this.edge = (EdgeImpl) requireNonNull(edge, "null edge");
        }
    }
    
    private static final class CompStatement<T> extends Statement {
        final T comp;
        
        CompStatement(T comp) {
            this.comp = requireNonNull(comp, "null component");
        }
    }

    private sealed static class DefaultStatement extends Statement {

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