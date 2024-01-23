/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static java.util.Objects.*;
import static java.util.stream.Collectors.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Locale;
import java.util.OptionalInt;

import javax.imageio.ImageIO;

/**
 * @author Carlos F. Heuberger, 2023-03-03
 *
 */
public class Dot {
    
    static final int INDENT = 2;
    
    /** Java Property ><code>-Dcfh.jgraphviz.path=...</code>with path to GraphViz binary diretory. */
    private static final String PATH_PROPERTY = "cfh.jgraphviz.path";
    
    /** Environment variable poiting to GraphViz home, used if <code>cfh.jgraphviz.path</code> is not set. */
    private static final String PATH_ENVIRONMENT = "GRAPHVIZ_HOME";
    
    //----------------------------------------------------------------------------------------------
    
    /** Replaced by the name of the graph, valid for:<ul>
     * <li><code>edgehref</code></li>
     * <li><code>edgetarget</code></li>
     * <li><code>edgetooltip</code></li>
     * <li><code>edgeURL</code></li>
     * <li><code>headhref</code></li>
     * <li><code>headtarget</code></li>
     * <li><code>headtooltip</code></li>
     * <li><code>headURL</code></li>
     * <li><code>href</code></li>
     * <li><code>id</code></li>
     * <li><code>labelhref</code></li>
     * <li><code>labeltarget</code></li>
     * <li><code>labeltooltip</code></li>
     * <li><code>labelURL</code></li>
     * <li><code>tailhref</code></li>
     * <li><code>tailtarget</code></li>
     * <li><code>tailtooltip</code></li>
     * <li><code>tailURL</code></li>
     * <li><code>target</code></li>
     * <li><code>tooltip</code></li>
     * <li><code>URL</code</li>
     * </ul>
     */
    public static final String GRAPH_NAME = "\\G";
    
    /** Replaced by the name of the node, valid for:<ul>
     * <li><code>edgehref</code></li>
     * <li><code>edgetarget</code></li>
     * <li><code>edgetooltip</code></li>
     * <li><code>edgeURL</code></li>
     * <li><code>headhref</code></li>
     * <li><code>headtarget</code></li>
     * <li><code>headtooltip</code></li>
     * <li><code>headURL</code></li>
     * <li><code>href</code></li>
     * <li><code>id</code></li>
     * <li><code>labelhref</code></li>
     * <li><code>labeltarget</code></li>
     * <li><code>labeltooltip</code></li>
     * <li><code>labelURL</code></li>
     * <li><code>tailhref</code></li>
     * <li><code>tailtarget</code></li>
     * <li><code>tailtooltip</code></li>
     * <li><code>tailURL</code></li>
     * <li><code>target</code></li>
     * <li><code>tooltip</code></li>
     * <li><code>URL</code</li>
     * </ul>
     */
    public static final String NODE_NAME = "\\N";
    
    /** Replaced by the name of the edge, valid for:<ul>
     * <li><code>edgehref</code></li>
     * <li><code>edgetarget</code></li>
     * <li><code>edgetooltip</code></li>
     * <li><code>edgeURL</code></li>
     * <li><code>headhref</code></li>
     * <li><code>headtarget</code></li>
     * <li><code>headtooltip</code></li>
     * <li><code>headURL</code></li>
     * <li><code>href</code></li>
     * <li><code>id</code></li>
     * <li><code>labelhref</code></li>
     * <li><code>labeltarget</code></li>
     * <li><code>labeltooltip</code></li>
     * <li><code>labelURL</code></li>
     * <li><code>tailhref</code></li>
     * <li><code>tailtarget</code></li>
     * <li><code>tailtooltip</code></li>
     * <li><code>tailURL</code></li>
     * <li><code>target</code></li>
     * <li><code>tooltip</code></li>
     * <li><code>URL</code</li>
     * </ul>
     */
    public static final String EDGE_NAME = "\\E";
    
    /** Replaced by the name of the edge head: the string formed from the name of the tail node, 
     * the edge operator (-- or ->) and the name of the head node, valid for:<ul>
     * <li><code>edgehref</code></li>
     * <li><code>edgetarget</code></li>
     * <li><code>edgetooltip</code></li>
     * <li><code>edgeURL</code></li>
     * <li><code>headhref</code></li>
     * <li><code>headtarget</code></li>
     * <li><code>headtooltip</code></li>
     * <li><code>headURL</code></li>
     * <li><code>href</code></li>
     * <li><code>id</code></li>
     * <li><code>labelhref</code></li>
     * <li><code>labeltarget</code></li>
     * <li><code>labeltooltip</code></li>
     * <li><code>labelURL</code></li>
     * <li><code>tailhref</code></li>
     * <li><code>tailtarget</code></li>
     * <li><code>tailtooltip</code></li>
     * <li><code>tailURL</code></li>
     * <li><code>target</code></li>
     * <li><code>tooltip</code></li>
     * <li><code>URL</code</li>
     * </ul>
     */
    public static final String HEAD_NAME = "\\H";
    
    /** Replaced by the name of the edge tail, valid for:<ul>
     * <li><code>edgehref</code></li>
     * <li><code>edgetarget</code></li>
     * <li><code>edgetooltip</code></li>
     * <li><code>edgeURL</code></li>
     * <li><code>headhref</code></li>
     * <li><code>headtarget</code></li>
     * <li><code>headtooltip</code></li>
     * <li><code>headURL</code></li>
     * <li><code>href</code></li>
     * <li><code>id</code></li>
     * <li><code>labelhref</code></li>
     * <li><code>labeltarget</code></li>
     * <li><code>labeltooltip</code></li>
     * <li><code>labelURL</code></li>
     * <li><code>tailhref</code></li>
     * <li><code>tailtarget</code></li>
     * <li><code>tailtooltip</code></li>
     * <li><code>tailURL</code></li>
     * <li><code>target</code></li>
     * <li><code>tooltip</code></li>
     * <li><code>URL</code</li>
     * </ul>
     */
    public static final String TAIL_NAME = "\\T";
    
    /** Replaced by the object's label, valid for:<ul>
     * <li><code>edgehref</code></li>
     * <li><code>edgetarget</code></li>
     * <li><code>edgetooltip</code></li>
     * <li><code>edgeURL</code></li>
     * <li><code>headhref</code></li>
     * <li><code>headtarget</code></li>
     * <li><code>headtooltip</code></li>
     * <li><code>headURL</code></li>
     * <li><code>href</code></li>
     * <li><code>id</code></li>
     * <li><code>labelhref</code></li>
     * <li><code>labeltarget</code></li>
     * <li><code>labeltooltip</code></li>
     * <li><code>labelURL</code></li>
     * <li><code>tailhref</code></li>
     * <li><code>tailtarget</code></li>
     * <li><code>tailtooltip</code></li>
     * <li><code>tailURL</code></li>
     * <li><code>target</code></li>
     * <li><code>tooltip</code></li>
     * <li><code>URL</code</li>
     * </ul>
     */
    public static final String LABEL_VALUE = "\\L";
    
    /** Actual text centered and start a new line, valid for:<ul>
     * <li><code>label</code></li>
     * <li><code>headlabel</code></li>
     * <li><code>taillabel</code></li>
     * </ul>
     */
    public static final String CENTER_LINE = "\\n";
    
    /** Actual text left-justified and start a new line, valid for:<ul>
     * <li><code>label</code></li>
     * <li><code>headlabel</code></li>
     * <li><code>taillabel</code></li>
     * </ul>
     */
    public static final String LEFT_LINE = "\\l";
    
    /** Actual text right-justified and start a new line, valid for:<ul>
     * <li><code>label</code></li>
     * <li><code>headlabel</code></li>
     * <li><code>taillabel</code></li>
     * <li><code>xlabel</code></li>
     * </ul>
     */
    public static final String RIGHT_LINE = "\\r";
    
    //----------------------------------------------------------------------------------------------
    
    /** Layout Engine. */
    public enum Engine implements Valuable, Attr.G {
        /** <a href="https://en.wikipedia.org/wiki/Layered_graph_drawing">Hierarchical or layered drawings</a>. */
        DOT,
        /** "Spring Model" layout. */
        NEATO, 
        /** <a href="https://en.wikipedia.org/wiki/Force-directed_graph_drawing">Force-Directed Placement</a>. */
        FDP, 
        /** Scalable <a href="https://en.wikipedia.org/wiki/Force-directed_graph_drawing">Force-Directed Placement</a>. */
        SFDP,
        /** <a href="https://en.wikipedia.org/wiki/Circular_layout">Circular Layout</a>. */
        CIRCO,
        /** Radial layout. */
        TWOPI,
        /** Pretty-print DOT graph file. */
        NOP,
        /** Pretty-print DOT graph file, assuming positions already known. */
        NOP2,
        /** Clustered graphs. */
        OSAGE,
        /** Map of clustered graph using a <a href="https://en.wikipedia.org/wiki/Treemapping">Squarified Treemap Layout</a>. */
        PATCHWORK,
        ;
        private final String value = name().toLowerCase();
        @Override public String attribute() { return "layout"; }
        @Override public String value() { return value; }
    }

