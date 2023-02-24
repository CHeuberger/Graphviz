/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph.attr;

import static java.util.Objects.*;
import static cfh.graph.Dot.*;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
public sealed class Attribute implements Attr 
permits NodeAttribute, LinkAttribute {

    final String name;
    final String value;
    
    protected Attribute(String name, String value) {
        this.name = requireNonNull(name, "null name");
        this.value = requireNonNull(value, "null value");
    }

    @Override
    public String format() {
        return "%s=%s".formatted(name, quote(value));
    }
}
