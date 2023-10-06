/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static java.util.Objects.*;
import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author Carlos F. Heuberger, 2023-03-03
 *
 */
public interface StatementList<T extends StatementList<T>> {

    public T graphdefs(GraphAttr... defaults);
    public T nodedefs(NodeAttr... defaults);
    public T edgedefs(EdgeAttr... defaults);
    
    public T add(Node node);
    public T add(Edge edge);
    public T add(Subgraph subgraph);
}

@SuppressWarnings("unchecked")
class StatementListImpl<T extends StatementList<T>> implements StatementList<T> {

    private final List<Statement> statements = new ArrayList<>();
    @Override
    public T graphdefs(GraphAttr... defaults) {
        if (defaults.length > 0) {
            statements.add(new GraphDefaultStatement(defaults));
        }
        return (T) this;
    }

    @Override
    public T nodedefs(NodeAttr... defaults) {
        if (defaults.length > 0) {
            statements.add(new NodeDefaultStatement(defaults));
        }
        return (T) this;
    }

    @Override
    public T edgedefs(EdgeAttr... defaults) {
        if (defaults.length > 0) {
            statements.add(new EdgeDefaultStatement(defaults));
        }
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

    protected void with(Attr... attributes) {
        Arrays.stream(attributes).map(AttrStatement::new).forEach(statements::add);
    }
    
    protected String scriptStatements(GraphImpl graph) {
//        UnaryOperator<String> appendSemiColon = s -> s + ";";
        return statements
            .stream()
            .map(s -> s.script(graph))
            .filter(Objects::nonNull)
//            .map(appendSemiColon)
            .collect(joining("\n"));
    }

    //----------------------------------------------------------------------------------------------
    
    sealed static interface Statement {
        
        public String script(GraphImpl graph);
    }
    
    private static final class NodeStatement implements Statement {

        final NodeImpl node;
        
        NodeStatement(Node node) {
            this.node = (NodeImpl) requireNonNull(node, "null node");
        }

        @Override
        public String script(GraphImpl graph) {
            return node.script(graph);
        }
    }
    
    private static final class EdgeStatement implements Statement {
        
        final EdgeImpl edge;
        
        EdgeStatement(Edge edge) {
            this.edge = (EdgeImpl) requireNonNull(edge, "null edge");
        }

        @Override
        public String script(GraphImpl graph) {
            return edge.script(graph);
        }
    }
    
    private static final class SubgraphStatement implements Statement {
        final SubgraphImpl subgraph;
        
        SubgraphStatement(Subgraph subgraph) {
            this.subgraph = (SubgraphImpl) requireNonNull(subgraph, "null subgraph");
        }

        @Override
        public String script(GraphImpl graph) {
            return subgraph.script(graph);
        }
    }
    
    private static final class AttrStatement implements Statement {

        final Attribute attr; 
        
        AttrStatement(Attr attr) {
            this.attr = (Attribute) requireNonNull(attr, "null attr");
        }

        @Override
        public String script(GraphImpl graph) {
            return attr.script();
        }
    }

    private sealed static class DefaultStatement extends AttributeHolder implements Statement {

        final String type;
        
        protected DefaultStatement(String type, Attr... defaults) {
            this.type = requireNonNull(type, "null type");
            if (defaults.length == 0) {
                throw new IllegalArgumentException("empty defaults");
            }
            addAll(defaults);
        }

        @Override
        public String script(GraphImpl graph) {
            return type + super.script();
        }
    }
    
    private final class GraphDefaultStatement extends DefaultStatement {
        protected GraphDefaultStatement(GraphAttr... defaults) {
            super("graph", defaults);
        }
    }
    
    private final class NodeDefaultStatement extends DefaultStatement {
        protected NodeDefaultStatement(NodeAttr... defaults) {
            super("node", defaults);
        }
    }
    
    private final class EdgeDefaultStatement extends DefaultStatement {
        protected EdgeDefaultStatement(EdgeAttr... defaults) {
            super("edge", defaults);
        }
    }
}