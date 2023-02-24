/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph.attr;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
public final class CommentAttribute extends Attribute implements GraphAttr, NodeAttr, EdgeAttr {

    public CommentAttribute(String value) {
        super("comment", value);
    }
}
