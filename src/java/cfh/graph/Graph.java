/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

import static java.util.Objects.*;
import static cfh.graph.Dot.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.function.Consumer;

import cfh.graph.Dot.Format;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
public final class Graph extends StatementList<Graph> {

    final String name;
    
    private boolean strict = false;
    private boolean directed = false;
    
    Graph() {
        this.name = null;
    }
    
    Graph(String name) {
        this.name = requireNonNull(name, "null name");
    }

    /** Sets the graph to directed. */
    public Graph strict() {
        return strict(true);
    }
    
    /** Sets strict or not. */
    public Graph strict(boolean strict) {
        if (!statements.isEmpty())
            throw new IllegalStateException("must be called before any Statement is added");
        this.strict = strict;
        return this;
    }
    
    boolean isStrict() {
        return strict;
    }
    
    /** Sets the graph to directed. */
    public Graph directed() {
        return directed(true);
    }
    
    /** Sets the graph to undirected. */
    public Graph undirected() {
        return directed(false);
    }
    
    /** Sets the graph to directed or not. */
    public Graph directed(boolean directed) {
        if (!statements.isEmpty())
            throw new IllegalStateException("must be called before any Statement is added");
        this.directed = directed;
        return this;
    }
    
    boolean isDirected() {
        return directed;
    }
    
    /** Helper to consume the created dot string. */
    public Graph visit(Consumer<String> visitor) {
        visitor.accept(toDot());
        return this;
    }
    
    /** Creates an image and saves to a file. */
    public Graph save(Format format, File file) throws IOException, InterruptedException {
        try (var output = new FileOutputStream(file)) {
            dot(format, toDot(), output);
        }
        return this;
    }
    
    /** Creates an image of this graph. */
    public BufferedImage toImage(Format format) {
        // TODO more engines?
        try {
            return dotToImage(format, toDot());
        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    /** Creates a dot string of this graph. */
    public String toDot() {
        var lineFormat = INDENT + "%s;\n";
        var dot = new StringBuilder();
        if (strict) dot.append("strict ");
        dot.append(directed ? "digraph" : "graph");
        if (name != null) dot.append(" ").append(quote(name));
        dot.append(" {\n");
        
        attributes.stream().map(Attribute::format).map(lineFormat::formatted).forEach(dot::append);
        dot.append("\n");
        statements.stream().map(s -> s.format(0, this)).map(lineFormat::formatted).forEach(dot::append);
        
        dot.append("}\n");
        return dot.toString();
    }
}
