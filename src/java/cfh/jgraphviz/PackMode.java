/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static java.util.Objects.*;

/**
 * @author Carlos F. Heuberger, 2023-11-02
 *
 */
public non-sealed interface PackMode extends Attr.G {

    public enum Align {
        TOP, LEFT, RIGHT, BOTTOM
    }
    
    /** Pack at the node and edge level, with no overlapping of these objects. */
    public static final PackMode NODE = new PackModeImpl("node");
    
    /** Top-level clusters are kept intact. */
    public static final PackMode CLUST = new PackModeImpl("clust");
    
    /** Pack using the bounding box of the component. */
    public static final PackMode GRAPH = new PackModeImpl("graph");
    
    /** Pack at the graph level into an array of graphs in row-major order, with the number of columns roughly the square root of the number of components. */
    public static final PackMode ARRAY = new PackModeImpl("array");
    
    /** Pack at the graph level into an array of graphs in column-major order, with the number of columns roughly the square root of the number of components. */
    public static final PackMode ARRAY_C = new PackModeImpl("array_c");
    
    public PackMode count(int count);
    
    public PackMode align(Align align);
}

/**
 * @author Carlos F. Heuberger, 2023-11-02
 *
 */
final class PackModeImpl implements PackMode, Valuable {

    private static final int NONE = -1;
    
    private final String value;
    private final int count;
    private final String align;
    
    PackModeImpl(String value) {
        this.value = requireNonNull(value, "null value");
        this.count = NONE;
        this.align = "";
    }
    
    private PackModeImpl(String value, int count, String align) {
        if (count != NONE && count <= 0)
            throw new IllegalArgumentException("count value must be positive: " + count);
        this.value = requireNonNull(value, "null value");
        this.count = count;
        this.align = requireNonNull(align, "null align");
    }
    
    @Override
    public final String attribute() { 
        return "packmode"; 
    }
    
    @Override
    public String value() {
        var v = value;
        if (!align.isEmpty() && !v.contains("_")) {
            v += "_";
        }
        v += align;
        if (count == NONE) {
            return v;
        } else {
            return v + count;
        }
    }
    
    @Override
    public PackMode count(int count) {
        if (this.count != NONE && count != this.count)
            throw new IllegalArgumentException("changing count from: " + this.count + " to: " + count + " not allowed");
        return new PackModeImpl(value, count, align);
    }
    
    @Override
    public PackMode align(Align align) {
        return new PackModeImpl(value, count, 
            this.align + switch (align) { case TOP -> "t"; case BOTTOM -> "b"; case LEFT -> "l"; case RIGHT -> "r"; default -> ""; } 
            );
    }
}