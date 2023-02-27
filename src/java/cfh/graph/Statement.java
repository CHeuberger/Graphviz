/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
public sealed interface Statement<T extends Attr> 
permits Node, Edge, AttrStatement, DefaultStatement<?> {
// TODO add more statements (attribute, ID=ID, subgraph)
    public String format(Graph graph);
    
    @SuppressWarnings("unchecked")
    public Statement<T> with(T... attr);
}
