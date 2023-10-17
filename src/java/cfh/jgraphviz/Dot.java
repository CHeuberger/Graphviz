/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.jgraphviz;

import static java.util.Objects.requireNonNull;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.imageio.ImageIO;

/**
 * @author Carlos F. Heuberger, 2023-03-03
 *
 */
public class Dot {
    
    static final int INDENT = 2;
    
    private static final String PATH_PROPERTY = "GraphPath";
    private static final String PATH_ENVIRONMENT = "GRAPHVIZ_HOME";

    /** Node Port. */
    public enum Port {
        // n | ne | e | se | s | sw | w | nw | c | _
        N, NE, E, SE, S, Sw, W, NW, C, DEFAULT;

        String format() {
            if (this == DEFAULT) return "_";
            else return name().toLowerCase();
        }
    }
    
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
        PATCHWORK;
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

    public enum DirType {
        FORWARD,
        BACK,
        BOTH,
        NONE;
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
    
    //==============================================================================================

    /** Creates a new (undirected) Graph. */
    public static Graph graph() {
        return new GraphImpl();
    }
    
    /** Creates a new named (undirected) Graph. */
    public static Graph graph(String id) {
        return new GraphImpl(id);
    }
    
    /** Creates a directed Graph. */
    public static Graph digraph() {
        return new GraphImpl().directed();
    }
    
    /** Creates a directed named Graph. */
    public static Graph digraph(String id) {
        return new GraphImpl(id).directed();
    }
    
    /** Creates a new Node. */
    public static NodeId node(String id) {
        return new NodeImpl(id); // TODO
    }
    
    /** Creates a new Edge. */
    public static Edge edge(Source source, Target target) {
        return new EdgeImpl(source, target);
    }
    
    /** Creates a new Subgraph. */
    public static Subgraph subgraph() {
        return new SubgraphImpl();
    }
    
    /** Creates a new named Subgraph. */
    public static Subgraph subgraph(String id) {
        return new SubgraphImpl(id);
    }
    
    /** Creates a new Subgraph. */
    public static Subgraph subgraph(Node... nodes) {
        var sub = new SubgraphImpl();
        Arrays.stream(nodes).forEach(sub::add);
        return sub;
    }
    
    //----------------------------------------------------------------------------------------------
    
    /** Arbitrary backgorund using the <a href="https://www.graphviz.org/docs/attr-types/xdot/">xodt format</a>. */
    public static Attr.G _background(XDot xdot) { return new Attribute("_background", xdot); }
    
    /** Preferred area for a node or empty cluster, default: <code>1.0</code>. <a href="https://www.graphviz.org/docs/layouts/patchwork/">patchwork</a> only. */
    public static Attr.NS area(double area) { return new Attribute("area", positive(area, "area")); }
    
    /** Arrow style of the head node of an edge. */
    public static Attr.E arrowhead(ArrowType type) { return new Attribute("arrowhead", type); }
    
    /** Multiplicative scale factor for arrow heads, default: <code>1.0</code>. */
    public static Attr.E arrowsize(double scale) { return new Attribute("arrowsize", nonNegative(scale, "arrowsize")); }
    
    /** Arrow style of the tail node of an edge. */
    public static Attr.E arrowtail(ArrowType type) { return new Attribute("arrowtail", type); }
    
    /** Draw leaf nodes uniformly in a circle around the root node in <code>sfdp</code>. */
    public static Attr.G beautify() { return beautify(true); }
    
    /** Draw leaf nodes uniformly in a circle around the root node in <code>sfdp</code>, default: <code>false</code>. */
    public static Attr.G beautify(boolean beautify) { return new Attribute("beautify", beautify); }
    
     /** Canvas background color. */
    public static Attr.GS bgcolor(Color color) { return new Attribute("bgcolor", color); }
    
    /** Canvas background color. */
    public static Attr.GS bgcolor(ColorList colors) { return new Attribute("bgcolor", colors); }
    