    //----------------------------------------------------------------------------------------------
    
    /* Format shortcuts. */
    public static final Format JPG = Format.JPG;
    public static final Format GIF = Format.GIF;
    public static final Format PNG = Format.PNG;
    public static final Format SVG = Format.SVG;
    
    /** Output formats. */
    public enum Format {
        // TODO enable all formats
        BMP(FormatType.IMAGE),
        // CGImage,
        // CANON, DOT, XDOT, XDOT1$2, XDOT1$4
        xdot(FormatType.IMAGE),
        EPS(FormatType.IMAGE),
        // EXR,
        // FIG,
        // GD, GD2,
        GIF(FormatType.IMAGE),
        // GTK,
        ICO(FormatType.IMAGE),
        // IMAP, IMAP_NP, ISMAP, CMAP, CMAPX, CMAPX_NP,
        JPG(FormatType.IMAGE), 
        JPEG(FormatType.IMAGE),
        // JP2,
        // JSON, JSON0, DOT_JSON, XDOT_JSON,
        // PDF,
        // PIC,
        // PCT, PICT,
        // PLAIN, PLAIN-EXT,
        PNG(FormatType.IMAGE),
        // POV,
        // PS,
        // PS2,
        // PSD,
        // SGI,
        SVG(FormatType.IMAGE), SVGZ(FormatType.IMAGE),
        // TGA,
        TIF(FormatType.IMAGE), TIFF(FormatType.IMAGE),
        // TK,
        // VML, VMLZ,
        // VRML,
        // WBMP,
        // WEBP,
        // XLIB, X11,
        ;
        final FormatType type;
        private Format(FormatType type) {
            this.type = requireNonNull(type, "null type");
        }
        String asParameter() { return name().toLowerCase().replace("$", "."); }
        FormatType type() { return type; }
    }
    
    private enum FormatType {
        IMAGE, TEXT;
    }
    
    //----------------------------------------------------------------------------------------------

    /** Character encoding used when interpreting string input as a text label. </P>
     *  Valid values: utf-8. iso-8859-1 (latin-1,) big-5 */
    public enum CharSet implements Valuable, Attr.G {
        UTF_8(),
        ISO_8859_1,
        LATIN_1,
        BIG_5;
        ;
        private final String value = name().toLowerCase().replace("_", "-");
        @Override public String attribute() { return "charset"; }
        @Override public String value() { return value; }
    }
    
    //----------------------------------------------------------------------------------------------

    /** Edge arrow direction type. */
    public enum DirType implements Valuable, Attr.E {
        /** Forward arrow type, draw head glyph only. */  
        FORWARD,
        /** Backward arrow type, draw tail glyph only. */  
        BACK,
        /** Both arrow type, draw head and tail glyph. */  
        BOTH,
        /** None arrow type, draw neither head nor tail glyph. */  
        NONE,
        ;
        private final String value = name().toLowerCase();
        @Override public String attribute() { return "dir"; }
        @Override public String value() { return value; }
    }
    
    //----------------------------------------------------------------------------------------------

    /** Fixed size type: whether to use the specified width and height attributes to choose node size. */
    public enum FixedSize implements Valuable, Attr.N {
        /** Size is specified by the values of the <code>width</code> and <code>height</code> attributes only and is not expanded to contain the text label. */
        TRUE,
        /** Size of a node is determined by smallest width and height needed to contain its label and image. */
        FALSE,
        /** The <code>width</code> and <code>height</code> attributes also determine the size of the node shape, but the label can be much larger. */
        SHAPE,
        ;
        private final String value = name().toLowerCase();
        @Override public String attribute() { return "fixedsize"; }
        @Override public String value() { return value; }
    }
    
    //----------------------------------------------------------------------------------------------
    
    /** How basic fontnames are represented in SVG output. */
    public enum FontNames implements Valuable, Attr.G {
        /** Try to use known SVG fontnames. */
        SVG,
        /** Use known PostScript font names. */
        PS,
        /** The fontconfig font conventions are used. */
        HD,
        ;
        private final String value = name().toLowerCase();
        @Override public String attribute() { return "fontnames"; }
        @Override public String value() { return value; }
    }
    
    //----------------------------------------------------------------------------------------------

    /** Compass point used for Port Positions. */
    public enum Compass implements Valuable {
        N, NE, E, SE, S, SW, W, NW, 
        /** Center of node or port. */
        CENTER("c"),
        /** Appropriate side of the port adjacent to the exterior of the node should be used, if such exists. Otherwise, the center is used. */
        DEFAULT("_"),
        ;
        private Compass() { this.value = name().toLowerCase(); }
        private Compass(String value) { this.value = value; }
        private final String value;
        @Override public String value() { return value; }
    }
    
    //----------------------------------------------------------------------------------------------

    /** How an image is positioned within its containing node. */
    public enum ImagePos implements Valuable, Attr.N {
        TOP_LEFT("tl"),
        TOP_CENTER("tc"),
        TOP_RIGHT("tr"),
        MIDDLE_LEFT("ml"),
        MIDDLE_CENTER("mc"),
        MIDDLE_RIGHT("mr"),
        BOTTOM_LEFT("bl"),
        BOTTOM_CENTER("bc"),
        BOTTOM_RIGHT("br"),
        ;
        private ImagePos(String value) { this.value = value; }
        private final String value;
        @Override public String attribute() { return "imagepos"; }
        @Override public String value() { return value; }
    }
    
    //----------------------------------------------------------------------------------------------

    /** How an image fills its containing node. */
    public enum ImageScale implements Valuable, Attr.N {
        /** Image retains its natural size. */         NONE("false"),
        /** Image is uniformly scale. */               UNIFORMLY("true"),
        /** The width of the image is scaled. */       WIDTH("width"),
        /** The height of the image is scaled. */      HEIGHT("height"),
        /** Height and Width are scaled separately. */ BOTH("both"),
        ;
        private ImageScale(String value) { this.value = value; }
        private final String value;
        @Override public String attribute() { return "imagescale"; }
        @Override public String value() { return value; }
    }
    
    //----------------------------------------------------------------------------------------------
    
    /** How to treat a node whose name has the form <code>|edgelabel|*</code> as a special node representing an edge label. */
    public enum LabelScheme implements Valuable, Attr.G {
        /** Produces no effect, the default (<code>0</code>). */
        NO_EFFECT(0),
        /** Uses a penalty-based method to make that kind of node close to the center of its neighbor (<code>1</code>). */
        NEIGHBOR(1),
        /** Uses a penalty-based method to make that kind of node close to the old center of its neighbor (<code>2</code>). */
        OLD_CENTER(2),
        /** Two-step process of overlap removal and straightening (<code>3</code>). */
        TWO_STEPS(3),
        ;
        private LabelScheme(int value) { this.value = Integer.toString(value); }
        private final String value;
        @Override public String attribute() { return "label_scheme"; }
        @Override public String value() { return value; }
    }
    
    //----------------------------------------------------------------------------------------------

    /** Justification for graph & cluster labels. */
    public enum LabelJust implements Valuable, Attr.GS {
        /** The label is centered (<code>c</code>), the default. */
        CENTER,
        /** The label is right-justified within bounding rectangle (<code>r</code>). */
        RIGHT,
        /** The label is left-justified (<code>l</code>). */
        LEFT,
        ;
        private final String value = name().toLowerCase().substring(0, 1);
        @Override public String attribute() { return "labeljust"; }
        @Override public String value() { return value; }
    }
    
    //----------------------------------------------------------------------------------------------
    
    /** Vertical placement of labels for nodes, root graphs and clusters.*/
    public enum LabelLoc implements Valuable, Attr.GSN {
        CENTER,
        TOP,
        BOTTOM,
        ;
        private final String value = name().toLowerCase().substring(0, 1);
        @Override public String attribute() { return "labelloc"; }
        @Override public String value() { return value; }
    }
    
    //----------------------------------------------------------------------------------------------

