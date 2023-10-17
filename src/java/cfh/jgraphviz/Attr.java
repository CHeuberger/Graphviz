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
    public sealed interface N extends Attr { }
    public sealed interface E extends Attr { }
    public sealed interface S extends Attr { }
//    public sealed interface GN extends G, N permits Attribute { }
//    public sealed interface GE extends G, E permits Attribute { }
    public sealed interface GS extends G, S permits Attribute { }
//    public sealed interface NE extends N, E { }
    public sealed interface NS extends N, S permits Attribute { }
//    public sealed interface EC extends E, S permits Attribute { }
    public sealed interface GNE extends G, N, E permits Attribute { }
//    public sealed interface GNS extends G, N, S permits Attribute { }
//    public sealed interface GES extends G, E, S permits Attribute { }
    public sealed interface NES extends N, E, S permits Attribute { }
    public sealed interface GNES extends G, N, E, S permits Attribute { }
}
