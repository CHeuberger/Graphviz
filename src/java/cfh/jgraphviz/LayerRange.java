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
final class LayerRangeImpl implements LayerRange {
    
    private final String listSep;
    private final String rangeSep;
    private final StringBuilder ranges = new StringBuilder(); 
    
    LayerRangeImpl(String listSep, String rangeSep) {
        this.listSep = Character.toString(listSep.codePointAt(0));
        this.rangeSep = Character.toString(rangeSep.codePointAt(0));
    }

    LayerRangeImpl(String listSep, String rangeSep, String layer) {
        this(listSep, rangeSep);
        ranges.append(layer);
    }
    
    LayerRangeImpl(String listSep, String rangeSep, int layer) {
        this(listSep, rangeSep, Integer.toString(layer));
    }
    
    LayerRangeImpl(String listSep, String rangeSep, String first, String last) {
        this(listSep, rangeSep);
        ranges.append(first).append(this.rangeSep).append(last);
    }
    
    LayerRangeImpl(String listSep, String rangeSep, int first, int last) {
        this(listSep, rangeSep, Integer.toString(first), Integer.toString(last));
    }
    
    @Override
    public LayerRange include(String layer) {
        if (!ranges.isEmpty()) {
            ranges.append(listSep);
        }
        ranges.append(layer);
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
        ranges.append(first).append(rangeSep).append(last);
        return this;
    }
    
    @Override
    public LayerRange include(int first, int last) {
        return include(Integer.toString(first), Integer.toString(last));
    }
        
    @Override
    public String toString() {
        return ranges.toString();
    }
}