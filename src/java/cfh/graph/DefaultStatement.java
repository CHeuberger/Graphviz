/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

import static java.util.Objects.*;

import java.util.Collection;

import cfh.graph.attr.Attr;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 * @param <T> 
 *
 */
public final class DefaultStatement<T extends Attr> implements Statement<T> {

    public enum Type {
        GRAPH, NODE, EDGE;
    }
    
    private final Type type;
    private final AttrList<T> attributes = new AttrList<>();
    
    public DefaultStatement(Type type, Collection<T> attributes) {
        this.type = requireNonNull(type, "null type");
        this.attributes.addAll(requireNonNull(attributes, "null attributes"));
    }

    @Override
    public DefaultStatement<T> with(String name, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DefaultStatement<T> with(T attr) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String format(Graph graph) {
        return "%s %s".formatted(type.name().toLowerCase(), attributes.format());
    }
}
