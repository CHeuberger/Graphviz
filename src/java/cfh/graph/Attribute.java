/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

import static java.util.Objects.*;
import static cfh.graph.Dot.*;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
sealed abstract class Attribute implements Attr {

    final String name;
    final Object value;
    
    protected Attribute(String name, Object value) {
        this.name = requireNonNull(name, "null name");
        this.value = requireNonNull(value, "null value");
    }

    protected String format() {
        return "%s=%s".formatted(name, quote(value.toString()));
    }
    
    //==============================================================================================
    
    final static class AnyAttribute extends Attribute implements GraphAttr, NodeAttr, EdgeAttr, ClusterAttr {

        public AnyAttribute(String name, Object value) {
            super(name, value);
        }
    }

    //----------------------------------------------------------------------------------------------
    
    final static class GraphAttribute extends Attribute implements GraphAttr {

        public GraphAttribute(String name, Object value) {
            super(name, value);
        }
    }
    
    //----------------------------------------------------------------------------------------------
    
    final static class NodeAttribute extends Attribute implements NodeAttr {

        public NodeAttribute(String name, Object value) {
            super(name, value);
        }
    }
    
    //----------------------------------------------------------------------------------------------
    
    final static class EdgeAttribute extends Attribute implements EdgeAttr {

        public EdgeAttribute(String name, Object value) {
            super(name, value);
        }
    }
    
    //----------------------------------------------------------------------------------------------
    
    final static class CommentAttribute extends Attribute implements GraphAttr, NodeAttr, EdgeAttr {

        public CommentAttribute(String value) {
            super("comment", value);
        }
    }
    
    //----------------------------------------------------------------------------------------------
    
    sealed abstract static class ColorAttribute<T extends ColorAttribute<T>> extends Attribute
    permits Color, BackgroundColor, FillColor, FontColor {

        protected ColorAttribute(String name, String value) {
            super(name, value);
        }
        
        protected abstract T clone(String newValue); 
        
        public T gradient(String to) {
            return clone(value + ":" + to);
        }
        
        public T and(double at, String to) {
            return clone(value + ";" + at + ":" + to);
        }
        
        public T gradient(Color to) {
            return gradient((String) to.value);
        }
        
        public T and(double at, Color to) {
            return and(at, (String) to.value);
        }
    }
    
    //----------------------------------------------------------------------------------------------
    
    final static class BackgroundColor extends ColorAttribute<BackgroundColor> implements GraphAttr, ClusterAttr {

        protected BackgroundColor(String value) {
            super("bgcolor", value);
        }
        
        @Override
        protected BackgroundColor clone(String newValue) {
            return new BackgroundColor(newValue);
        }
    }
    
    //----------------------------------------------------------------------------------------------
    
    final static class FillColor extends ColorAttribute<FillColor> implements NodeAttr, EdgeAttr, ClusterAttr {

        protected FillColor(String value) {
            super("fillcolor", value);
        }
        
        @Override
        protected FillColor clone(String newValue) {
            return new FillColor(newValue);
        }
    }
    
    //----------------------------------------------------------------------------------------------
    
    final static class FontColor extends ColorAttribute<FontColor> implements GraphAttr, NodeAttr, EdgeAttr, ClusterAttr {

        protected FontColor(String value) {
            super("fontcolor", value);
        }
        
        @Override
        protected FontColor clone(String newValue) {
            return new FontColor(newValue);
        }
    }
    
    //----------------------------------------------------------------------------------------------
    
    final static class FontName extends Attribute implements GraphAttr, NodeAttr, EdgeAttr, ClusterAttr {

        public FontName(String value) {
            super("fontname", value);
        }
    }
    
    //----------------------------------------------------------------------------------------------
    
    final static class FontSize extends Attribute implements GraphAttr, NodeAttr, EdgeAttr, ClusterAttr {

        public FontSize(double value) {
            super("fontsize", value);
        }
    }
}
