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

import cfh.graph.attr.AnyAttribute;
import cfh.graph.attr.Color;
import cfh.graph.attr.CommentAttribute;
import cfh.graph.attr.FontName;
import cfh.graph.attr.FontSize;

/**
 * @author Carlos F. Heuberger, 2023-02-24
 *
 */
public final class Dot {
    
    private Dot() {
        throw new AssertionError("do not instanciate");
    }
    
    //==============================================================================================

//    // FORMATS
//    public static final Format JPEG = Format.JPEG;
//    public static final Format PNG = Format.PNG;
//    public static final Format SVG = Format.SVG;
    
    // FONTNAMES
    // TODO
    
    // STYLES
    // TODO
    
    // ENGINE
    // TODO
    
    /** Start a new Graph. */
    public static Graph graph(String name) {
        return new Graph(name);
    }
    
    /** Start a new Node. */
    public static Node node(String name) {
        return new Node(name);
    }
    
    /** Start a new Edge. */
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

    public static void dot(Format format, String dotInput, OutputStream output) throws IOException, InterruptedException {
        dot(format, new ByteArrayInputStream(dotInput.getBytes(StandardCharsets.UTF_8)), output);
    }
    
    public static void dotToJpeg(InputStream dotInput, OutputStream pngOutput) throws IOException, InterruptedException {
        dot(Format.JPEG, dotInput, pngOutput);
    }
    
    public static void dotToPng(InputStream dotInput, OutputStream pngOutput) throws IOException, InterruptedException {
        dot(Format.PNG, dotInput, pngOutput);
    }
    
    public static void dotToSvg(InputStream dotInput, OutputStream svgOutput) throws IOException, InterruptedException {
        dot(Format.SVG, dotInput, svgOutput);
    }
    
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
        public String asParameter() { return name().toLowerCase().replace("$", "."); }
        public FormatType type() { return type; }
    }
}
