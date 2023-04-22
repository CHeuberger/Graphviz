/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static cfh.jgraphviz.Dot.*;
import static java.util.Objects.*;

import cfh.jgraphviz.StatementListImpl.Statement;

/**
 * @author Carlos F. Heuberger, 2023-03-04
 *
 */
sealed class Attribute {

    final String name;
    final Object value;
    
    protected Attribute(String name, Object value) {
        this.name = requireNonNull(name, "null name");
        this.value = requireNonNull(value, "null value");
    }
}

final class LabelAttr extends Attribute implements GraphAttr, NodeAttr, EdgeAttr {
    
    LabelAttr(String label) {
        super("label", label);
    }
}