/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static cfh.jgraphviz.Dot.INDENT;
import static cfh.jgraphviz.Dot.quote;
import static java.util.Objects.requireNonNull;

import java.util.Formatter;

/**
 * @author Carlos F. Heuberger, 2023-03-03
 *
 */
public interface Subgraph extends StatementList<Subgraph>, Source, Target {

    public Graph with(ClusterAttr... attributes);
}

/**
 * @author Carlos F. Heuberger, 2023-03-06
 *
 */
class SubgraphImpl extends StatementListImpl<Subgraph> implements Subgraph, SourceTarget {

    private final String id;

    SubgraphImpl() {
        id = null;
    }
    
    SubgraphImpl(String id) {
        this.id = requireNonNull(id, "null id");
    }

    @Override
    public Edge to(Target target) {
        return new EdgeImpl(this, target);
    }

    @Override
    public Edge from(Source source) {
        return new EdgeImpl(source, this);
    }
    
    @Override
    public Graph with(ClusterAttr... attributes) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public String script(GraphImpl graph) {
        var formatter = new Formatter();
        try (formatter) {
            if (id != null) {
                formatter.format("subgraph %s ", quote(id));
            }
            formatter.format("""
                {
                %s
                }
                """, 
                scriptStatements(graph).indent(INDENT).stripTrailing());  // remove trailing linefeed

            return formatter.toString().stripTrailing();
        }
    }
}