/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

/**
 * @author Carlos F. Heuberger, 2023-03-03
 *
 */
public interface NodeId extends Node, Source, Target {

    /** Adds attributes to this node. */
    public Node with(NodeAttr... attributes);
}