    /** Center the drawing in the output canvas. */
    public static Attr.G center() { return center(true); }
    
    /** Center the drawing in the output canvas, default: <code>false</code>. */
    public static Attr.G center(boolean center) { return new Attribute("center", center); }
    
    /** Character encoding used when interpreting string input as a text label, , default: <code>UTF-8</code>. 
     * @deprecated testing */
    @Deprecated
    public static Attr.G charset(String charset) { return new Attribute("charset", charset); }
    
    /** Classnames to attach to the node, edge, graph, or cluster's SVG element; <code>svg</code> only. */
    public static Attr.GNES classname(String... names) { return new Attribute("class", String.join(" ", names)); }
    
    /** Subgraph is a cluster. */
    public static Attr.S cluster() { return cluster(true); }
    
    /** Whether the subgraph is a cluster, default: <code>false</code>. */
    public static Attr.S cluster(boolean cluster) { return new Attribute("cluster", cluster); }
    
    /** Mode used for handling clusters, default: <code>true</code>. Bounding rectangle drawn arround cluster and label, if present. */
    public static Attr.G clusterrank(boolean local) { return new Attribute("clusterrank", local ? "local" : "global"); }
    
    /**  Basic drawing color for graphics, not text. */
    public static Attr.NES color(ColorList color) { return new Attribute("color", color); }
    
    /** A color scheme namespace: the context for interpreting color names. 
     * @deprecated TODO testing only, need ColorScheme enum?. */
    public static Attr.GNES colorscheme(String name) { return new Attribute("colorscheme", name); }
    
    /** Comments are inserted into output. */
    public static Attr.GNE comment(String text) { return new Attribute("comment", text); }
    
    /** Allow edges between clusters. */
    public static Attr.G compound() { return compound(true); }
    
    /** Allow edges between clusters. */
    public static Attr.G compound(boolean allow) { return new Attribute("compound", allow); }
    
    /** Use edge concentrators - merges multiedges into a single edge and causes partially parallel edges to share part of their paths. */
    public static Attr.G concentrate() { return concentrate(true); }
    
    /** Use edge concentrators, merges multiedges into a single edge and causes partially parallel edges to share part of their paths. */
    public static Attr.G concentrate(boolean concentrate) { return new Attribute("concentrate", concentrate); }
    
    /** Edge is not used in ranking the nodes; <code>dot</code> only. */
    public static Attr.E unconstraint() { return constraint(false); }
    
    /** If <code>false</code> Edge is not used in ranking the nodes; <code>dot</code> only. */
    public static Attr.E constraint(boolean constraint) { return new Attribute("constraint", constraint); }
    
    /** Factor damping force motions; <code>neato</code> only. */
    public static Attr.G damping(double factor) { return new Attribute("damping", nonNegative(factor, "damping")); }
    
    /** Connect the edge label to the edge with a line. */
    public static Attr.E decorate() { return decorate(true); }
    
    /** Whether to connect the edge label to the edge with a line. */
    public static Attr.E decorate(boolean decorate) { return new Attribute("decorate", decorate); }
    
    /** The distance between nodes in separate connected components; <code>neato</code> only. */
    public static Attr.G defaultdist(double dist) { return new Attribute("defaultdist", nonNegative(dist, "defaultdist")); }
    
    /** Set the number of dimensions used for the layout (2-10); <code>neato</code>, <code>fdp</code> and <code>sfdp</code> only. */
    public static Attr.G dim(int dim) { return new Attribute("dim", range(dim, 2, 10, "dim")); }
    
    /** Set the number of dimensions used for rendering (2-10); <code>neato</code>, <code>fdp</code> and <code>sfdp</code> only. */
    public static Attr.G dimen(int dimen) { return new Attribute("dimen", range(dimen, 2, 10, "dimen")); }
    
    /** Edge type for drawing arrowheads. */
    public static Attr.E dir(DirType dir) { return new Attribute("dir", dir); }
    
