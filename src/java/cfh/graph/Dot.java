/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

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
    
    public static Link link(Node source, Node target) {
        return new Link(source, target);
    }
    
    //----------------------------------------------------------------------------------------------
    
    public static String quote(String id) {
        return '"'  + id.replace("\"", "\\\"") + '"';
    }
}