    /** Technique for optimizing the layout; <code>neato</code>, <code>sfdp</code>? only. */
    public enum Mode implements Valuable, Attr.G {
        /** Use stress majorization, the default. */
        MAJOR,
        /** Use the Kamada-Kawai2 version of the gradient descent method. </P>
         * KK is sometimes appreciably faster for small (number of nodes < 100) graphs. 
         * A significant disadvantage is that KK may cycle. 
         */
        KK("KK"), 
        /** Use a version of the Stochastic Gradient Descent3 method. </P>
         * <code>sgd</code>'s advantage is faster and more reliable convergence than both the previous methods, while <code>sgd</code>'s 
         * disadvantage is that it runs in a fixed number of iterations and may require larger values of maxiter in some graphs.
         */
        SGC,
        /** Adds a top-down directionality similar to the layout used in <code>dot</code>, <i>experimental</i>. */
        HIER,
        /** Allows the graph to specify minimum vertical and horizontal distances between nodes.
         * @see #sep(double)
         */
        IPSEP,
        /** For <code>sfdp</code>, use a spring-electrical model, the default. */
        SPRING,
        /** For <code>sfdp</code>, use a spring-electrical model but take into account edge lengths specified by the <code>len</code> attribute. */
        MAXENT,
        ;
        private Mode() { this.value = name().toLowerCase(); }
        private Mode(String value ) { this.value = value; }
        private final String value;
        @Override public String attribute() { return "mode"; }
        @Override public String value() { return value; }
    }
    
    //----------------------------------------------------------------------------------------------
    
    /** Specifies how the distance matrix is computed for the input graph; <code>neato</code> only. */
    public enum Model implements Valuable, Attr.G {
        /** Use the length of the shortest path, where the length of each edge is given by its <code>len</code> attribute, the default. */
        SHORTPATH,
        /** Use the circuit resistance model to compute the distances. This tends to emphasize clusters. */
        CIRCUIT,
        /** Use the subset model. </P>
         * This sets the edge length to be the number of nodes that are neighbors of exactly one of the end points, 
         * and then calculates the shortest paths. This helps to separate nodes with high degree.
         */
        SUBSET,
        /** Use the <code>len</code> of an edge as the ideal distance between its vertices. */
        MDS,
        ;
        private final String value = name().toLowerCase();
        @Override public String attribute() { return "model"; }
        @Override public String value() { return value; }
    }
    
    //----------------------------------------------------------------------------------------------
    
    /**Constrains the left-to-right ordering of node edges; <code>dot</code> only. */
    public enum Ordering implements Valuable, Attr.GSN {
        /** No ordering. */
        DEFAULT(""),
        /** The outedges of a node, that is, edges with the node as its tail node, 
         *  must appear left-to-right in the same order in which they are defined in the input. */
        OUT("out"),
        /** The inedges of a node, that is, edges with the node as its head node 
         *  must appear left-to-right in the same order in which they are defined in the input. */
        IN("in"),
        ;
        private Ordering(String value) { this.value = value; }
        private final String value;
        @Override public String attribute() { return "ordering"; }
        @Override public String value() { return value; }
    }
    
    //----------------------------------------------------------------------------------------------
    
    /** Order in which nodes and edges are drawn. */
    public enum OutputOrder implements Valuable, Attr.G {
        /** Output in breadth first graph walk order, the default. */
        BREADTH_FIRST("breadthfirst"),
        /** Nodes are drawn first, followed by the edges. */
        NODES_FIRST("nodesfirst"),
        /** Edges are drawn first, followed by the nodes; edges appear beneath nodes. */
        EDGES_FIRST("edgesfirst"),
        ;
        private OutputOrder(String value) { this.value = value; }
        private final String value;
        @Override public String attribute() { return "outputorder"; }
        @Override public String value() { return value; }
    }
    
    //----------------------------------------------------------------------------------------------

    /** How node overlaps should be removed; <code>sfdp</code>, <code>fdp</code>, <code>neato</code> only. */
    public enum Overlap implements Valuable, Attr.G {
        /** Overlaps are retained, the default (<code>true</code>) except for <code>fdp</code> and <code>sfdp</code>. */
        RETAIN("true"),
        /** Overlaps are removed by uniformly scaling in x and y. */
        SCALE,
        /** <code>Prism</code>, a proximity graph-based algorithm, is used to remove node overlaps, 1000 attempts. */
        PRISM,
        /** <code>Prism</code>, a proximity graph-based algorithm, is used to remove node overlaps, only the scaling phase, the default for <code>sfdp</code>. */
        PRISM0,
        /** A Voronoi-based technique is used to remove overlaps. */
        VORONOI,
        /** <code>x</code> and <code>y</code> are separately scaled to remove overlaps. */
        SCALEXY,
        /** The layout will be scaled down as much as possible without introducing any overlaps, obviously assuming there are none to begin with. */
        COMPRESS,
        /** Overlap removal is done as a quadratic optimization to minimize node displacement while removing node overlaps.*/
        VPSC,
        /** Overlaps are moved by optimizing two constraint for the <code>x</code> axis first and then for the <code>y</code> axis. */
        @Deprecated
        ORTHOXY,
        /** Overlaps are moved by optimizing two constraint for the <code>y</code> axis first and then for the <code>y</code> axis. */
        @Deprecated
        ORTHOYX,
        /** Similar to {@link #ORTHOXY}, using a heuristic is used to reduce the bias between the two passes.  */
        @Deprecated
        ORTHO,
        /** Similar to {@link #ORTHOYX}, using a heuristic is used to reduce the bias between the two passes.  */
        @Deprecated
        ORTHO_YX,
        /** Similar to {@link #ORTHOXY},  pseudo-orthogonal ordering is enforced. */
        @Deprecated
        PORTHOXY,
        /** Similar to {@link #ORTHOYX},  pseudo-orthogonal ordering is enforced. */
        @Deprecated
        PORTHOYX,
        /** Similar to {@link #ORTHO},  pseudo-orthogonal ordering is enforced. */
        @Deprecated
        PORTHO,
        /** Similar to {@link #ORTHO_YX},  pseudo-orthogonal ordering is enforced. */
        @Deprecated
        PORTHO_YX,
        /** Overlap removal constraints are incorporated into the layout algorithm itself; <code>neato</code>, <code>ipsec</code> only. */
        IPSEC,
        ;
        private Overlap() { this.value = name().toLowerCase(); }
        private Overlap(String value) { this.value = value; }
        private final String value;
        @Override public String attribute() { return "overlap"; }
        @Override public String value() { return value; }
    }
    
    //----------------------------------------------------------------------------------------------
    
    /** The order in which pages are emitted, used only if <code>page</code> is set and applicable. <P/> 
     *  The first character corresponding to the major order and the second to the minor order.
     */
    public enum PageDir implements Valuable, Attr.G {
        BL, BR, 
        TL, TR, 
        RB, RT, 
        LB, LT, 
        ;
        private final String value = name();
        @Override public String attribute() { return "pagedir"; }
        @Override public String value() { return value; }
    }
    
    //==============================================================================================
    
    private static String layerSep = Character.toString(":\\t".codePointAt(0));
    private static String layerListSep = Character.toString(",".codePointAt(0));
    
    //==============================================================================================
    

    /** Create a new (undirected) Graph. */
    public static Graph graph() {
        return new GraphImpl();
    }
    
    /** Create a new named (undirected) Graph. */
    public static Graph graph(String id) {
        return new GraphImpl(id);
    }
    
    /** Create a directed Graph. */
    public static Graph digraph() {
        return new GraphImpl().directed();
    }
    
    /** Create a directed named Graph. */
    public static Graph digraph(String id) {
        return new GraphImpl(id).directed();
    }
    
    /** Create a new Node. */
    public static NodeId node(String id) {
        return new NodeImpl(id);
    }

    /** Create a new Node with ID and Port. */
    public static NodeId node(String id, Port port) {
        return new NodeImpl(id, port);
    }

    /** Create a new Edge. */
    public static Edge edge(Source source, Target target) {
        return new EdgeImpl(source, target);
    }
    
    /** Create a new Edge between the given nodes. */
    public static Edge edge(String source, String target) {
        return edge(node(source), node(target));
    }
    
    /** Create a new Subgraph. */
    public static Subgraph subgraph() {
        return new SubgraphImpl();
    }
    
    /** Create a new named Subgraph. */
    public static Subgraph subgraph(String id) {
        return new SubgraphImpl(id);
    }
    
    /** Create a new Subgraph. */
    public static Subgraph subgraph(Node... nodes) {
        var sub = new SubgraphImpl();
        Arrays.stream(nodes).forEach(sub::add);
        return sub;
    }
    
    //----------------------------------------------------------------------------------------------
    
