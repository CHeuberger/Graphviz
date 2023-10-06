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
 * @author Carlos F. Heuberger, 2023-09-27
 *
 */
abstract class AttributeHolder {

    private final List<Attribute> attributes = new ArrayList<>();
    
    protected AttributeHolder() {
    }
    
    protected void addAll(Attr... attrs) {
        Arrays.stream(attrs).map(a -> (Attribute) a).forEach(attributes::add);
    }

    protected String script() {
        return attributes.isEmpty() 
            ? ""
            : attributes.stream().map(Attribute::script).collect(joining(",", " [","]"));
    }
}
