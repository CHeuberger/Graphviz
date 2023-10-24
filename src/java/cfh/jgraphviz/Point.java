/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static cfh.jgraphviz.Dot.*;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Carlos F. Heuberger, 2023-10-18
 *
 */
public sealed interface Point {
    //
}

final class PointImpl implements Point, Valuable {
    
    private final boolean add;
    private final double[] coords;
    
    PointImpl(boolean add, double... coords) {
        if (coords.length < 1)
            throw new IllegalArgumentException("need at least one double value");
        this.add = add;
        this.coords = coords;
    }
    
    @Override
    public String value() {
        return quote( (add ? "+" : "") + Arrays.stream(coords).mapToObj(Double::toString).collect(Collectors.joining(",")) );
    }
}
