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
        Engine, CharSet, DirType, FixedSize, FontNames, Compass, Ordering, OutputOrder, 
        Overlap, ImagePos, ImageScale, LabelScheme, LabelJust, LabelLoc, Mode, Model {
    
    public default String attribute() { throw new IllegalArgumentException("invalid attribute: " + this); }
    public String value();
    
}
