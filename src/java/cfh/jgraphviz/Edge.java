/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static cfh.jgraphviz.Dot.*;
import static java.util.Objects.*;

/**
 * @author Carlos F. Heuberger, 2023-03-03
 *
 */
public interface Edge {

    public Edge with(EdgeAttr first, EdgeAttr... attributes);
}

/**
 * @author Carlos F. Heuberger, 2023-03-06
 *
 */
class EdgeImpl implements Edge {

    final Source source;
    final Target target;
    
    EdgeImpl(Source source, Target target) {
        this.source = requireNonNull(source, "null source");
        this.target = requireNonNull(target, "null target");
    }
    
    @Override
    public Edge with(EdgeAttr first, EdgeAttr... attributes) {
        // TODO Auto-generated method stub
        return null;
    }
}
