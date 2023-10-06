/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static cfh.jgraphviz.Dot.*;
import static java.util.Objects.*;

/**
 * @author Carlos F. Heuberger, 2023-03-04
 *
 */
sealed abstract class Attribute {

    final String name;
    final Object value;
    
    protected Attribute(String name, Object value) {
        this.name = requireNonNull(name, "null name");
        if (value instanceof String) {
            this.value = quote((String) value);
        } else {
            this.value = requireNonNull(value, "null value");
        }
    }
    
    String script() {
        return quote(name) + "=" + value;
    }
}

final class GAttribute extends Attribute implements GraphAttr {
    GAttribute(String name, Object value) {
        super(name, value);
    }
}

final class GNECAttribute extends Attribute implements GraphAttr, NodeAttr, EdgeAttr, ClusterAttr {
    GNECAttribute(String name, Object value) {
        super(name, value);
    }
}
