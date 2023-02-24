/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

import java.util.Locale;

import cfh.graph.attr.AnyAttribute;
import cfh.graph.attr.Color;
import cfh.graph.attr.CommentAttribute;
import cfh.graph.attr.FontName;
import cfh.graph.attr.FontSize;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
public class Dot {

    public static final Format JPEG = Format.JPEG;
    
    public static Graph graph(String name) {
        return new Graph(name);
    }
    
    public static Node node(String name) {
        return new Node(name);
    }
    
    public static Edge edge(Source source, Target target) {
        return new Edge(source, target);
    }

    public static Color color(String value) {
        return new Color(value);
    }
    
    public static Color color(int r, int g, int b) {
        return new Color("#%02x%02x%02x".formatted( 
            checkARGB(r, "r:"),
            checkARGB(g, "g:"),
            checkARGB(b, "b:") ));
    }
    
    public static Color color(int a, int r, int g, int b) {
        return new Color("#%02x%02x%02x%02x".formatted( 
            checkARGB(r, "r:"),
            checkARGB(g, "g:"),
            checkARGB(b, "b:"),
            checkARGB(a, "a:") ));
    }
    
    public static Color color(double h, double s, double v) {
        return new Color(String.format(Locale.ROOT, "%.3f %.3f %.3f", 
            checkHSV(h, "h:"),
            checkHSV(s, "s:"),
            checkHSV(v, "v:") ));
    }
    
    public static AnyAttribute attr(String name, Object value) {
        return new AnyAttribute(name, value);
    }
    
    public static CommentAttribute comment(String comment) {
        return new CommentAttribute(comment);
    }
    
    public static FontName font(String name) {
        return new FontName(name);
    }
    
    public static FontSize font(double size) {
        return new FontSize(size);
    }
    
    //----------------------------------------------------------------------------------------------
    
    public static String quote(String id) {
        return '"'  + id.replace("\"", "\\\"") + '"';
    }
    
    private static int checkARGB(int val, String message) {
        if (val < 0 || val > 255)
            throw new IllegalArgumentException(message + val);
        return val;
    }
    
    private static double checkHSV(double val, String message) {
        if (val < 0 || val > 1)
            throw new IllegalArgumentException(message + val);
        return val;
    }
}
