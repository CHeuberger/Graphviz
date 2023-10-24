/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static java.util.stream.Collectors.*;

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
final class XDotImpl implements XDot {
    
    private final List<String> components; // TODO change to List<Component>?

    XDotImpl(String... components) {
        this.components = Collections.unmodifiableList( new ArrayList<>( Arrays.asList(components) ) );
    }
    
    @Override
    public String toString() {
        return components.stream().collect(joining(" ", "\"", "\""));
    }
}