    /** Arbitrary backgorund using the <a href="https://www.graphviz.org/docs/attr-types/xdot/">xodt format</a>. */
    public static Attr.G _background(XDot xdot) { return new AttributeImpl("_background", xdot); }
    
    /** Preferred area for a node or empty cluster, default: <code>1.0</code>, minimum: <code>&gt; 0.0</code>. <a href="https://www.graphviz.org/docs/layouts/patchwork/">patchwork</a> only. */
    public static Attr.SN area(double area) { return positiveAttribute("area", area); }
    
    /** Arrow style of the head node of an edge. */
    public static Attr.E arrowhead(ArrowType type) { return new AttributeImpl("arrowhead", type); }
    
    /** Multiplicative scale factor for arrow heads, default: <code>1.0</code>, minimum: <code>0.0</code>. */
    public static Attr.E arrowsize(double scale) { return nonNegativeAttribute("arrowsize", scale); }
    
    /** Arrow style of the tail node of an edge. */
    public static Attr.E arrowtail(ArrowType type) { return new AttributeImpl("arrowtail", type); }
    
    /** Draw leaf nodes uniformly in a circle around the root node in <code>sfdp</code>. */
    public static Attr.G beautify() { return beautify(true); }
    
    /** Draw leaf nodes uniformly in a circle around the root node in <code>sfdp</code>, default: <code>false</code>. */
    public static Attr.G beautify(boolean beautify) { return booleanAttribute("beautify", beautify); }
    
     /** Canvas background color. */
    public static Attr.GS bgcolor(Color color) { return new AttributeImpl("bgcolor", color); }
    
    /** Canvas background color. */
    public static Attr.GS bgcolor(ColorList colors) { return new AttributeImpl("bgcolor", colors); }
    
    /** Center the drawing in the output canvas. */
    public static Attr.G center() { return center(true); }
    
    /** Center the drawing in the output canvas, default: <code>false</code>. */
    public static Attr.G center(boolean center) { return booleanAttribute("center", center); }
    
    /** Character encoding used when interpreting string input as a text label, , default: <code>UTF-8</code>. */
    public static Attr.G charset(CharSet charset) { return new AttributeImpl("charset", charset); }
    
    /** Classnames to attach to the node, edge, graph, or cluster's SVG element; <code>svg</code> only. */
    public static Attr.GSNE classname(String... names) { return new AttributeImpl("class", String.join(" ", names)); }
    
    /** Subgraph is a cluster. */
    public static Attr.S cluster() { return cluster(true); }
    
    /** Whether the subgraph is a cluster, default: <code>false</code>. */
    public static Attr.S cluster(boolean cluster) { return booleanAttribute("cluster", cluster); }
    
    /** Mode used for handling clusters, default: <code>true</code>. Bounding rectangle drawn arround cluster and label, if present. */
    public static Attr.G clusterrank(boolean local) { return new AttributeImpl("clusterrank", local ? "local" : "global"); }
    
    /**  Basic drawing color for graphics, not text. */
    public static Attr.SNE color(ColorList color) { return new AttributeImpl("color", color); }
    
    /** A color scheme namespace: the context for interpreting color names. 
     * @deprecated TODO testing only, need ColorScheme enum?. */
    @Deprecated
    public static Attr.GSNE colorscheme(String name) { return new AttributeImpl("colorscheme", name); }
    
    /** Comments are inserted into output. */
    public static Attr.GNE comment(String text) { return new AttributeImpl("comment", text); }
    
    /** Allow edges between clusters. */
    public static Attr.G compound() { return compound(true); }
    
    /** Allow edges between clusters. */
    public static Attr.G compound(boolean allow) { return booleanAttribute("compound", allow); }
    
    /** Use edge concentrators - merges multiedges into a single edge and causes partially parallel edges to share part of their paths. */
    public static Attr.G concentrate() { return concentrate(true); }
    
    /** Use edge concentrators, merges multiedges into a single edge and causes partially parallel edges to share part of their paths. */
    public static Attr.G concentrate(boolean concentrate) { return booleanAttribute("concentrate", concentrate); }
    
    /** Edge is not used in ranking the nodes; <code>dot</code> only. */
    public static Attr.E unconstraint() { return constraint(false); }
    
    /** If <code>false</code> Edge is not used in ranking the nodes; <code>dot</code> only. */
    public static Attr.E constraint(boolean constraint) { return booleanAttribute("constraint", constraint); }
    
    /** Factor damping force motions, default: <code>0.99</code>, minimum: <code>0.0</code>; <code>neato</code> only. */
    public static Attr.G damping(double factor) { return nonNegativeAttribute("Damping", factor); }
    
    /** Connect the edge label to the edge with a line. */
    public static Attr.E decorate() { return decorate(true); }
    
    /** Whether to connect the edge label to the edge with a line. */
    public static Attr.E decorate(boolean decorate) { return booleanAttribute("decorate", decorate); }
    
    /** The distance between nodes in separate connected components, minimum: <code>epsilon</code>; <code>neato</code> only. */
    public static Attr.G defaultdist(double dist) { return positiveAttribute("defaultdist", dist); }
    
    /** Set the number of dimensions used for the layout (<code>2</code>-<code>10</code>); <code>neato</code>, <code>fdp</code> and <code>sfdp</code> only. */
    public static Attr.G dim(int dim) { return rangedAttribute("dim", dim, 2, 10); }
    
    /** Set the number of dimensions used for rendering (<code>2</code>-<code>10</code>); <code>neato</code>, <code>fdp</code> and <code>sfdp</code> only. */
    public static Attr.G dimen(int dimen) { return rangedAttribute("dimen", dimen, 2, 10); }
    
    /** Edge type for drawing arrowheads. */
    public static Attr.E dir(DirType dir) { return new AttributeImpl("dir", dir); }
    
    /** Constrain most edges to point downwards; <code>mode=ipsep</code>, <code>neato</code> only. */
    public static Attr.G diredgeconstraints() { return diredgeconstraints(true); }
    
    /** Constrain most edges to point downwards; <code>mode=ipsep</code>, <code>neato</code> only. */
    public static Attr.G diredgeconstraints(boolean constrain) { return booleanAttribute("diredgeconstraints", constrain); }
    
    /** Distortion factor for <code>shape=polygon</code>. </P>
     * Positive values cause top part to be larger than bottom; negative values do the opposite, default: <code>0.0</code>, minimum: <code>-100.0</code>.
     */
    public static Attr.N distortion(double distortion) { return minimumAttribute("distortion", distortion, -100.0); }
    
    /** Specifies the expected number of pixels per inch on a display device; bitmap output, <code>svg</code> only. */
    public static Attr.G dpi(double dpi) { return nonNegativeAttribute("dpi", dpi); }
    
    /** The link for the non-label parts of an edge; map, <code>svg</code> only. */
    public static Attr.E edgehref(String url) { return new AttributeImpl("edgehref", url); }
    
    /** Browser window to use for the <code>edgeURL</code> link; map, <code>svg</code> only. */
    public static Attr.E edgetarget(String target) { return new AttributeImpl("edgetarget", target); }
    
    /** Tooltip annotation attached to the non-label part of an edge; <code>cmap</code>, <code>svg</code> only. */
    public static Attr.E edgetooltip(String tooltip) { return new AttributeImpl("edgetooltip", tooltip); }
    
    /** The link for the non-label parts of an edge; map, <code>svg</code> only. */
    public static Attr.E edgeURL(String url) { return new AttributeImpl("edgeURL", url); }
    
    /** Terminating condition, if the length squared of all energy gradients are less than epsilon, the algorithm stops; <code>neato</code> only. */
    public static Attr.G epsilon(double epsilon) { return positiveAttribute("epsilon", epsilon); }
    
    /** Margin used around polygons for purposes of spline edge routing; <code>neato</code> only. */
    public static Attr.G esep(double separation) { return new AttributeImpl("esep", new PointImpl(false, separation)); }
    
    /** Margin used around polygons for purposes of spline edge routing; <code>neato</code> only. */
    public static Attr.G esep(double w, double h) { return new AttributeImpl("esep", new PointImpl(false, w, h)); }
    
    /** Margin added around polygons for purposes of spline edge routing; <code>neato</code> only. */
    public static Attr.G esep(Point add) { return new AttributeImpl("esep", add); }
    
    /** Color used to fill the background of a node or cluster. */
    public static Attr.SNE fillcolor(ColorList color) { return new AttributeImpl("fillcolor", color); }
    
    /** Whether to use the specified <code>width</code> and <code>height</code> attributes to choose node size (rather than sizing to fit the node contents.) */
    public static Attr.N fixedsize(boolean fixed) { return booleanAttribute("fixedsize", fixed); }
    
