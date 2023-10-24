/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

/**
 * @author Carlos F. Heuberger, 2023-03-04
 *
 */
sealed interface Attr {

    public sealed interface G extends Attr { }
    public sealed interface S extends Attr { }
    public sealed interface N extends Attr { }
    public sealed interface E extends Attr { }
    public sealed interface GN extends G, N permits Attribute { }
//    public sealed interface GE extends G, E permits Attribute { }
    public sealed interface GS extends G, S permits Attribute { }
//    public sealed interface NE extends N, E { }
    public sealed interface SN extends S, N permits Attribute { }
//    public sealed interface SE extends S, E permits Attribute { }
    public sealed interface GNE extends G, N, E permits Attribute { }
    public sealed interface GSN extends G, S, N permits Attribute { }
//    public sealed interface GSE extends G, S, E permits Attribute { }
    public sealed interface SNE extends S, N, E permits Attribute, ColorList { }
    public sealed interface GSNE extends G, S, N, E permits Attribute { }
}
