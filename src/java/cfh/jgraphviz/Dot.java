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
    public enum Engine {
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
        @Override public String toString() { return name().toLowerCase(); }
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

    /** Edge arrow direction type. */
    public enum DirType {
        /** Forward arrow type, draw head glyph only. */  
        FORWARD,
        /** Backward arrow type, draw tail glyph only. */  
        BACK,
        /** Both arrow type, draw head and tail glyph. */  
        BOTH,
        /** None arrow type, draw neither head nor tail glyph. */  
        NONE,
        ;
        @Override public String toString() { return name().toLowerCase(); }
    }
    
    //----------------------------------------------------------------------------------------------

    /** Fixed size type: whether to use the specified width and height attributes to choose node size. */
    public enum FixedSize {
        /** Size is specified by the values of the <code>width</code> and <code>height</code> attributes only and is not expanded to contain the text label. */
        TRUE,
        /** Size of a node is determined by smallest width and height needed to contain its label and image. */
        FALSE,
        /** The <code>width</code> and <code>height</code> attributes also determine the size of the node shape, but the label can be much larger. */
        SHAPE,
        ;
        @Override public String toString() { return name().toLowerCase(); }
    }
    
    //----------------------------------------------------------------------------------------------
    
    /** How basic fontnames are represented in SVG output. */
    public enum FontNames {
        /** Try to use known SVG fontnames. */
        SVG,
        /** Use known PostScript font names. */
        PS,
        /** The fontconfig font conventions are used. */
        HD,
        ;
        @Override public String toString() { return name().toLowerCase(); }
    }
    
    //----------------------------------------------------------------------------------------------

    /** Compass point used for Port Positions. */
    public enum Compass {
        N, NE, E, SE, S, SW, W, NW, 
        /** Center of node or port. */
        CENTER 
        { @Override public String toString() { return "c"; } },
        /** Appropriate side of the port adjacent to the exterior of the node should be used, if such exists. Otherwise, the center is used. */
        DEFAULT
        { @Override public String toString() { return "_"; } },
        ;
        @Override public String toString() { return name().toLowerCase(); }
    }
    
    //----------------------------------------------------------------------------------------------

    /** How an image is positioned within its containing node. */
    public enum ImagePos {
        /** Top Left. */         TopLeft("tl"),
        /** Top Centered. */     TopCenter("tc"),
        /** Top Right. */        TopRight("tr"),
        /** Middle Left. */      MiddleLeft("ml"),
        /** Middle Centered. */  MiddleCenter("mc"),
        /** Middle Right. */     MiddleRight("mr"),
        /** Bottom Left. */      BottomLeft("bl"),
        /** Bottom Centered. */  BottomCenter("bc"),
        /** Bottom Right. */     BottomRight("br"),
        ;
        private final String value;
        private ImagePos(String value) { this.value = value; }
        @Override public String toString() { return value; }
    }
    
    //----------------------------------------------------------------------------------------------

    /** How an image fills its containing node. */
    public enum ImageScale {
        /** Image retains its natural size. */         NONE("false"),
        /** Image is uniformly scale. */               UNIFORMLY("true"),
        /** The width of the image is scaled. */       WIDTH("width"),
        /** The height of the image is scaled. */      HEIGHT("height"),
        /** Height and Width are scaled separately. */ BOTH("both"),
        ;
        private final String value;
        private ImageScale(String value) { this.value = value; }
        @Override public String toString() { return value; }
    }
    
    //----------------------------------------------------------------------------------------------
    
    /** How to treat a node whose name has the form <code>|edgelabel|*</code> as a special node representing an edge label. */
    public enum LabelScheme {
        /** Produces no effect, the default (<code>0</code>). */
        NO_EFFECT(0),
        /** Uses a penalty-based method to make that kind of node close to the center of its neighbor (<code>1</code>). */
        NEIGHBOR(1),
        /** Uses a penalty-based method to make that kind of node close to the old center of its neighbor (<code>2</code>). */
        OLD_CENTER(2),
        /** Two-step process of overlap removal and straightening (<code>3</code>). */
        TWO_STEPS(3),
        ;
        private final int value;
        private LabelScheme(int value) { this.value = value; }
        @Override public String toString() { return Integer.toString(value); }
    }
    
    //----------------------------------------------------------------------------------------------

    /** Justification for graph & cluster labels. */
    public enum LabelJust {
        /** The label is centered (<code>c</code>), the default. */
        CENTER,
        /** The label is right-justified within bounding rectangle (<code>r</code>). */
        RIGHT,
        /** The label is left-justified (<code>l</code>). */
        LEFT,
        ;
        private final String value = name().toLowerCase().substring(0, 1);
        @Override public String toString() { return value; }
    }
    
    //----------------------------------------------------------------------------------------------
    
    /** Vertical placement of labels for nodes, root graphs and clusters.*/
    public enum LabelLoc {
        CENTER,
        TOP,
        BOTTOM,
        ;
        private final String value = name().toLowerCase().substring(0, 1);
        @Override public String toString() { return value; }
    }
    
    //----------------------------------------------------------------------------------------------

    /** Technique for optimizing the layout; <code>neato</code>, <code>sfdp</code>? only. */
    public enum Mode {
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
        private final String value;
        private Mode() { this.value = name().toLowerCase(); }
        private Mode(String value ) { this.value = value; }
        @Override public String toString() { return value; }
    }
    
    //----------------------------------------------------------------------------------------------
    
    /** Specifies how the distance matrix is computed for the input graph; <code>neato</code> only. */
    public enum Model {
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
        @Override public String toString() { return name().toLowerCase(); }
    }
    
    //----------------------------------------------------------------------------------------------
    
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
    public static Attr.G beautify(boolean beautify) { return new AttributeImpl("beautify", beautify); }
    
     /** Canvas background color. */
    public static Attr.GS bgcolor(Color color) { return new AttributeImpl("bgcolor", color); }
    
    /** Canvas background color. */
    public static Attr.GS bgcolor(ColorList colors) { return new AttributeImpl("bgcolor", colors); }
    
    /** Center the drawing in the output canvas. */
    public static Attr.G center() { return center(true); }
    
    /** Center the drawing in the output canvas, default: <code>false</code>. */
    public static Attr.G center(boolean center) { return new AttributeImpl("center", center); }
    
    /** Character encoding used when interpreting string input as a text label, , default: <code>UTF-8</code>. 
     * @deprecated testing */
    @Deprecated
    public static Attr.G charset(String charset) { return new AttributeImpl("charset", charset); }
    
    /** Classnames to attach to the node, edge, graph, or cluster's SVG element; <code>svg</code> only. */
    public static Attr.GSNE classname(String... names) { return new AttributeImpl("class", String.join(" ", names)); }
    
    /** Subgraph is a cluster. */
    public static Attr.S cluster() { return cluster(true); }
    
    /** Whether the subgraph is a cluster, default: <code>false</code>. */
    public static Attr.S cluster(boolean cluster) { return new AttributeImpl("cluster", cluster); }
    
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
    public static Attr.G compound(boolean allow) { return new AttributeImpl("compound", allow); }
    
    /** Use edge concentrators - merges multiedges into a single edge and causes partially parallel edges to share part of their paths. */
    public static Attr.G concentrate() { return concentrate(true); }
    
    /** Use edge concentrators, merges multiedges into a single edge and causes partially parallel edges to share part of their paths. */
    public static Attr.G concentrate(boolean concentrate) { return new AttributeImpl("concentrate", concentrate); }
    
    /** Edge is not used in ranking the nodes; <code>dot</code> only. */
    public static Attr.E unconstraint() { return constraint(false); }
    
    /** If <code>false</code> Edge is not used in ranking the nodes; <code>dot</code> only. */
    public static Attr.E constraint(boolean constraint) { return new AttributeImpl("constraint", constraint); }
    
    /** Factor damping force motions, default: <code>0.99</code>, minimum: <code>0.0</code>; <code>neato</code> only. */
    public static Attr.G damping(double factor) { return nonNegativeAttribute("Damping", factor); }
    
    /** Connect the edge label to the edge with a line. */
    public static Attr.E decorate() { return decorate(true); }
    
    /** Whether to connect the edge label to the edge with a line. */
    public static Attr.E decorate(boolean decorate) { return new AttributeImpl("decorate", decorate); }
    
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
    public static Attr.G diredgeconstraints(boolean constrain) { return new AttributeImpl("diredgeconstraints", constrain); }
    
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
    public static Attr.N fixedsize(boolean fixed) { return new AttributeImpl("fixedsize", fixed); }
    
    /** Use the specified <code>width</code> and <code>height</code> attributes to choose node size. */
    public static Attr.N fixedsize() { return new AttributeImpl("fixedsize", true); }
    
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
    public static Attr.G forcelabels(boolean force) { return new AttributeImpl("forcelabels", force); }
    
    /** If a gradient fill is being used, this determines the angle of the fill, default: <code>0</code>, minimum: <code>0</code>. */
    public static Attr.GSN gradientangle(int angle) { return nonNegativeAttribute("gradientangle", angle); }
    
    /** Name for a group of nodes, for bundling edges avoiding crossings; <code>dot</code> only. */
    public static Attr.N group(String name) { return new AttributeImpl("group", name); }
    
    /** The end of the edge goes to the center. */
    public static Attr.E noheadclip() { return headclip(false); }
    
    /** Clip the head of an edge to the boundary of the head node, otherwise the end of the edge goes to the center, default: <code>true</code>. */
    public static Attr.E headclip(boolean clip) { return new AttributeImpl("headclip", clip); }
    
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
    public static Attr.G inputscale(double scale) { return new AttributeImpl("inputscale", scale); }
    
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
    public static Attr.E labelfloat(boolean allow) { return new AttributeImpl("labelfloat", allow); }
    
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
    public static Attr.G landscape(boolean landscape) { return new AttributeImpl("landscape", landscape); }
    
    /** Specifies layers in which the node, edge or cluster is present. */
    public static Attr.SNE layer(LayerRange... ranges) { 
        return new AttributeImpl("layer", Arrays.stream(ranges).map(LayerRange::toString).collect(joining(layerListSep)));
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
        return new AttributeImpl("layerselect", Arrays.stream(ranges).map(LayerRange::toString).collect(joining(layerListSep)));
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
    public static Attr.G levelsgap(double strictness) { return new AttributeImpl("levelsgap", strictness); }
    
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
    public static Attr.GSN margin(double margin) { return new AttributeImpl("margin", margin); }
    
    /** For graphs, this sets x and y margins of canvas, in inches.</br>
     *  For nodes, this attribute specifies space left around the node's label, in inches, default <code>0.11,0.055</code>.
     */
    public static Attr.GN margin(double x, double y) { return new AttributeImpl("margin", new PointImpl(false, x, y)); }
    
    /** For graphs, this sets x and y margins of canvas, in inches.</br>
     *  For nodes, this attribute specifies space left around the node's label, in inches, default <code>0.11,0.055</code>.
     */
    public static Attr.GN margin(Point margin) { return new AttributeImpl("margin", margin); }
    
    /** Sets the number of iterations used; <code>neato</code>, <code>fdp</code> only. */
    public static Attr.G maxiter(int iterations) { return new AttributeImpl("maxiter", iterations); }
    
    /** Scale factor for mincross (mc) edge crossing minimiser parameters, default: <code>1.0</code>; <code>dot</code> only. */
    public static Attr.G mclimit(double factor) { return new AttributeImpl("mclimit", factor); }
    
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
    public static Attr.G newrank(boolean ignore) { return new AttributeImpl("newrank", ignore); }
    
    
    
    
    
    // TODO 
    
    
    
    
    
    
    /** Arbitrary attribute. 
     *  @deprecated for testing, maybe delted (or not) 
     */
    @Deprecated
    public static Attr.GSNE attribute(String name, Object value) { return new AttributeImpl(name, value); }
    
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

    private static Attribute rangedAttribute(String name, int value, int min, int max) {
        if (value < min || value > max)
            throw new IllegalArgumentException(String.format(Locale.ROOT, "invalid '%s' attribute: %d, expected between %d and %d", name, value, min, max));
        return new AttributeImpl(name, value);
    }
    
    private static Attribute nonNegativeAttribute(String name, int value) {
        if (value < 0)
            throw new IllegalArgumentException(String.format(Locale.ROOT, "invalid '%s' attribute: %d, expected non negative integer", name, value));
        return new AttributeImpl(name, value);
    }
    
    private static Attribute minimumAttribute(String name, int value, int min) {
        if (value < min)
            throw new IllegalArgumentException(String.format(Locale.ROOT, "invalid '%s' attribute: %d, expected minimum %d", name, value, min));
        return new AttributeImpl(name, value);
    }
    
    private static Attribute positiveAttribute(String name, double value) {
        if (value <= 0)
            throw new IllegalArgumentException(String.format(Locale.ROOT, "invalid '%s' attribute: %f, expected positive number", name, value));
        return new AttributeImpl(name, value);
    }
    
    private static Attribute nonNegativeAttribute(String name, double value) {
        if (value < 0)
            throw new IllegalArgumentException(String.format(Locale.ROOT, "invalid '%s' attribute: %f, expected non negative number", name, value));
        return new AttributeImpl(name, value);
    }
    
    private static Attribute minimumAttribute(String name, double value, double min) {
        if (value < min)
            throw new IllegalArgumentException(String.format(Locale.ROOT, "invalid '%s' attribute: %f, expected minimum %f", name, value, min));
        return new AttributeImpl(name, value);
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