    /** Use the specified <code>width</code> and <code>height</code> attributes to choose node size. */
    public static Attr.N fixedsize() { return booleanAttribute("fixedsize", true); }
    
    /** Whether to use the specified <code>width</code> and <code>height</code> attributes to choose node size (rather than sizing to fit the node contents.) */
    public static Attr.N fixedsize(FixedSize fixed) { return new AttributeImpl("fixedsize", fixed); }
    
    /** Color used for text. */
    public static Attr.GSNE fontcolor(ColorList color) { return new AttributeImpl("fontcolor", color); }
    
    /** Font used for text, default: <code>"Times-Roman"</code>. */
    public static Attr.GSNE fontname(String name) { return new AttributeImpl("fontname", name); }
    
    /** How basic fontnames are represented in SVG output; <code>svg</code> only. */
    public static Attr.G fontnames(FontNames handling) { return new AttributeImpl("fontnames", handling); }
    
    /** Directory list used by libgd to search for bitmap fonts. */
    public static Attr.G fontpath(Path path) { return new AttributeImpl("fontpath", path.toString()); }
    
    /** Font size, in points, used for text, default: <code>14.0</code>, minimum: <code>1.0</code>. */
    public static Attr.GSNE fontsize(double size) { return minimumAttribute("fontsize", size, 1.0); }
    
    /** Do not force placement of all <code>xlabels</code>, even if overlapping. */
    public static Attr.G unforcelabels() { return forcelabels(false); }
    
    /** Force placement of all <code>xlabels</code>, even if overlapping, default: <code>true</code>. */
    public static Attr.G forcelabels(boolean force) { return booleanAttribute("forcelabels", force); }
    
    /** If a gradient fill is being used, this determines the angle of the fill, default: <code>0</code>, minimum: <code>0</code>. */
    public static Attr.GSN gradientangle(int angle) { return nonNegativeAttribute("gradientangle", angle); }
    
    /** Name for a group of nodes, for bundling edges avoiding crossings; <code>dot</code> only. */
    public static Attr.N group(String name) { return new AttributeImpl("group", name); }
    
    /** The end of the edge goes to the center. */
    public static Attr.E noheadclip() { return headclip(false); }
    
    /** Clip the head of an edge to the boundary of the head node, otherwise the end of the edge goes to the center, default: <code>true</code>. */
    public static Attr.E headclip(boolean clip) { return booleanAttribute("headclip", clip); }
    
    /** Hyperlink output as part of the head label of the edge; <code>map</code>, <code>svg</code> only. */
    public static Attr.E headhref(String url) { return new AttributeImpl("headhref", url); }
    
    /** Text label to be placed near head of edge. */
    public static Attr.E headlabel(String label) { return new AttributeImpl("headlabel", label); }
    
    /** Text label to be placed near head of edge. */
    public static Attr.E headlabel(HTML label) { return new AttributeImpl("headlabel", label); }
    
    /** Indicates where on the head node to attach the head of the edge. 
     * @depreceted testing */
    @Deprecated
    public static Attr.E headport(String port) { return new AttributeImpl("headport", port); }
    
    /** Indicates where on the head node to attach the head of the edge. */
    public static Attr.E headport(Compass port) { return new AttributeImpl("headport", port); }
    
    /** Indicates where on the head node to attach the head of the edge. */
    public static Attr.E headport(Port port) { return new AttributeImpl("headport", port); }
    
    /** Browser window to use for the <code>headURL</code> link; <code>map</code>, <code>svg</code> only. */
    public static Attr.E headtarget(String target) { return new AttributeImpl("headtarget", target); }
    
    /** Tooltip annotation attached to the head of an edge; <code>cmap</code>, <code>svg</code> only. */
    public static Attr.E headtooltip(String tooltip) { return new AttributeImpl("headtooltip", tooltip); }
    
    /** Hyperlink output as part of the head label of the edge; <code>map</code>, <code>svg</code> only. */
    public static Attr.E headURL(String url) { return new AttributeImpl("headURL", url); }
    
    /** The initial, minimum height of the node in inches, default: <code>0.5</code<, minimum: <code>0.02</code>. */
    public static Attr.N height(double inches) { return minimumAttribute("height", inches, 0.02); }
    
    /** Hyperlinks incorporated into device-dependent output; <code>map</code>, <code>postscript</code>, <code>svg</code> only. */
    public static Attr.GSNE href(String url) { return new AttributeImpl("href", url); }
    
    /** Identifier for graph objects; <code>map</code>, <code>postscript</code>, <code>svg</code> only. */
    public static Attr.GSNE id(String id) { return new AttributeImpl("id", id); }
    
    /** Gives the name of a file containing an image to be displayed inside a node. */
    public static Attr.N image(String path) { return new AttributeImpl("image", path); }
    
    /** A list of directories in which to look for image files. */
    public static Attr.G imagepath(String... paths) { return new AttributeImpl("imagepath", String.join(File.pathSeparator, paths)); }
    
    /** How an image is positioned within its containing node, only has an effect when the image is smaller than the containing node. */
    public static Attr.N imagepos(ImagePos pos) { return new AttributeImpl("imagepos", pos); }
    
    /** How an image fills its containing node. */
    public static Attr.N imagescale(ImageScale scale) { return new AttributeImpl("imagescale", scale); }
    
    /** Scales the input <code>positions</code> to convert between length units; <code>neato</code>, <code>fdp</code> only. */
    public static Attr.G inputscale(double scale) { return doubleAttribute("inputscale", scale); }
    
    /** Spring constant used in virtual physical model, default: <code>0.3</code>, minimum: <code>0.0</code>; <code>fdp</code>, <code>sfdp</code> only. */
    public static Attr.GS K(double inches) { return nonNegativeAttribute("K", inches); }
    
    /** Label attribute. */
    public static Attr.GSNE label(String label) { return new AttributeImpl("label", label); }
    
    /** Label attribute. */
    public static Attr.GSNE label(HTML label) { return new AttributeImpl("label", label); }
    // TODO
//    public static Attr.GSNE label(Record rec) { return new AttributeImpl("label", label); }

    /** How to treat a node whose name has the form <code>|edgelabel|*</code> as a special node representing an edge label; <code>sfdp</code> only. */
    public static Attr.G label_scheme(LabelScheme scheme) { return new AttributeImpl("label_scheme", scheme); }
    
    /** How to treat a node whose name has the form <code>|edgelabel|*</code> as a special node representing an edge label,
     * default: <code>0</code>, minimum: <code>0</code>; <code>sfdp</code> only.
     */
    public static Attr.G label_scheme(int scheme) { return nonNegativeAttribute("label_scheme", scheme); }
    
    /** The angle (in degrees) in polar coordinates of the head & tail edge labels, default: <code>-25.0</code>, minimum: <code>-180.0</code>.</P>
     * The ray of 0 degrees goes from the origin back along the edge, parallel to the edge at the origin.
     * The angle, in degrees, specifies the rotation from the 0 degree ray, with positive angles moving counterclockwise and negative angles moving clockwise.
     */
    public static Attr.E labelangle(double angle) { return minimumAttribute("labelangle", angle, -180.0); }
    
    /** Scaling factor for the distance of <code>headlabel</code> / <code>taillabel</code> from the head / tail nodes, default: <code>1.0</code>, minimum: <code>0.0</code>. */
    public static Attr.E labeldistance(double scale) { return nonNegativeAttribute("labeldistance", scale); }
    
    /** Allows edge labels to be less constrained in position. */
    public static Attr.E labelfloat() { return labelfloat(true); }
    
    /** Allows edge labels to be less constrained in position. */
    public static Attr.E labelfloat(boolean allow) { return booleanAttribute("labelfloat", allow); }
    
    /** Color used for <code>headlabel</code> and <code>taillabel</code>. */
    public static Attr.E labelfontcolor(Color color) { return new AttributeImpl("labelfontcolor", color); }
    
    /** Font for <code>headlabel</code> and <code>taillabel</code>. */
    public static Attr.E labelfontname(String name) { return new AttributeImpl("labelfontname", name); }
    
    /** Font size of <code>headlabel</code> and <code>taillabel</code>, default: <code>14.0</code>, minimum: <code>1.0</code>. */
    public static Attr.E labelfontsize(double size) { return minimumAttribute("labelfontsize", size, 1.0); }
    
    /** The link used for the label of an edge; <code>map</code>, <code>svg</code> only. */
    public static Attr.E labelhref(String url) { return new AttributeImpl("labelhref", url); }
    
    /** Justification for graph & cluster labels. */
    public static Attr.GS labeljust(LabelJust just) { return new AttributeImpl("labeljust", just); }
    
