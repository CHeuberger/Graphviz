/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
public interface Source {

    public String format(int indent, Graph graph);
    
    public Edge to(Target target);
}
