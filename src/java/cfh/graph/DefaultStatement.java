/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

import static java.util.Objects.*;

import java.util.Collection;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 * @param <T> 
 *
 */
final class DefaultStatement<T extends Attr> implements Statement<T> {

    enum Type {
        GRAPH, NODE, EDGE;
    }
    
    private final Type type;
    private final AttrList<T> attributes = new AttrList<>();
    
    DefaultStatement(Type type, Collection<T> attributes) {
        this.type = requireNonNull(type, "null type");
        this.attributes.addAll(requireNonNull(attributes, "null attributes"));
    }

    /** Unsupported. */
    @Override
    @SuppressWarnings("unchecked")
    public DefaultStatement<T> with(T... attr) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String format(Graph graph) {
        return "%s %s".formatted(type.name().toLowerCase(), attributes.format());
    }
}
