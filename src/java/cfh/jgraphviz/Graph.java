/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static java.util.Objects.requireNonNull;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.function.Consumer;

import cfh.jgraphviz.Dot.Engine;
import cfh.jgraphviz.Dot.Format;

/**
 * @author Carlos F. Heuberger, 2023-03-03
 *
 */
public interface Graph extends StatementList<Graph> {

    public default Graph strict() { return strict(true); }
    public Graph strict(boolean strict);
    
    public default Graph directed() { return directed(true); }
    public Graph directed(boolean directed);

    public Graph with(GraphAttr first, GraphAttr... attributes);
    
    public Graph visit(Consumer<String> visitor);

    public default BufferedImage image(Format format) { return image(Engine.DOT, format); }
    public BufferedImage image(Engine engine, Format format);

}

/**
 * @author Carlos F. Heuberger, 2023-03-03
 *
 */
class GraphImpl extends StatementListImpl<Graph> implements Graph {

    private final String id;
    
    private boolean strict = false;
    private boolean directed = false;
    
    GraphImpl() {
        id = null;
    }
    
    GraphImpl(String id) {
        this.id = requireNonNull(id, "null id");
    }
    
    @Override
    public Graph strict(boolean b) {
        // TODO allowed
        this.strict = b;
        return this;
    }
    
    boolean isStrict() {
        return strict;
    }
    
    @Override
    public Graph directed(boolean b) {
        // TODO allowed
        this.directed = b;
        return this;
    }
    
    boolean isDirected() {
        return directed;
    }
    
    @Override
    public Graph with(GraphAttr first, GraphAttr... attributes) {
        super.with(first, attributes);
        return this;
    }
    
    @Override
    public Graph visit(Consumer<String> visitor) {
        var text = script();
        visitor.accept(text);
        return this;
    }

    @Override
    public BufferedImage image(Engine engine, Format format) {
        // TODO engines
        var text = script();
        try {
            return Dot.dotToImage(engine, format, text);
        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    private String script() {
        var dot = new StringBuilder();
        if (strict) {
            dot.append("strict ");
        }
        dot.append(directed ? "digraph " : "graph ");
        if (id != null) {
            dot.append(id).append(" ");
        }
        dot.append("{\n");

        // TODO
        
        dot.append("}");
        
        return dot.toString();
    }
}
