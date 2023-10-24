/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static java.util.Objects.*;

import java.util.Locale;

/**
 * @author Carlos F. Heuberger, 2023-03-03
 *
 */
public interface Edge {

    public Edge with(Attr.E... attributes);
}

/**
 * @author Carlos F. Heuberger, 2023-03-06
 *
 */
class EdgeImpl extends AttributeHolder implements Edge {

    private final SourceTarget source;
    private final SourceTarget target;
    
    EdgeImpl(Source source, Target target) {
        this.source = (SourceTarget) requireNonNull(source, "null source");
        this.target = (SourceTarget) requireNonNull(target, "null target");
    }
    
    EdgeImpl(EdgeImpl org) {
        super(org);
        this.source = org.source;
        this.target = org.target;
    }

    @Override
    public Edge with(Attr.E... attributes) {
        addAll(attributes);
        return this;
    }
    
    String script(GraphImpl graph) {
        return String.format(Locale.ROOT, 
            "%s %s %s%s",
            source.script(graph),
            graph.isDirected() ? "->" : "--",
            target.script(graph),
            super.script());
    }
}
