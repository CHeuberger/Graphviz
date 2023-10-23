/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static java.util.Objects.*;

/**
 * @author Carlos F. Heuberger, 2023-10-20
 *
 */
public sealed interface HTML {
    //
}

/**
 * @author Carlos F. Heuberger, 2023-10-20
 *
 */
final class HTMLImpl implements HTML {

    private final String html;
    
    public HTMLImpl(String html) {
        this.html = requireNonNull(html, "null html");
    }

    @Override
    public String toString() {
        return "< " + html + " >";
    }
}