    /** Vertical placement of labels for nodes, root graphs and clusters. */
    public static Attr.GSN labelloc(LabelLoc loc) { return new AttributeImpl("labelloc", loc); }
    
    /** Browser window to open <code>labelURL</code> links in; <code>map</code>, <code>svg</code> only. */
    public static Attr.E labeltarget(String target) { return new AttributeImpl("labeltarget", target); }
    
    /** Tooltip annotation attached to label of an edge; <code>cmap</code>, <code>svg</code> only. */
    public static Attr.E labeltooltip(String tooltip) { return new AttributeImpl("labeltooltip", tooltip); }
    
    /** The link used for the label of an edge; <code>map</code>, <code>svg</code> only. */
    public static Attr.E labelURL(String url) { return new AttributeImpl("labelURL", url); }
    
    /** Render the graph in landscape mode. */
    public static Attr.G landscape() { return landscape(true); }
    
    /** Render the graph in landscape mode. */
    public static Attr.G landscape(boolean landscape) { return booleanAttribute("landscape", landscape); }
    
    /** Specifies layers in which the node, edge or cluster is present. */
    public static Attr.SNE layer(LayerRange... ranges) { 
        return new AttributeImpl("layer", Arrays.stream(ranges).map(lr -> (Valuable)lr).map(Valuable::value).collect(joining(layerListSep)));
    }

    /** The separator characters used to split attributes of type <code>layerRange</code> into a list of ranges, default: <code>","</code>. */
    public static Attr.G layerlistsep(String separators) {
        layerListSep = Character.toString(separators.codePointAt(0));
        return new AttributeImpl("layerlistsep", separators);
    }
    
    /** A linearly ordered list of layer names attached to the graph. */
    public static Attr.G layers(String... layers) { return new AttributeImpl("layers", String.join(layerSep, layers)); }
    
    /** Selects a list of layers to be emitted. */
    public static Attr.G layerselect(LayerRange... ranges) {
        return new AttributeImpl("layerselect", Arrays.stream(ranges).map(lr -> (Valuable)lr).map(Valuable::value).collect(joining(layerListSep)));
    }
    
    /** The separator characters for splitting the layers attribute into a list of layer names, default: <code>":\t "</code>. */
    public static Attr.G layersep(String separators) {
        layerSep = Character.toString(separators.codePointAt(0));
        return new AttributeImpl("layersep", separators);
    }
    
    /** Which layout engine to use. */
    public static Attr.G layout(Engine engine) { return new AttributeImpl("layout", engine); }
    
    /** Preferred edge length, in inches, default: <code>1.0</code (<i>neato</i>), <code>0.3</code> (<i>fdp</i>); <code>neato</code>, <code>fdp</code> only. */
    public static Attr.E len(double inches) { return nonNegativeAttribute("len", inches); }
    
    /** Number of levels allowed in the multilevel scheme, default: INT_MAX, minimum: <code>0</code>; <code>sfpd</code> only. */
    public static Attr.G levels(int levels) { return nonNegativeAttribute("levels", levels); }
    
    /** Strictness of neato level constraints, default: <code>0.0</code>; <code>neato</code> only. */
    public static Attr.G levelsgap(double strictness) { return doubleAttribute("levelsgap", strictness); }
    
    /** Logical head of an edge; <code>compound=true</code>, <code>dot</code> only. */
    public static Attr.E lhead(String head) { return new AttributeImpl("lhead", head); }

    /** How long strings should get before overflowing to next line, for text output, default: <code>128</code>, minimum: <code>60</code>. */
    public static Attr.G linelength(int characters) {return minimumAttribute("linelength", characters, 60); }
    
    /** Logical tail of an edge; <code>compound=true</code>, <code>dot</code> only. */
    public static Attr.E ltail(String tail) { return new AttributeImpl("ltail", tail); }
    
    /** For graphs, this sets both x and y margins of canvas, in inches.</br>
     *  For clusters, margin specifies the space between the nodes in the cluster and the cluster bounding box, in points, default <code>8</code> points.</br> 
     *  For nodes, this attribute specifies space left around the node's label, in inches, default <code>0.11,0.055</code>.
     */
    public static Attr.GSN margin(double margin) { return doubleAttribute("margin", margin); }
    
    /** For graphs, this sets x and y margins of canvas, in inches.</br>
     *  For nodes, this attribute specifies space left around the node's label, in inches, default <code>0.11,0.055</code>.
     */
    public static Attr.GN margin(double x, double y) { return new AttributeImpl("margin", new PointImpl(false, x, y)); }
    
    /** For graphs, this sets x and y margins of canvas, in inches.</br>
     *  For nodes, this attribute specifies space left around the node's label, in inches, default <code>0.11,0.055</code>.
     */
    public static Attr.GN margin(Point margin) { return new AttributeImpl("margin", margin); }
    
    /** Sets the number of iterations used; <code>neato</code>, <code>fdp</code> only. */
    public static Attr.G maxiter(int iterations) { return intAttribute("maxiter", iterations); }
    
    /** Scale factor for mincross (mc) edge crossing minimiser parameters, default: <code>1.0</code>; <code>dot</code> only. */
    public static Attr.G mclimit(double factor) { return doubleAttribute("mclimit", factor); }
    
    /** Specifies the minimum separation between all nodes, default: <code>1.0</code>, minimum: <code>0.0</code>; <code>circo</code> only. */
    public static Attr.G mindist(double separation) { return nonNegativeAttribute("mindist", separation); }
    
    /** Minimum edge length (rank difference between head and tail), default: <code>1</code>, minimum: <code>0</code>; <code>dot</code> only. */
    public static Attr.E minlen(int rank) { return nonNegativeAttribute("minlen", rank); }
    
    /** Technique for optimizing the layout, default: <code>MAJOR</code>; <code>neato</code>, <code>sfdp</code>? only. */
    public static Attr.G mode(Mode mode) { return new AttributeImpl("mode", mode); }
    
    /** Specifies how the distance matrix is computed for the input graph, deafult: <code>SHORTPATH</code>; <code>neato</code> only. */
    public static Attr.G model(Model model) { return new AttributeImpl("model", model); }
    
    /** Use a single global ranking, ignoring clusters; <code>dot</code> only. */
    public static Attr.G newrank() { return newrank(true); }
    
    /** Use a single global ranking, ignoring clusters; <code>dot</code> only. */
    public static Attr.G newrank(boolean ignore) { return booleanAttribute("newrank", ignore); }
    
    /** <code>dot</code>: the minimum space between two adjacent nodes in the same rank, in inches;
     * other layouts: affects the spacing between loops on a single node, or multiedges between a pair of nodes;
     * defualt: <code>0.25</code>, minimum: <code>0.02</code>.
     */
    public static Attr.G nodesep(double inches) { return minimumAttribute("nodesep", inches, 0.02); }
    
    /** Justify multiline text vs the previous text line (rather than the side of the container). */
    public static Attr.GSNE nojustify() { return nojustify(true); }
    
    /** Justify multiline text vs the previous text line (rather than the side of the container). */
    public static Attr.GSNE nojustify(boolean nojustify) { return booleanAttribute("nojustify", nojustify); }
    
    /** Normalizes coordinates of final layout, so that the first point is at the origin (0 degrees); 
     * <code>neato</code>, <code>fdp</code>, <code>sfdp</code>, <code>twopi</code>, <code>circo</code> only.
     */
    public static Attr.G normalize() { return normalize(true); }
    
    
    /** Normalizes coordinates of final layout, so that the first point is at the origin (0 degrees); 
     * <code>neato</code>, <code>fdp</code>, <code>sfdp</code>, <code>twopi</code>, <code>circo</code> only. 
     */
    public static Attr.G normalize(boolean normalize) { return booleanAttribute("normalize", normalize); }
    
    
    /** Normalizes coordinates of final layout, so that the first point is at the origin, 
     * and then rotates the layout so that the angle of the first edge is specified by the value of normalize in degrees; 
     * <code>neato</code>, <code>fdp</code>, <code>sfdp</code>, <code>twopi</code>, <code>circo</code> only. 
     */
    public static Attr.G normalize(double degrees) { return doubleAttribute("normalize", degrees); }
    
    /** Avoid translating layout to the origin point; <code>neato</code> only. */
    public static Attr.G notranslate() { return notranslate(true); }
    
    /** Avoid translating layout to the origin point; <code>neato</code> only. */
    public static Attr.G notranslate(boolean notranslate) { return booleanAttribute("notranslate", notranslate); }
    
