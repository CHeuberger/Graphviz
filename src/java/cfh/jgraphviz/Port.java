/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static java.util.Objects.*;

import static cfh.jgraphviz.Dot.*;

/**
 * @author Carlos F. Heuberger, 2023-10-19
 *
 */
public sealed interface Port {
    //
}

final class PortImpl implements Port {

    private final String name;
    private final Compass compass;
    
    PortImpl(String name, Compass compass) {
        this.name = requireNonNull(name, "null name");
        this.compass = compass;
    }
    
    @Override
    public String toString() {
        if (compass == null)
            return name;
        else
            return quote(name + ":" + compass);
    }
}
