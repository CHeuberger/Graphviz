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
 * @author Carlos F. Heuberger, 2023-10-06
 *
 */
public sealed interface ArrowType {
    //
}

/**
 * @author Carlos F. Heuberger, 2023-10-06
 *
 */
final class ArrowTypeImpl implements ArrowType {

    private final List<String> shapes = new ArrayList<>();
    
    ArrowTypeImpl(String... shapes) {
        this.shapes.addAll(Arrays.asList(shapes));
    }
    
    @Override
    public String toString() {
        return shapes.stream().collect(joining("", "\"", "\""));
    }
}
