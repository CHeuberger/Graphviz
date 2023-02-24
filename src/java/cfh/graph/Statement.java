/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

import cfh.graph.attr.Attr;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
public sealed interface Statement<T extends Attr> 
permits Node, Edge, AttrStatement {
// TODO add more statements (attribute, ID=ID, subgraph)
    public String format(Graph graph);
    
    public Statement<T> with(String name, Object value);
    
    public Statement<T> with(T attr);
}
