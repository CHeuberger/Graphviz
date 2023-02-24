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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import cfh.graph.attr.GraphAttr;
import cfh.graph.attr.GraphAttribute;
import cfh.graph.engine.DotEngine;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
public class Graph {

    final String name;
    
    private boolean strict = false;
    private boolean directed = false;
    
    private final AttrList<GraphAttr> attributes = new AttrList<>();
    private final List<Statement<?>> statements = new ArrayList<>();

    Graph(String name) {
        this.name = requireNonNull(name, "null name");
    }

    public Graph strict(boolean strict) {
        if (!statements.isEmpty())
            throw new IllegalStateException("must be called before any Statement is added");
        this.strict = strict;
        return this;
    }
    
    public boolean strict() {
        return strict;
    }
    
    public Graph directed(boolean directed) {
        if (!statements.isEmpty())
            throw new IllegalStateException("must be called before any Statement is added");
        this.directed = directed;
        return this;
    }
    
    public boolean directed() {
        return directed;
    }
    
    public Graph with(GraphAttr attribute) {
        attributes.add(attribute);
        return this;
    }
    
    public Graph with(String name, Object value) {
        return with(new GraphAttribute(name, value));
    }

    public Graph add(Statement<?>... statements) {
        this.statements.addAll(Arrays.asList(statements));
        return this;
    }

    public Graph visit(Consumer<String> visitor) {
        visitor.accept(toDot());
        return this;
    }

    public BufferedImage toImage(Format format) throws IOException, InterruptedException {
        // TODO more engines?
        return DotEngine.dotToImage(format, toDot());
    }
    
    public Graph save(Format format, File file) throws IOException, InterruptedException {
        try (var output = new FileOutputStream(file)) {
            DotEngine.dot(format, toDot(), output);
        }
        return this;
    }
    
    public String toDot() {
        var dot = new StringBuilder();
        if (strict) dot.append("strict ");
        dot.append(directed ? "digraph" : "graph");
        if (name != null) dot.append(" ").append(quote(name));
        dot.append(" {\n");
        
        attributes.stream().map(GraphAttr::format).map("  %s;\n"::formatted).forEach(dot::append);
        dot.append("\n");
        statements.stream().map(s -> s.format(this)).map("  %s\n"::formatted).forEach(dot::append);
        
        dot.append("}\n");
        return dot.toString();
    }
}
