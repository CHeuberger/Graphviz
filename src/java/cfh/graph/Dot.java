/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

import cfh.graph.attr.Color;

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
    
    //----------------------------------------------------------------------------------------------
    
    public static String quote(String id) {
        return '"'  + id.replace("\"", "\\\"") + '"';
    }
}
