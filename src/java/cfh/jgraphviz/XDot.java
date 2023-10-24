/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Carlos F. Heuberger, 2023-10-05
 *
 */
public sealed interface XDot {
    //
}

/**
 * @author Carlos F. Heuberger, 2023-10-05
 *
 */
final class XDotImpl implements XDot, Valuable {
    
    private final List<String> components; // TODO change to List<Component>?

    XDotImpl(String... components) {
        this.components = Collections.unmodifiableList( new ArrayList<>( Arrays.asList(components) ) );
    }
    
    @Override
    public String value() {
        return '"' + String.join(" ", components) + '"';
    }
}
