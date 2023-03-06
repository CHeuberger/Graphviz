/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graphviz;

import static cfh.graphviz.Dot.*;
import static java.util.Objects.*;

import cfh.graphviz.StatementListImpl.Statement;

/**
 * @author Carlos F. Heuberger, 2023-03-03
 *
 */
public interface NodeId extends Node, Source, Target {

    public Node with(NodeAttr first, NodeAttr... attributes);
}
