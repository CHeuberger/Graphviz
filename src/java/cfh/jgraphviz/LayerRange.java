/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static java.util.Objects.*;

import static cfh.jgraphviz.Dot.*;

/**
 * @author Carlos F. Heuberger, 2023-10-23
 *
 */
public sealed interface LayerRange {
    public static final String ALL = "all";
    public LayerRange include(String layer);
    public LayerRange include(int layer);
    public LayerRange include(String first, String last);
    public LayerRange include(int first, int last);
}

/**
 * @author Carlos F. Heuberger, 2023-10-23
 *
 */
final class LayerRangeImpl implements LayerRange, Valuable {
    
    private final String listSep;
    private final String rangeSep;
    private final StringBuilder ranges = new StringBuilder(); 
    
    LayerRangeImpl(String listSep, String rangeSep) {
        this.listSep = Character.toString(listSep.codePointAt(0));
        this.rangeSep = Character.toString(rangeSep.codePointAt(0));
    }

    LayerRangeImpl(String listSep, String rangeSep, String layer) {
        this(listSep, rangeSep);
        include(layer);
    }
    
    LayerRangeImpl(String listSep, String rangeSep, int layer) {
        this(listSep, rangeSep, Integer.toString(layer));
    }
    
    LayerRangeImpl(String listSep, String rangeSep, String first, String last) {
        this(listSep, rangeSep);
        include(first, last);
    }
    
    LayerRangeImpl(String listSep, String rangeSep, int first, int last) {
        this(listSep, rangeSep, Integer.toString(first), Integer.toString(last));
    }
    
    @Override
    public LayerRange include(String layer) {
        if (!ranges.isEmpty()) {
            ranges.append(listSep);
        }
        ranges.append(requireNonNull(layer, "null layer"));
        return this;
    }
    
    @Override
    public LayerRange include(int layer) {
        return include(Integer.toString(layer));
    }
    
    @Override
    public LayerRange include(String first, String last) {
        if (!ranges.isEmpty()) {
            ranges.append(listSep);
        }
        ranges
        .append(requireNonNull(first, "null first"))
        .append(this.rangeSep)
        .append(requireNonNull(last, "null last"));
        return this;
    }
    
    @Override
    public LayerRange include(int first, int last) {
        return include(Integer.toString(first), Integer.toString(last));
    }
        
    @Override
    public String value() {
        return quote( ranges.toString() );
    }
}
