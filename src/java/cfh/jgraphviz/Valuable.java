/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static cfh.jgraphviz.Dot.*;
/**
 * @author Carlos F. Heuberger, 2023-10-24
 *
 */
sealed interface Valuable 
permits XDotImpl, ArrowTypeImpl, ColorImpl, ColorListImpl, PointImpl, HTMLImpl, PortImpl, LayerRangeImpl, 
        Engine, DirType, FixedSize, FontNames, Compass, 
        ImagePos, ImageScale, LabelScheme, LabelJust, LabelLoc, Mode, Model {
    
    public String value();
    
}