    /** Number of iterations in network simplex applications in computing node x coordinates, <code># iterations = nslimit * # nodes</code>; <code>dot</code> only. */
    public static Attr.G nslimit(double factor) { return doubleAttribute("nslimit", factor); }
    
    /** Number of iterations in network simplex applications for ranking nodes, <code># iterations = nslimit * # nodes</code>; <code>dot</code> only. */
    public static Attr.G nslimit1(double factor) { return doubleAttribute("nslimit1", factor); }
    
    /** Draw circo graphs around one circle; <code>circo</code> only. */
    public static Attr.G oneblock() { return oneblock(true); }
    
    /** Draw circo graphs around one circle, default: <code>false</code>; <code>circo</code> only. */
    public static Attr.G oneblock(boolean oneblock) { return booleanAttribute("oneblock", oneblock); }
    
    /** Constrains the left-to-right ordering of node edges; <code>dot</code> only. */
    public static Attr.GSN ordering(Ordering ordering) { return new AttributeImpl("ordering", ordering); }
    
    /** Node shape rotation angle,in degrees, default: <code>0.0</code>, minimum <code>-360.0</code>. */
    public static Attr.N orientation(double degrees) { return minimumAttribute("orientation", degrees, -360.0); }
    
    /** Graph orientation, landscape. */
    public static Attr.G orientation() { return orientation(true); }
    
    /** Graph orientation, landscape. */
    public static Attr.G orientation(boolean landscape) { return new AttributeImpl("orientation", landscape ? "landscape" : "default"); }
    
    /** Order in which nodes and edges are drawn. */
    public static Attr.G outputorder(OutputOrder order) { return new AttributeImpl("outputorder", order); }
    
    /** How node overlaps should be removed; <code>sfdp</code>, <code>fdp</code>, <code>neato</code> only. </P>
     *  If <code>true</code>, overlaps are retained. 
     *  If <code>false</code>, and available, Prism, a proximity graph-based algorithm, is used to remove node overlaps;
     *  otherwise, if not available, Voronoi-based technology is used. 
     *  @see Overlap#PRISM
     *  @see Overlap#VORONOI  */
    public static Attr.G overlap(boolean retain) { return booleanAttribute("overlap", retain); }
    
    /** How node overlaps should be removed; <code>sfdp</code>, <code>fdp</code>, <code>neato</code> only. */
    public static Attr.G overlap(Overlap overlap) { return new AttributeImpl("overlap", overlap); }
    
    /** How node overlaps should be removed and given number of attempts - for {@link Overlap#PRISM} mode; <code>sfdp</code>, <code>fdp</code>, <code>neato</code> only. */
    public static Attr.G overlap(Overlap overlap, int attempts) {
        if (overlap != Overlap.PRISM)
            throw new IllegalArgumentException("attempts can only be used with Overlap.PRISM, not: " + overlap);
        if (attempts < 0)
            throw new IllegalArgumentException("number of attempts must be non-negative: " + attempts);
        return new AttributeImpl("overlap", overlap.value() + attempts); 
    }
    
    /** How node overlaps should be removed, first try a number of passes using a built-in, force-directed technique; <code>sfdp</code>, <code>fdp</code> only. */
    public static Attr.G overlap(int passes, Overlap overlap) {
        if (passes < 0)
            throw new IllegalArgumentException("number of passes must be non-negative: " + passes);
        return new AttributeImpl("overlap", passes + ":" + overlap.value()); 
    }
    
    /** Scale layout by factor, to reduce node overlap, default: <code>-4.0<code>, minimum: <code>-1e+10</code>; <code>overlap=prism</code>, <code>neato</code>, <code>sfdp</code>, <code>fdp</code>, <code>circo</code>, <code>twopi</code> only. */
    public static Attr.G overlap_scale(double scale) { return minimumAttribute("overlap_scale", scale, -1E+10); }
    
    /** The overlap removal algorithm should perform a compression pass to reduce the size of the layout, default: <code>true</code>; <code>prism</code> only. */
    public static Attr.G overlap_shrink(boolean shrink) { return booleanAttribute("overlap_shrink", shrink); }
    
    /** Each connected component of the graph should be laid out separately, and then packed together. */
    public static Attr.G pack() { return pack(true); }
    
    /** Each connected component of the graph should be laid out separately, and then packed together, default: <code>false</code>. */
    public static Attr.G pack(boolean pack) { return booleanAttribute("pack", pack); }
    
    /** Each connected component of the graph should be laid out separately, and then packed together using given margin in points, default: <code>8</code>. */
    public static Attr.G pack(int margin) { return intAttribute("pack", margin); }
    
    /** How connected components should be packed, default: <code>node</code>. */
    public static Attr.G packmode(PackMode mode) { return new AttributeImpl("packmode", mode); }

    /** Extend the drawing area around the minimal area needed to draw the graph in inches, default: <code>0.0555</code>. */
    public static Attr.G pad(double inches) { return doubleAttribute("pad", inches); }
    
    /** Extend the drawing area around the minimal area needed to draw the graph in inches, default: <code>0.0555</code>. */
    public static Attr.G pad(Point inches) { return new AttributeImpl("pad", inches); }
    
    /** Width and height of output pages, in inches; Postscript only. */
    public static Attr.G page(double inches) { return doubleAttribute("page", inches); }
    
    /** Width and height of output pages, in inches; Postscript only. */
    public static Attr.G page(Point inches) { return new AttributeImpl("page", inches); }
    
    /** The order in which pages are emitted, used only if <code>page</code> is set and applicable, default: <code>BL</code>. <P/> 
     *  The first character corresponding to the major order and the second to the minor order.
     */
    public static Attr.G pagedir(PageDir dir) { return new AttributeImpl("pagedir", dir); }
    
    /** Color used to draw the bounding box around a cluster, default: <code>black</code>. */
    public static Attr.S pencolor(Color color) { return new AttributeImpl("pencolor", color); }
    
    /** Width of the pen, in points, used to draw lines and curves, default: <code>1.0</code>, minimum: <code>0.0</code>. */
    public static Attr.SNE penwidth(double points) { return minimumAttribute("penwidth", points, 0.0); }
    
    /** Number of peripheries used in polygonal shapes and cluster boundaries, minimum: <code>0</code>. */
    public static Attr.SN peripheries(int count) { return nonNegativeAttribute("peripheries", count); }
    
    /** Keep the node at the node's given input position if the node has a <code>pos</code>, default: <code>false</code>; <code>neato</code>, <code>fdp</code> only. */
    public static Attr.N pin() { return pin(true); }
    
    /** Keep the node at the node's given input position if the node has a <code>pos</code>, default: <code>false</code>; <code>neato</code>, <code>fdp</code> only. */
    public static Attr.N pin(boolean pin) { return booleanAttribute("pin", pin); }
    
    /** Position of node; <code>neato</code>, <code>fdp</code> only. */
    public static Attr.N pos(Point pos) { return new AttributeImpl("pos", pos); }

    // TODO
    // public static Attr.E pos(...)
    
    
    
    
    // TODO 
    
    
    
    
    
    
    /** Arbitrary attribute. 
     *  @deprecated for testing, maybe delted (or not) 
     */
    @Deprecated
    public static Attr.GSNE attribute(String name, CharSequence value) { return new AttributeImpl(name, value); }
    /** Arbitrary attribute. 
     *  @deprecated for testing, maybe delted (or not) 
     */
    @Deprecated
    public static Attr.GSNE attribute(String name, Object value) { return new AttributeImpl(name, String.valueOf(value)); }
    
    //----------------------------------------------------------------------------------------------
    
    /** Create an <code>ArrowTpe</code> with the given shape descriptions. 
     * @deprecated mostly for testing. */  // TODO shape functions
    @Deprecated
    public static ArrowType arrowtype(String... shapes) { return new ArrowTypeImpl(shapes); }
    
    /** TODO */
    public static Color rgb(int r, int g, int b) { return new ColorImpl(r, g, b); }
    
    /** TODO */
    public static Color rgb(int a, int r, int g, int b) { return new ColorImpl(a, r, g, b); }
    
    /** TODO */
    public static Color hsv(double h, double s, double v) { return new ColorImpl(h, s, v); }
    
    /** TODO */
    public static Color hsv(double a, double h, double s, double v) { return new ColorImpl(a, h, s, v); }
    
    /** Create a color with given description. 
     * @deprecated mostly for testing. */
    @Deprecated
    public static Color color(String text) { return new ColorImpl(text); }
    
    /** Create an HTML Label. */
    public static HTML html(String html) { return new HTMLImpl(html); }
    
    /** Create a 2D <code>Point</code. */
    public static Point point(double x, double y) { return new PointImpl(false, x, y); }
    
