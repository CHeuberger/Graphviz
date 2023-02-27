/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 * @param <T> 
 *
 */
class AttrList<T extends Attr> {

    private final List<T> attributes = new ArrayList<>();

    boolean add(T attr) {
        return attributes.add(attr);
    }

    boolean addAll(Collection<? extends T> attrs) {
        return attributes.addAll(attrs);
    }
    
    void forEach(Consumer<? super T> action) {
        attributes.forEach(action);
    }

    Stream<T> stream() {
        return attributes.stream();
    }

    String format() {
        return attributes.isEmpty()
            ? ""
            : attributes.stream().map(Attr::format).collect(Collectors.joining(",", "[", "]"));
    }
}
