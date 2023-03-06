/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graphviz;

import static java.util.Objects.*;
import static cfh.graphviz.Dot.*;

/**
 * @author Carlos F. Heuberger, 2023-03-04
 *
 */
public class Attribute {

    final String name;
    final Object value;
    
    protected Attribute(String name, Object value) {
        this.name = requireNonNull(name, "null name");
        this.value = requireNonNull(value, "null value");
    }
}
