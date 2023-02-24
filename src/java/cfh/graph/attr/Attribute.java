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
public sealed abstract class Attribute implements Attr 
permits GraphAttribute, NodeAttribute, EdgeAttribute, Color {
    // TODO area, arrowhead, arrowsize

    final String name;
    final Object value;
    
    protected Attribute(String name, Object value) {
        this.name = requireNonNull(name, "null name");
        this.value = requireNonNull(value, "null value");
    }

    @Override
    public String format() {
        return "%s=%s".formatted(name, quote(value.toString()));
    }
}
