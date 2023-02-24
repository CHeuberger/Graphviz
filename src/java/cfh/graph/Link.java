/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

import static cfh.graph.Dot.*;
import static java.util.Objects.*;

import cfh.graph.attr.LinkAttr;
import cfh.graph.attr.LinkAttribute;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
public final class Link implements Statement {

    final Source source;
    final Target target;
    
    private final AttrList<LinkAttr> attrList = new AttrList<>();
    
    Link(Source source, Target target) {
        this.source = requireNonNull(source, "null source");
        this.target = requireNonNull(target, "null target");
    }
    
    @Override
    public Statement with(String name, Object value) {
        attrList.add(new LinkAttribute(name, value.toString()));
        return this;
    }
    
    @Override
    public String format(Graph graph) {
        return "%s %s %s %s;".formatted(
            quote(source.name()),
            graph.directed() ? "->" : "--",
            quote(target.name()),
            attrList.format()
            );
    }
}
