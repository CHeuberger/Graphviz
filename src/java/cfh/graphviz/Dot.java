/*
 * Copyright: Carlos F. Heuberger. All rights reserved.
 *
 */
package cfh.graphviz;

import static java.util.Objects.requireNonNull;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import javax.imageio.ImageIO;

/**
 * @author Carlos F. Heuberger, 2023-03-03
 *
 */
public class Dot {

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
        DOT,NEATO;
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
    
    //==============================================================================================

    /** Creates a new Grarph. */
    public static Graph graph() {
        return new GraphImpl();
    }
    
    /** Creates a new named Grarph. */
    public static Graph graph(String id) {
        return new GraphImpl(id);
    }
    
    /** Creates a new Node. */
    public static NodeId node(String id) {
        return new NodeImpl(id); // TODO
    }
    
    /** Creates a new Edge. */
    public static Edge edge(Source source, Target target) {
        return new EdgeImpl(source, target);
    }
    
    /** Creates a Cluster. */
    public static Subgraph cluster() {
        return null; // TODO
    }
    /** Creates a new Subgraph. */
    public static Subgraph subgraph() {
        return null; // TODO
    }
    
    /** Creates a new named Subgraph. */
    public static Subgraph subgraph(String id) {
        return null; // TODO
    }
    
    /** Creates a new Subgraph. */
    public static Subgraph subgraph(NodeId first, NodeId... nodes) {
        return null; // TODO
    }
    
    //==============================================================================================
    
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
    
    /** Creates a graph from given input stream and writes to the output stream. */
    public static void dot(Engine engine, Format format, InputStream dotInput, OutputStream output) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(
            new String[] { 
                    PATH + engine.name().toLowerCase(), 
                    "-T" + format.asParameter() 
                    });

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
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        dot(engine, format, dotInput, output);
        return ImageIO.read(new ByteArrayInputStream(output.toByteArray()));
    }
    
    private static void writeAll(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[4096];

        int count;
        while ((count = input.read(buffer)) != -1) {
            output.write(buffer, 0, count);
        }
    }

    //----------------------------------------------------------------------------------------------
    
    static String quote(String id) {
        return '"'  + id.replace("\"", "\\\"") + '"';
    }
    
    //==============================================================================================
    
    static final String INDENT = "  ";
    
    private Dot() {
        throw new AssertionError("do not instanciate");
    }
}