    /** Constrain most edges to point downwards; <code>neato</code> only. */
    public static Attr.G diredgeconstraints() { return diredgeconstraints(true); }
    
    /** Constrain most edges to point downwards; <code>neato</code> only. */
    public static Attr.G diredgeconstraints(boolean constrain) { return new Attribute("diredgeconstraints", constrain); }
    
    /** Distortion factor for <code>shape=polygon</code>. Positive values cause top part to be larger than bottom; negative values do the opposite. */
    public static Attr.N distortion(double distortion) { return new Attribute("distortion", minimum(distortion, -100, "distortium")); }
    
    /** Specifies the expected number of pixels per inch on a display device; bitmap output, <code>svg</code> only. */
    public static Attr.G dpi(double dpi) { return new Attribute("dpi", nonNegative(dpi, "dpi")); }
    
    /** The link for the non-label parts of an edge; map, <code>svg</code> only. */
    public static Attr.E edgehref(String url) { return new Attribute("edgehref", url); }
    
    /** Browser window to use for the <code>edgeURL</code> link; map, <code>svg</code> only. */
    public static Attr.E edgetarget(String target) { return new Attribute("edgetarget", target); }
    
    /** Tooltip annotation attached to the non-label part of an edge; <code>cmap</code>, <code>svg</code> only. */
    public static Attr.E edgetooltip(String tooltip) { return new Attribute("edgetooltip", tooltip); }
    
    /** The link for the non-label parts of an edge; map, <code>svg</code> only. */
    public static Attr.E edgeURL(String url) { return new Attribute("edgeURL", url); }
    
    epsilon
    // TODO 
    
    
    
    
    
    /** Label attribute. */
    public static Attr.GNES label(String label) { return new Attribute("label", label); }
    
    /** Font size attribute. */
    public static Attr.GNES fontsize(double size) { return new Attribute("fontsize", size); }
//        return new Attribute("fontsize", new BigDecimal(size).setScale(1, RoundingMode.HALF_UP));
    
    /** Arbitrary attribute. 
     *  @deprecated for testing, maybe delted (or not) 
     */
    @Deprecated
    public static Attr.GNES attribute(String name, Object value) { return new Attribute(name, value); }
    
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
    
    /** TODO
     * @deprecated mostly for testing, should be changed to function. */
    @Deprecated
    public static XDot xdot(String... components) { return new XDotImpl(components); }
    
    //----------------------------------------------------------------------------------------------

    private static int range(int value, int min, int max, String name) {
        if (value < min || value > max)
            throw new IllegalArgumentException("invalid '%s' attribute: %d, expected between %d and %d".formatted(name, value, min, max));
        return value;
    }
    
    private static double positive(double value, String name) {
        if (value <= 0)
            throw new IllegalArgumentException("invalid '%s' attribute: %f, expected positive number".formatted(name, value));
        return value;
    }
    
    private static double nonNegative(double value, String name) {
        if (value < 0)
            throw new IllegalArgumentException("invalid '%s' attribute: %f, expected non negative number".formatted(name, value));
        return value;
    }
    
    private static double minimum(double value, double min, String name) {
        if (value < min)
            throw new IllegalArgumentException("invalid '%s' attribute: %f, expected minimum %f".formatted(name, value, min));
        return value;
    }
    
    //==============================================================================================
    
    private static final String PATH;
    static {
        String cmd = System.getProperty(PATH_PROPERTY);
        if (cmd == null) {
            String path = System.getenv(PATH_ENVIRONMENT);
            if (path == null) {
                cmd = "/usr/local/bin/";
                System.err.printf("neither \"%s\" property nor \"%s\" environmrnt variables set, using \"%s\"",
                    PATH_PROPERTY, PATH_ENVIRONMENT, cmd);
            } else {
                cmd = path + "/bin/";
            }
        }
        if (!cmd.endsWith("/")) {
            cmd += "/";
        }
        PATH = cmd;
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
