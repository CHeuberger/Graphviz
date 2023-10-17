/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static java.util.Objects.*;

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

    final SourceTarget source;
    final SourceTarget target;
    
    EdgeImpl(Source source, Target target) {
        this.source = (SourceTarget) requireNonNull(source, "null source");
        this.target = (SourceTarget) requireNonNull(target, "null target");
    }
    
    @Override
    public Edge with(Attr.E... attributes) {
        addAll(attributes);
        return this;
    }
    
    String script(GraphImpl graph) {
        return "%s %s %s%s".formatted(
            source.script(graph),
            graph.isDirected() ? "->" : "--",
            target.script(graph),
            super.script());
    }
}
