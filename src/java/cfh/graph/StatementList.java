/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

import static cfh.graph.Dot.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import cfh.graph.DefaultStatement.Type;
import cfh.graph.Attr.EdgeAttr;
import cfh.graph.Attr.GraphAttr;
import cfh.graph.Attr.NodeAttr;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
@SuppressWarnings("unchecked")
public abstract sealed class StatementList<T extends StatementList<T>> 
permits Graph, Subgraph {

    protected final AttrList<Attribute> attributes = new AttrList<>();
    protected final List<Statement<?>> statements = new ArrayList<>();

    protected StatementList() {
    }
    
    /** Add Graph attributes. */
    public T with(GraphAttr... attributes) {
        Arrays.stream(attributes).map(Attribute.class::cast).forEach(this.attributes::add);
        return (T) this;
    }

    /** Adds nodes and edges. */
    public T add(Statement<?>... statements) {
        this.statements.addAll(Arrays.asList(statements));
        return (T) this;
    }
    
    /** Adds attributes to this graph. */
    public T add(GraphAttr... attrs) {
        Arrays.stream(attrs).map(AttrStatement::new).forEach(statements::add);
        return (T) this;
    }
    
    /** Adds default graph attributes. */
    public T defaults(GraphAttr... attributes) {
        return add(new DefaultStatement<>(Type.GRAPH, Arrays.asList(attributes)));
    }
    
    /** Adds default node attributes. */
    public T nodes(NodeAttr... attributes) {
        return add(new DefaultStatement<>(Type.NODE, Arrays.asList(attributes)));
    }
    
    /** Adds default edge attributes. */
    public T edges(EdgeAttr... attributes) {
        return add(new DefaultStatement<>(Type.EDGE, Arrays.asList(attributes)));
    }
    
    protected String format(int indent, Graph graph) {
        var lineFormat = INDENT.repeat(indent+1) + "%s;\n";
        var dot = new StringBuilder();
        
        dot.append("{\n");
        
        attributes.stream().map(Attribute::format).map(lineFormat::formatted).forEach(dot::append);
        if (!attributes.isEmpty() && !statements.isEmpty()) {
            dot.append("\n");
        }
        statements.stream().map(s -> s.format(indent, graph)).map(lineFormat::formatted).forEach(dot::append);
        
        dot.append(INDENT.repeat(indent)).append("}");
        
        return dot.toString();
    }
}
