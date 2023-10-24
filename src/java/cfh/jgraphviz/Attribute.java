/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static cfh.jgraphviz.Dot.*;
import static cfh.jgraphviz.Attr.*;
import static java.util.Objects.*;

import cfh.jgraphviz.Attr.GN;
import cfh.jgraphviz.Attr.GNE;
import cfh.jgraphviz.Attr.GSN;
import cfh.jgraphviz.Attr.SNE;

/**
 * @author Carlos F. Heuberger, 2023-03-04
 *
 */
non-sealed interface Attribute extends GN, GS, SN, SNE, GNE, GSNE, GSN {
    public String script();
}

final class AttributeImpl implements Attribute  {

    final String name;
    final Object value;
    
    protected AttributeImpl(String name, Object value) {
        this.name = requireNonNull(name, "null name");
        if (value instanceof String) {
            this.value = quote((String) value);
        } else {
            this.value = requireNonNull(value, "null value");
        }
    }
    
    @Override
    public String script() {
        return quote(name) + "=" + value;
    }
}
