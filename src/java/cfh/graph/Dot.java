/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graph;

import static java.util.Objects.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import javax.imageio.ImageIO;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
public final class Dot {
    
    static final String INDENT = "  ";
    private Dot() {
        throw new AssertionError("do not instanciate");
    }
    
    //==============================================================================================

    public enum Port {
        // n | ne | e | se | s | sw | w | nw | c | _
        N, NE, E, SE, S, Sw, W, NW, C, DEFAULT;

        String format() {
            if (this == DEFAULT) return "_";
            else return name().toLowerCase();
        }
    }
    
    public enum Engine {
        DOT,NEATO;
    }

    // FORMATS
    public static final Format JPEG = Format.JPEG;
    public static final Format PNG = Format.PNG;
    public static final Format SVG = Format.SVG;
    
    // FONTNAMES
    // TODO
    
    // STYLES
    // TODO
    
    // ENGINE
    // TODO
    
    /** Start a new Graph. */
    public static Graph graph() {
        return new Graph();
    }
    
    /** Start a new Graph. */
    public static Graph graph(String name) {
        return new Graph(name);
    }
    
    /** Creates a new Node. */
    public static Node node(String name) {
        return new Node(name);
    }
    
    /** Creates a new Node. */
    public static Node node(String name, Port port) {
        return new Node(name, port);
    }
    
    /** Creates a new Node. */
    public static Node node(String name, String portId) {
        return new Node(name, portId);
    }
    
    /** Creates a new Node. */
    public static Node node(String name, String portId, Port port) {
        return new Node(name, portId, port);
    }
    
    /** Creates a new Edge. */
    public static Edge edge(Source source, Target target) {
        return new Edge(source, target);
    }

    /** Creates a color attribute.
     * <p>Valid value formats: 
     * <ul> <li><code>"#rrggbb"</code>
     *      <li><code>"#rrggbbaa"<//code>
     *      <li><code>"H S V"<//code> 0.0 <= H,S,V <= 1.0
     *      <li><<code>name</code> a color name, see {@link Color}
     * </ul>
     */
    public static Color color(String value) {
        return new Color(value);
    }
    
    /** Creates a color attribute. */
    public static Color color(int r, int g, int b) {
        return new Color("#%02x%02x%02x".formatted( 
            checkARGB(r, "r:"),
            checkARGB(g, "g:"),
            checkARGB(b, "b:") ));
    }
    
    /** Creates a color attribute. */
    public static Color color(int a, int r, int g, int b) {
        return new Color("#%02x%02x%02x%02x".formatted( 
            checkARGB(r, "r:"),
            checkARGB(g, "g:"),
            checkARGB(b, "b:"),
            checkARGB(a, "a:") ));
    }
    
    /** Creates a color attribute. 
     *  <p>0.0 <= H,S,V <= 1.0 
     */
    public static Color color(double h, double s, double v) {
        return new Color(String.format(Locale.ROOT, "%.3f %.3f %.3f", 
            checkHSV(h, "h:"),
            checkHSV(s, "s:"),
            checkHSV(v, "v:") ));
    }
    
    /** Creates a comment. */
    public static Attribute.CommentAttribute comment(String comment) {
        return new Attribute.CommentAttribute(comment);
    }
    
    /** Sets the font name. */
    public static Attribute.FontName font(String name) {
        return new Attribute.FontName(name);
    }
    
    /** Sets the font size. */
    public static Attribute.FontSize font(double size) {
        return new Attribute.FontSize(size);
    }
    
    /** Creates a label (GNEC). */
    public static Attribute.AnyAttribute label(String label) {
        return new Attribute.AnyAttribute("label", label);
    }
    
    /** Creates a cluster (G). */
    public static Subgraph cluster() {
        return new Subgraph();
    }
    
    /** Creates a subgraph (G). */
    public static Subgraph subgraph() {
        return new Subgraph("");
    }
    
    /** Creates a named subgraph (G). */
    public static Subgraph subgraph(String name) {
        return new Subgraph(name);
    }
    
    /** Creates an arbitrary attribute. */
    public static Attribute.AnyAttribute attr(String name, Object value) {
        return new Attribute.AnyAttribute(name, value);
    }
    
    //----------------------------------------------------------------------------------------------
    
    static String quote(String id) {
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

    //==============================================================================================
    
    private static final String DOT = "dot";
    
    private static final String PATH;
    static {
        String cmd = System.getProperty("GraphPath");
        if (cmd == null) {
            String path = System.getenv("GRAPHVIZ_HOME");
            if (path == null) {
                cmd = "/usr/local/bin/";
                System.err.println("neither \"Graph\" nor \"GRAPHVIZ_HOME\" environmrnt variables set, using \"" + cmd + "\"");
            } else {
                cmd = path + "/bin/";
            }
        }
        if (!cmd.endsWith("/")) {
            cmd += "/";
        }
        PATH = cmd;
    }
    
    /** Creates a graph using the Dot engine from given input stream and writes to the output stream. */
    public static void dot(Format format, InputStream dotInput, OutputStream output) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(new String[] { PATH+DOT, "-T" + format.asParameter() });

        try (OutputStream processIn = process.getOutputStream()) {
            writeAll(dotInput, processIn);
        }

        try (InputStream processOut = process.getInputStream()) {
            writeAll(processOut, output);
        }

        int ret = process.waitFor();
        if (ret != 0) {
            writeAll(process.getErrorStream(), System.err);
            throw new RuntimeException("dot to " + format + " conversion failed, returned: " + ret);
        }
    }

    /** Creates a graph using the Dot engine from given string and writes to the output stream. */
    public static void dot(Format format, String dotInput, OutputStream output) throws IOException, InterruptedException {
        dot(format, new ByteArrayInputStream(dotInput.getBytes(StandardCharsets.UTF_8)), output);
    }
    
    /** Creates a JPEG graph using the Dot engine from given string and writes to the output stream. */
    public static void dotToJpeg(InputStream dotInput, OutputStream pngOutput) throws IOException, InterruptedException {
        dot(Format.JPEG, dotInput, pngOutput);
    }
    
    /** Creates a PNG graph using the Dot engine from given string and writes to the output stream. */
    public static void dotToPng(InputStream dotInput, OutputStream pngOutput) throws IOException, InterruptedException {
        dot(Format.PNG, dotInput, pngOutput);
    }
    
    /** Creates a SVG graph using the Dot engine from given string and writes to the output stream. */
    public static void dotToSvg(InputStream dotInput, OutputStream svgOutput) throws IOException, InterruptedException {
        dot(Format.SVG, dotInput, svgOutput);
    }
    
    /** Creates a graph using the Dot engine from given stringreturning an image. */
    public static BufferedImage dotToImage(Format format, String dotInput) throws IOException, InterruptedException {
        if (format.type != FormatType.IMAGE) {
            throw new IllegalArgumentException("non-image format: " + format);
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        dot(format, dotInput, output);
        return ImageIO.read(new ByteArrayInputStream(output.toByteArray()));
    }
    
    private static void writeAll(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[4096];

        int count;
        while ((count = input.read(buffer)) != -1) {
            output.write(buffer, 0, count);
        }
    }

    //==============================================================================================
    
    private enum FormatType {
        IMAGE, TEXT;
    }
    
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
}
