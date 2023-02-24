/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cfh.graph.attr.Attr;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 * @param <T> 
 *
 */
public class AttrList<T extends Attr> {

    private final List<T> attributes = new ArrayList<>();

    public boolean add(T attr) {
        return attributes.add(attr);
    }

    public boolean addAll(Collection<? extends T> attrs) {
        return attributes.addAll(attrs);
    }
    
    public void forEach(Consumer<? super T> action) {
        attributes.forEach(action);
    }

    public Stream<T> stream() {
        return attributes.stream();
    }

    public String format() {
        return attributes.isEmpty()
            ? ""
            : attributes.stream().map(Attr::format).collect(Collectors.joining(";", "[", "]"));
    }
}
