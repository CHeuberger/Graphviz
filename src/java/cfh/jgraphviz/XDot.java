/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Carlos F. Heuberger, 2023-10-05
 *
 */
public sealed interface XDot {

}

final class XDotImpl implements XDot {
    private final List<String> components = new ArrayList<>();

    XDotImpl() {
    }
    
    XDotImpl(String... components) {
        this.components.addAll(Arrays.asList(components));
    }
}
