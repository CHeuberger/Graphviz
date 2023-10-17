/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static cfh.jgraphviz.Dot.*;
import static cfh.jgraphviz.Attr.*;
import static java.util.Objects.*;

import cfh.jgraphviz.Attr.GNE;
import cfh.jgraphviz.Attr.NES;

/**
 * @author Carlos F. Heuberger, 2023-03-04
 *
 */
final class Attribute implements GS, NS, GNES, NES, GNE  {

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
