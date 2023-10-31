/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static cfh.jgraphviz.Dot.*;

/**
 * @author Carlos F. Heuberger, 2023-03-04
 *
 */
sealed interface Attr {

    public sealed interface G extends Attr permits GN, GS, GNE, GSN, GSNE, Engine, CharSet, FontNames, LabelScheme, Mode, Model, OutputOrder, Overlap { }
    public sealed interface S extends Attr { }
    public sealed interface N extends Attr permits GN, SN, GNE, GSN, SNE, GSNE, FixedSize, ImagePos, ImageScale { }
    public sealed interface E extends Attr permits GNE, SNE, GSNE, DirType { }
    public sealed interface GN extends G, N permits Attribute { }
//    public sealed interface GE extends G, E permits Attribute { }
    public sealed interface GS extends G, S permits Attribute, LabelJust { }
//    public sealed interface NE extends N, E { }
    public sealed interface SN extends S, N permits Attribute { }
//    public sealed interface SE extends S, E permits Attribute { }
    public sealed interface GNE extends G, N, E permits Attribute { }
    public sealed interface GSN extends G, S, N permits Attribute, LabelLoc, Ordering { }
//    public sealed interface GSE extends G, S, E permits Attribute { }
    public sealed interface SNE extends S, N, E permits Attribute, ColorList { }
    public sealed interface GSNE extends G, S, N, E permits Attribute { }
}
