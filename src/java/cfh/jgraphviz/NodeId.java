/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static cfh.jgraphviz.Dot.*;
import static java.util.Objects.*;

import cfh.jgraphviz.StatementListImpl.Statement;

/**
 * @author Carlos F. Heuberger, 2023-03-03
 *
 */
public interface NodeId extends Node, Source, Target {

    public Node with(NodeAttr first, NodeAttr... attributes);
}
