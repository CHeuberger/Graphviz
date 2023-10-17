/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static java.util.stream.Collectors.*;

import java.util.ArrayList;
import java.util.Arrays;
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
    
    private final List<String> components = new ArrayList<>(); // TODO change to List<Component>

    XDotImpl() {
    }
    
    XDotImpl(String... components) {
        this.components.addAll(Arrays.asList(components));
    }
    
    @Override
    public String toString() {
        return components.stream().collect(joining(" ", "\"", "\""));
    }
}
