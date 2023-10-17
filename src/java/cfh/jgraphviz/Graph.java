/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static java.util.Objects.*;

import static cfh.jgraphviz.Dot.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Formatter;
import java.util.function.Consumer;

import cfh.jgraphviz.Dot.Engine;
import cfh.jgraphviz.Dot.Format;

/**
 * @author Carlos F. Heuberger, 2023-03-03
 *
 */
public sealed interface Graph extends StatementList<Graph> {

    /** Describe this a graph as <code>strict</code>, that is, forbids the creation of multi-edges. */
    public default Graph strict() { return strict(true); }
    /** Forbids the creation of multi-edges, if <code>strict</code> is set to <code>true</code>. */
    public Graph strict(boolean strict);
    
    /** Marks this Graph as direcred. */
    public default Graph directed() { return directed(true); }
    /** Marks this Graph as directed (<code>true</code>) or undirected (<code>false</code>). */
    public Graph directed(boolean directed);

    /** Add attributes to this Graph. */
    public Graph with(Attr.G... attributes);
    
    /** Calls <code>visitor</code> with the dot script for this Graph. For debugging. */
    public Graph visit(Consumer<String> visitor);

    /** Creates the image of this Graph in given <code>format</code>. */
    public BufferedImage image(Format format);
    /** Creates the image of this Graph in given <code>format</code> using <code>engine<code>. */
    public BufferedImage image(Engine engine, Format format);

}

/**
 * @author Carlos F. Heuberger, 2023-03-03
 *
 */
final class GraphImpl extends StatementListImpl<Graph> implements Graph {

    private final String id;
    
    private boolean strict = false;
    protected boolean directed = false;
    
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
        // TODO allowed?
        this.directed = b;
        return this;
    }
    
    boolean isDirected() {
        return directed;
    }
    
    @Override
    public Graph with(Attr.G... attributes) {
        super.with(attributes);
        return this;
    }
    
    @Override
    public Graph visit(Consumer<String> visitor) {
        var text = script();
        visitor.accept(text);
        return this;
    }
    
    @Override
    public BufferedImage image(Format format) {
        return this.image(Engine.DOT, format);
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
        var formatter = new Formatter();
        try (formatter) {
            if (strict) {
                formatter.format("strict ");
            }
            formatter.format("%s ", directed ? "digraph" : "graph");
            if (id != null) {
                formatter.format("%s ", quote(id));
            }
            formatter.format("""
                {
                %s
                }
                """, 
                scriptStatements(this).indent(INDENT).stripTrailing());  // remove trailing linefeed

            return formatter.toString();
        }
    }
}
