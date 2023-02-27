/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
interface Attr {

    public String format();
    
    //----------------------------------------------------------------------------------------------
    
    interface ClusterAttr extends Attr { /* */ }
    
    interface EdgeAttr extends Attr { /* */ }

    interface GraphAttr extends Attr { /* */ }
    
    interface NodeAttr extends Attr { /* */ }
}
