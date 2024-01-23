/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static cfh.jgraphviz.Dot.*;
import static cfh.jgraphviz.Attr.*;
import static java.util.Objects.*;

/**
 * @author Carlos F. Heuberger, 2023-03-04
 *
 */
non-sealed interface Attribute extends GN, GS, NE, SN, SNE, GNE, GSNE, GSN {
    public String script();
}

final class AttributeImpl implements Attribute  {

    final String name;
    final String value;
    
    AttributeImpl(String name, CharSequence value) {
        this.name = requireNonNull(name, "null name");
        this.value = quote(value.toString());
    }
    
    // TODO needed?
    AttributeImpl(String name, CharSequence value, boolean quote) {
        this.name = requireNonNull(name, "null name");
        this.value = quote ? quote(value.toString()) : value.toString();
    }
    
    AttributeImpl(String name, Valuable v) {
        if (v instanceof Attr)
            assert name.equals(v.attribute()) : "name does not match \"" + name + "\" != \"" + v.attribute() + "\"";
        this.name = requireNonNull(name, "null name");
        this.value = v.value();
    }
    
    AttributeImpl(String name, Object value) {
        if (value instanceof Valuable v) {
            this.name = requireNonNull(name, "null name");
            this.value = v.value();
        } else {
            throw new IllegalArgumentException("invalid attribute type " + value.getClass() + " (" + value + ")");
        }
    }
    
    @Override
    public String script() {
        return quote(name) + "=" + value;
    }
}
