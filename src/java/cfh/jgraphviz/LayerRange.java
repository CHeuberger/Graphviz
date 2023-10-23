/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import static cfh.jgraphviz.Dot.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Carlos F. Heuberger, 2023-10-23
 *
 */
public sealed interface LayerRange {
    public LayerRange include(String layer);
    public LayerRange include(String first, String last);
}

/**
 * @author Carlos F. Heuberger, 2023-10-23
 *
 */
final class LayerRangeImpl implements LayerRange {
    
    private final List<RangeImpl> ranges; 
    
    LayerRangeImpl() {
        this.ranges = new ArrayList<>();
    }
    
    LayerRangeImpl(String layer) {
        this.ranges = new ArrayList<>();
        ranges.add(new RangeImpl(layer));
    }
    
    LayerRangeImpl(String first, String last) {
        this.ranges = new ArrayList<>();
        ranges.add(new RangeImpl(first, last));
    }
    
    @Override
    public LayerRange include(String layer) {
        ranges.add(new RangeImpl(layer));
        return this;
    }
    
    @Override
    public LayerRange include(String first, String last) {
        ranges.add(new RangeImpl(first, last));
        return this;
    }
    
    @Override
    public String toString() {
        return quote(ranges.stream().map(RangeImpl::toString).collect(joining(",")));
    }
    
    private class RangeImpl {
        private final String first;
        private final String last;
        
        private RangeImpl(String layer) {
            this.first = requireNonNull(layer, "null layer");
            this.last = null;
        }
        private RangeImpl(String first, String last) {
            this.first = requireNonNull(first, "null first");
            this.last = requireNonNull(last, "null first");
        }
        
        @Override
        public String toString() {
            return last == null ? first : first + ":" + last;
        }
    }
}