    /** Create a 3D <code>Point</code>. */
    public static Point point(double x, double y, double z) { return new PointImpl(false, x, y, z); }
    
    /** Create a 2D <code>Point</code> with a prepended <code>+</code> to be used for <code>sep</code> and  <code>esep</code>. */
    public static Point add(double x, double y) { return new PointImpl(true, x, y); }
    
    /** Create a 3D <code>Point</code> with a prepended <code>+</code> to be used for <code>sep</code> and  <code>esep</code>. */
    public static Point add(double x, double y, double z) { return new PointImpl(true, x, y, z); }
    
    /** Create a <code>Port</code> with the given name. */
    public static Port port(String name) { return new PortImpl(name, null); }
    
    /** Create a <code>Port</code> with the given name and compass direction. */
    public static Port port(String name, Compass compass) { return new PortImpl(name, compass); }
    
    /** Create an empty <code>LayerRange</code>. */
    public static LayerRange range() { return new LayerRangeImpl(layerListSep, layerSep); }
    /** Create a <code>LayerRange</code> with the given layer. */
    public static LayerRange range(String layer) { return new LayerRangeImpl(layerListSep, layerSep, layer); }
    /** Create a <code>LayerRange</code> with the given layer. */
    public static LayerRange range(int layer) { return new LayerRangeImpl(layerListSep, layerSep, layer); }
    /** Create a <code>LayerRange</code> with the given range. */
    public static LayerRange range(String first, String last) { return new LayerRangeImpl(layerListSep, layerSep, first, last); }
    /** Create a <code>LayerRange</code> with the given range. */
    public static LayerRange range(int first, int last) { return new LayerRangeImpl(layerListSep, layerSep, first, last); }
    
    /** TODO
     * @deprecated mostly for testing, should be changed to function. */
    @Deprecated
    public static XDot xdot(String... components) { return new XDotImpl(components); }
    
    //----------------------------------------------------------------------------------------------
    
    private static Attribute booleanAttribute(String name, boolean value) {
        return new AttributeImpl(name, Boolean.toString(value), false);
    }
    
    private static Attribute intAttribute(String name, int value) {
        return new AttributeImpl(name, Integer.toString(value), false);
    }
    
    private static Attribute doubleAttribute(String name, double value) {
        return new AttributeImpl(name, Double.toString(value), false);
    }

    private static Attribute rangedAttribute(String name, int value, int min, int max) {
        if (value < min || value > max)
            throw new IllegalArgumentException(String.format(Locale.ROOT, "invalid '%s' attribute: %d, expected between %d and %d", name, value, min, max));
        return intAttribute(name, value);
    }
    
    private static Attribute nonNegativeAttribute(String name, int value) {
        if (value < 0)
            throw new IllegalArgumentException(String.format(Locale.ROOT, "invalid '%s' attribute: %d, expected non negative integer", name, value));
        return intAttribute(name, value);
    }
    
    private static Attribute minimumAttribute(String name, int value, int min) {
        if (value < min)
            throw new IllegalArgumentException(String.format(Locale.ROOT, "invalid '%s' attribute: %d, expected minimum %d", name, value, min));
        return intAttribute(name, value);
    }
    
    private static Attribute positiveAttribute(String name, double value) {
        if (value <= 0)
            throw new IllegalArgumentException(String.format(Locale.ROOT, "invalid '%s' attribute: %f, expected positive number", name, value));
        return doubleAttribute(name, value);
    }
    
    private static Attribute nonNegativeAttribute(String name, double value) {
        if (value < 0)
            throw new IllegalArgumentException(String.format(Locale.ROOT, "invalid '%s' attribute: %f, expected non negative number", name, value));
        return doubleAttribute(name, value);
    }
    
    private static Attribute minimumAttribute(String name, double value, double min) {
        if (value < min)
            throw new IllegalArgumentException(String.format(Locale.ROOT, "invalid '%s' attribute: %f, expected minimum %f", name, value, min));
        return doubleAttribute(name, value);
    }
    
    //==============================================================================================
    
    private static final String PATH;
    static {
        String path = System.getProperty(PATH_PROPERTY);
        if (path == null) {
            path = System.getenv(PATH_ENVIRONMENT);
            if (path == null) {
                path = "/usr/local/bin/";
                System.err.printf("neither \"%s\" property nor \"%s\" environmrnt variables set, using \"%s\"",
                    PATH_PROPERTY, PATH_ENVIRONMENT, path);
            } else {
                path += "/bin/";
            }
        }
        if (!path.endsWith("/")) {
            path += "/";
        }
        PATH = path;
    }
    
    /** Creates a graph from given input stream and writes to the output stream. */
    public static void dot(Engine engine, Format format, InputStream dotInput, OutputStream output) throws IOException, InterruptedException {
        String[] cmd = { PATH + engine.name().toLowerCase(), "-T" + format.asParameter() };
        Process process;
        try {
            process = Runtime.getRuntime().exec(cmd);
        } catch (IOException ex) {
            System.err.printf("Exception starting %s, check the \"%s\" property or the \"%s\" environment variable%n", 
                Arrays.toString(cmd), PATH_PROPERTY, PATH_ENVIRONMENT);
            throw ex;
        }

        try (OutputStream processIn = process.getOutputStream()) {
            writeAll(dotInput, processIn);
        }

        try (InputStream processOut = process.getInputStream()) {
            writeAll(processOut, output);
        }

        int ret = process.waitFor();
        if (ret != 0) {
            writeAll(process.getErrorStream(), System.err);
            throw new IOException("dot to " + format + " conversion failed, returned: " + ret);
        }
    }

    /** Creates a graph from given string and writes to the output stream. */
    public static void dot(Engine engine, Format format, String dotInput, OutputStream output) throws IOException, InterruptedException {
        dot(engine, format, new ByteArrayInputStream(dotInput.getBytes(StandardCharsets.UTF_8)), output);
    }
    
    /** Creates a graph using the Dot engine from given string and writes to the output stream. */
    public static void dot(Format format, String dotInput, OutputStream output) throws IOException, InterruptedException {
        dot(Engine.DOT, format, new ByteArrayInputStream(dotInput.getBytes(StandardCharsets.UTF_8)), output);
    }
    
    /** Creates a JPEG graph using the Dot engine from given string and writes to the output stream. */
    public static void dotToJpeg(InputStream dotInput, OutputStream pngOutput) throws IOException, InterruptedException {
        dot(Engine.DOT, Format.JPEG, dotInput, pngOutput);
    }
    
    /** Creates a PNG graph using the Dot engine from given string and writes to the output stream. */
    public static void dotToPng(InputStream dotInput, OutputStream pngOutput) throws IOException, InterruptedException {
        dot(Engine.DOT, Format.PNG, dotInput, pngOutput);
    }
    
    /** Creates a SVG graph using the Dot engine from given string and writes to the output stream. */
    public static void dotToSvg(InputStream dotInput, OutputStream svgOutput) throws IOException, InterruptedException {
        dot(Engine.DOT, Format.SVG, dotInput, svgOutput);
    }
    
    /** Creates a graph using the Dot engine from given stringreturning an image. */
    public static BufferedImage dotToImage(Format format, String dotInput) throws IOException, InterruptedException {
        return dotToImage(Engine.DOT, format, dotInput);
    }
    
    /** Creates a graph from given stringreturning an image. */
    public static BufferedImage dotToImage(Engine engine, Format format, String dotInput) throws IOException, InterruptedException {
        if (format.type != FormatType.IMAGE) {
            throw new IllegalArgumentException("non-image format: " + format);
        }
        var output = new ByteArrayOutputStream();
        try {
            dot(engine, format, dotInput, output);
            return ImageIO.read(new ByteArrayInputStream(output.toByteArray()));
        } catch (IOException ex) {
            byte[] data = output.toByteArray();
            if (data.length > 0) {
                ex.printStackTrace();
                return ImageIO.read(new ByteArrayInputStream(output.toByteArray()));
            } else {
                throw ex;
            }
        }
    }
    
    private static void writeAll(InputStream input, OutputStream output) throws IOException {
        var buffer = new byte[4096];

        int count;
        while ((count = input.read(buffer)) != -1) {
            output.write(buffer, 0, count);
        }
    }

    //----------------------------------------------------------------------------------------------
    
    static String quote(String id) {
        if (id.matches(".*[^A-Za-z0-9].*")) {
            return '"'  + id.replace("\"", "\\\"") + '"';
        } else {
            return id;
        }
    }
    
    //==============================================================================================
    
    private Dot() {
        throw new AssertionError("do not instanciate");
    }
}
