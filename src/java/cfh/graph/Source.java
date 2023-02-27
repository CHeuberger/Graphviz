/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
interface Source {

    public String name();
    
    public Edge to(Target target);
}
